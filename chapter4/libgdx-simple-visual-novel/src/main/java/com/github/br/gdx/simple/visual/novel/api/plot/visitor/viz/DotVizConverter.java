package com.github.br.gdx.simple.visual.novel.api.plot.visitor.viz;

import com.github.br.gdx.simple.visual.novel.api.ElementId;
import com.github.br.gdx.simple.visual.novel.api.plot.visitor.viz.data.NodeElementType;
import com.github.br.gdx.simple.visual.novel.api.plot.visitor.viz.data.NodeElementVizData;
import com.github.br.gdx.simple.visual.novel.api.scene.Edge;

import java.util.*;

public class DotVizConverter implements VizConverter {

    @Override
    public String convert(PLotViz<?> pLotViz) {
        StringBuilder builder = new StringBuilder();
        builder.append("digraph G {\n")
                .append("charset=\"UTF-8\"").append("\n");

        Map<ElementId, ? extends SceneViz<?>> scenes = pLotViz.getScenes();
        ElementId beginSceneId = pLotViz.getBeginSceneId();
        SceneViz<?> mainScene = scenes.get(pLotViz.getBeginSceneId());
        ElementId beginNodeId = mainScene.getBeginNodeId();

        printScene(builder, "MAIN", beginSceneId, scenes);


        builder.append("}");
        return builder.toString();
    }

    private void printScene(StringBuilder result, String label, ElementId sceneId, Map<ElementId, ? extends SceneViz<?>> scenes) {
        SceneViz<?> sceneViz = scenes.get(sceneId);
        LinkedHashMap<ElementId, NodeElementVizData> nodes = sceneViz.getNodes();

        ArrayList<NodeElementVizData> sceneLinks = new ArrayList<>();
        result.append("subgraph cluster_").append(sceneId).append("_").append(label).append(" {\n");
        result.append("label=").append("\"").append(label).append(" (").append(sceneId).append(")\"\n");
        for (Map.Entry<ElementId, NodeElementVizData> nodeEntry : nodes.entrySet()) {
            NodeElementVizData value = nodeEntry.getValue();
            if (NodeElementType.SCENE_LINK == value.type) {
                sceneLinks.add(value);
            }
            result.append(createNode(label, value));
        }
        LinkedHashMap<ElementId, Edge<?>> edges = sceneViz.getEdges();
        for (Map.Entry<ElementId, Edge<?>> edgeEntry : edges.entrySet()) {
            Edge<?> edge = edgeEntry.getValue();
            result.append(label).append("_").append(edge.getSourceId().getId())
                    .append(" -> ")
                    .append(label).append("_").append(edge.getDestId().getId())
                    .append(";\n");
        }

        result.append("\n}\n");

        // отрисовка вложенных подсценариев
        for (NodeElementVizData sceneLink : sceneLinks) {
            printScene(result, sceneLink.nodeId.getId(), sceneLink.sceneLinkId, scenes);
            // создаем связь к подсценарию
            String parentNodeLabel = sceneLink.nodeId.getId();
            SceneViz<?> subScene = scenes.get(sceneLink.sceneLinkId);
            String subSceneBeginNodeId = parentNodeLabel + "_" + subScene.getBeginNodeId().getId();
            result.append(label).append("_").append(parentNodeLabel)
                    .append(" -> ")
                    .append(subSceneBeginNodeId)
                    .append(";\n");
        }
    }

    private String createNode(String label, NodeElementVizData value) {
        StringBuilder builder = new StringBuilder();

        String nodeId = label + "_" + value.nodeId.getId();
        builder
                .append(nodeId)
                .append(" [ ")
                .append("label=\"").append(value.nodeId.getId()).append("\"").append("\n")
                .append("shape=").append(getNodeShape(value.type)).append("\n")
                .append("];\n");

        return builder.toString();
    }

    private String getNodeShape(NodeElementType type) {
        switch (type) {
            case SIMPLE_NODE:
                return "circle";
            case SCENE_LINK:
                return "doubleoctagon";
            case COMPOSITE_NODE:
                return "Mcircle";
            default:
                throw new IllegalArgumentException("type=[" + type + "] is not defined");
        }
    }


//    private String createSubgraph(PLotViz<?> pLotViz,
//                                  ElementId sceneId,
//                                  String sceneLabel,
//                                  SceneViz<?> sceneViz) {
//        StringBuilder builder = new StringBuilder();
//
//        LinkedHashMap<ElementId, NodeElementVizData> subScenes = new LinkedHashMap<>();
//        builder.append("    subgraph ").append("cluster_").append(sceneId.getId()).append(" {\n");
//        builder.append("        label =").append("\"").append(sceneLabel).append("\";").append("\n");
//        for (Map.Entry<ElementId, NodeElementVizData> nodeEntry : sceneViz.nodes.entrySet()) {
//            ElementId key = nodeEntry.getKey();
//            NodeElementVizData value = nodeEntry.getValue();
//            builder.append(createNode(key, value));
//
//            if(value.isLink) {
//                subScenes.put(key, value);
//            }
//        }
//
//        builder.append("    }").append("\n");
//
//        // отрисовка остальных нод
//        for (Map.Entry<ElementId, NodeElementVizData> subScenesNodeEntry : subScenes.entrySet()) {
//            NodeElementVizData value = subScenesNodeEntry.getValue();
//            ElementId sceneLinkId = Utils.checkNotNull(value.sceneLinkId, "value.sceneLinkId");
//            String title = subScenesNodeEntry.getKey() + "(" + sceneLinkId.getId() + ")";
//            createSubgraph(pLotViz, false, subScenesNodeEntry.getKey(), title, pLotViz.getScenes().get(sceneLinkId));
//        }
//
//        return builder.toString();
//    }
//
//    private String createNode(ElementId elementId, NodeElementVizData data) {
//        StringBuilder builder = new StringBuilder();
//        builder.append("        ").append(elementId);
//        builder.append("[style=filled");
//
//        if (value.isErrorNode) {
//            builder.append(", color=red");
//        } else if (value.isVisited) {
//            builder.append(", color=green");
//        }
//
//        String shape;
//        if (data.isLink) {
//            shape = ", shape=box";
//        } else {
//            shape = ", shape=circle";
//        }
//        builder.append(shape);
//        if (value.errorMessage != null) {
//            builder.append(", label=\"").append(value.errorMessage).append("\"");
//        }
//
//        builder.append("];\n");
//
//        return builder.toString();
//    }
//
//    private void printEdge(StringBuilder builder, ElementId sceneId, Map.Entry<ElementId, EdgeElementVizData> edgeEntry) {
//        EdgeElementVizData edgeData = edgeEntry.getValue();
//        ElementId sourceId = edgeData.edge.getSourceId();
//        ElementId destId = edgeData.edge.getDestId();
//
//        LinkedHashMap<ElementId, NodeElementVizData> nodesMap = nodes.get(sceneId);
//        NodeElementVizData sourceData = nodesMap.get(sourceId);
//        NodeElementVizData destData = nodesMap.get(destId);
//
//        builder.append("    " + sourceId.getId()).append(" -> ").append(destId.getId());
//        if (sourceData.isVisited && destData.isVisited) {
//            builder.append("[").append("color=green").append("]");
//        }
//        // окрашивается стрелочка к текущему состоянию
//        if (sceneId.equals(currentSceneId) && sourceId.equals(prevNodeId) && destId.equals(currentNodeId)) {
//            builder.append("[").append("color=green").append("]");
//        }
//        builder.append(";\n");
//    }

}
