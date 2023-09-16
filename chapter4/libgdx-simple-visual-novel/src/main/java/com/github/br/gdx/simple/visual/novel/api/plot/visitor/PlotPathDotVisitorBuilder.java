package com.github.br.gdx.simple.visual.novel.api.plot.visitor;

import com.github.br.gdx.simple.visual.novel.api.ElementId;
import com.github.br.gdx.simple.visual.novel.api.context.FullNodeId;
import com.github.br.gdx.simple.visual.novel.api.node.Node;
import com.github.br.gdx.simple.visual.novel.api.node.NodeVisitor;
import com.github.br.gdx.simple.visual.novel.api.scene.Edge;
import com.github.br.gdx.simple.visual.novel.inner.SceneLinkNode;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class PlotPathDotVisitorBuilder<T extends NodeVisitor> implements PlotVisitor<T> {

    private ElementId beginSceneId;
    private final LinkedHashMap<ElementId, LinkedHashMap<ElementId, NodeElementData>> nodes = new LinkedHashMap<>();
    private final LinkedHashMap<ElementId, LinkedHashMap<ElementId, EdgeElementData>> edges = new LinkedHashMap<>();

    // current
    private ElementId currentSceneId;
    private ElementId currentNodeId;

    // prev
    private ElementId prevSceneId;
    private ElementId prevNodeId;

    @Override
    public void visitNode(ElementId sceneId, ElementId nodeId, Node<?, T> node) {
        LinkedHashMap<ElementId, NodeElementData> scenesIds = nodes.get(sceneId);
        if (scenesIds == null) {
            scenesIds = new LinkedHashMap<>();
            nodes.put(sceneId, scenesIds);
        }
        boolean isLink = node instanceof SceneLinkNode;
        scenesIds.put(nodeId, new NodeElementData(nodeId, isLink));
    }

    @Override
    public void visitEdge(ElementId sceneId, ElementId nodeId, Edge<?> edge) {
        LinkedHashMap<ElementId, EdgeElementData> scenesIds = edges.get(sceneId);
        if (scenesIds == null) {
            scenesIds = new LinkedHashMap<>();
            edges.put(sceneId, scenesIds);
        }
        scenesIds.put(nodeId, new EdgeElementData(nodeId, edge));
    }

    @Override
    public void visitBeginNodeId(ElementId sceneId, ElementId beginNodeId) {
        LinkedHashMap<ElementId, NodeElementData> nodes = this.nodes.get(sceneId);
        NodeElementData nodeData = nodes.get(beginNodeId);
        nodeData.isBegin = true;
    }

    @Override
    public void visitBeginSceneId(ElementId sceneId) {
        this.beginSceneId = sceneId;
    }

    public void visitCurrentNodeId(ElementId sceneId, ElementId nodeId, String currentNodeMessage) {
        LinkedHashMap<ElementId, NodeElementData> nodes = this.nodes.get(sceneId);
        NodeElementData nodeData = nodes.get(nodeId);

        nodeData.isErrorNode = true;
        nodeData.errorMessage = "[" + nodeId.getId() + "] " + currentNodeMessage;

        this.currentSceneId = sceneId;
        this.currentNodeId = nodeId;
    }

    public void visitPlotPath(List<FullNodeId> path) {
        if(path == null) {
            return;
        }

        for (FullNodeId fullNodeId : path) {
            LinkedHashMap<ElementId, NodeElementData> nodesMap = nodes.get(fullNodeId.sceneId);
            NodeElementData nodeElementData = nodesMap.get(fullNodeId.nodeId);
            nodeElementData.isVisited = true;
        }

        if(path.size() > 1) {
            FullNodeId fullNodeId = path.get(path.size() - 1);
            prevSceneId = fullNodeId.sceneId;
            prevNodeId = fullNodeId.nodeId;
        }
    }

    public String build() {
        StringBuilder builder = new StringBuilder();
        builder.append("digraph G {\n");

        int counter = 1;
        for (Map.Entry<ElementId, LinkedHashMap<ElementId, NodeElementData>> sceneEntry : nodes.entrySet()) {
            builder.append("    subgraph ").append("cluster_").append(counter).append(" {\n");
            builder.append("        label =").append("\"").append(sceneEntry.getKey().getId()).append("\";").append("\n");

            for (Map.Entry<ElementId, NodeElementData> nodeEntry : sceneEntry.getValue().entrySet()) {
                builder.append("        ").append(nodeEntry.getKey());
                builder.append("[style=filled");

                NodeElementData value = nodeEntry.getValue();
                if(value.isErrorNode) {
                    builder.append(", color=red");
                } else if(value.isVisited) {
                    builder.append(", color=green");
                }

                String shape;
                if(value.isLink) {
                    shape = ", shape=box";
                } else if(value.isBegin) {
                    shape = ", shape=Mdiamond";
                } else {
                    shape = ", shape=circle";
                }
                builder.append(shape);
                if(value.errorMessage != null) {
                    builder.append(", label=\"").append(value.errorMessage).append("\"");
                }

                builder.append("];\n");
            }
            builder.append("    }").append("\n");
            counter++;
        }

        for (Map.Entry<ElementId, LinkedHashMap<ElementId, EdgeElementData>> edges : edges.entrySet()) {
            ElementId sceneId = edges.getKey();
            LinkedHashMap<ElementId, EdgeElementData> value = edges.getValue();
            for (Map.Entry<ElementId, EdgeElementData> edgeEntry : value.entrySet()) {
                EdgeElementData edgeData = edgeEntry.getValue();
                ElementId sourceId = edgeData.edge.getSourceId();
                ElementId destId = edgeData.edge.getDestId();

                LinkedHashMap<ElementId, NodeElementData> nodesMap = nodes.get(sceneId);
                NodeElementData sourceData = nodesMap.get(sourceId);
                NodeElementData destData = nodesMap.get(destId);

                builder.append("    " + sourceId.getId()).append(" -> ").append(destId.getId());
                if(sourceData.isVisited && destData.isVisited) {
                    builder.append("[").append("color=green").append("]");
                }
                // окрашивается стрелочка к текущему состоянию
                if(sceneId.equals(currentSceneId) && sourceId.equals(prevNodeId) && destId.equals(currentNodeId)) {
                    builder.append("[").append("color=green").append("]");
                }
                builder.append(";\n");
            }
        }

        builder.append("}");
        return builder.toString();
    }

    public static class NodeElementData {
        public final ElementId nodeId;
        public final boolean isLink;
        public boolean isBegin;
        public boolean isVisited;
        public boolean isErrorNode;
        public String errorMessage;

        public NodeElementData(ElementId nodeId, boolean isLink) {
            this.nodeId = nodeId;
            this.isLink = isLink;
        }
    }

    public static class EdgeElementData {
        public final ElementId nodeId;
        public final Edge<?> edge;

        public EdgeElementData(ElementId edgeId, Edge<?> edge) {
            this.nodeId = edgeId;
            this.edge = edge;
        }
    }

}
