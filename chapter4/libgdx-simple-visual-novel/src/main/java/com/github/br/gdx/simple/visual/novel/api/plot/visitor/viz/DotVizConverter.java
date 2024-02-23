package com.github.br.gdx.simple.visual.novel.api.plot.visitor.viz;

import com.github.br.gdx.simple.visual.novel.api.ElementId;
import com.github.br.gdx.simple.visual.novel.api.context.CurrentState;
import com.github.br.gdx.simple.visual.novel.api.node.Node;
import com.github.br.gdx.simple.visual.novel.api.plot.visitor.viz.data.NodeElementType;
import com.github.br.gdx.simple.visual.novel.api.plot.visitor.viz.data.NodeElementVizData;
import com.github.br.gdx.simple.visual.novel.api.plot.visitor.viz.data.NodeElementVizDataFactory;
import com.github.br.gdx.simple.visual.novel.api.scene.Edge;
import com.github.br.gdx.simple.visual.novel.inner.SceneLinkNode;
import com.github.br.gdx.simple.visual.novel.utils.NullObjects;

import java.util.*;

public class DotVizConverter implements VizConverter {

    private final NodeElementVizDataFactory nodeElementVizFactory;

    public DotVizConverter(NodeElementVizDataFactory nodeElementVizFactory) {
        this.nodeElementVizFactory = nodeElementVizFactory;
    }

    @Override
    public String convert(PLotViz<?> pLotViz, DotVizSettings settings) {
        StringBuilder builder = new StringBuilder();
        builder.append("digraph G {\n")
                .append("charset=\"UTF-8\"").append("\n");
        builder.append("rankdir=").append(settings.getRankDirType()).append("\n");

        if (settings.isShowLegend()) {
            builder.append(createLegend()).append("\n");
        }

        Map<ElementId, ? extends SceneViz<?>> scenes = pLotViz.getScenes();
        ElementId beginSceneId = pLotViz.getBeginSceneId();
        Set<String> nodePaths = convertToNodePaths(pLotViz.getPath());

        builder.append("subgraph cluster_plot {").append("\n");
        printScene(settings, builder, "MAIN", beginSceneId, scenes, nodePaths, "");
        builder.append("}").append("\n");

        builder.append("}");
        return builder.toString();
    }

    private String createLegend() {
        StringBuilder result = new StringBuilder();
        result
                .append("subgraph cluster_legend {").append("\n")
                .append("label = \"Legend\";").append("\n")
                .append("e1 [label=\"\", shape=").append(NodeElementType.SIMPLE_NODE.getDotShape()).append("]").append("\n")
                .append("d1 [label=").append(NodeElementType.SIMPLE_NODE.name()).append(", shape=plaintext]").append("\n")
                .append("e2 [label=\"\", shape=").append(NodeElementType.SCENE_LINK.getDotShape()).append("]").append("\n")
                .append("d2 [label=").append(NodeElementType.SCENE_LINK.name()).append(", shape=plaintext] ").append("\n")
                .append("e3 [label=\"\", shape=").append(NodeElementType.COMPOSITE_NODE.getDotShape()).append("]").append("\n")
                .append("d3 [label=").append(NodeElementType.COMPOSITE_NODE.name()).append(", shape=plaintext] ").append("\n")

                .append("subgraph cluster_description {").append("\n")
                .append("label = \"description\";").append("\n")
                .append("style=invisible;").append("\n")
                .append("edge [style=invisible,dir=none];").append("\n")
                .append("node [style=filled,color=white];").append("\n")
                .append("d1 -> d2 -> d3;").append("\n")
                .append("}").append("\n")

                .append("subgraph cluster_element {").append("\n")
                .append("label = \"element\";").append("\n")
                .append("style=invisible;").append("\n")
                .append("edge [style=invisible,dir=none];").append("\n")
                .append("node [style=filled,color=white];").append("\n")
                .append("e1 -> e2 -> e3;").append("\n")
                .append("}").append("\n")

                .append("edge[style=invisible, dir=none, constraint=false];").append("\n")
                .append("e1 -> d1;").append("\n")
                .append("e2 -> d2;").append("\n")
                .append("e3 -> d3;").append("\n")

                .append("}");

        return result.toString();
    }

    private Set<String> convertToNodePaths(List<CurrentState> path) {
        if (path == null || path.isEmpty()) {
            return Collections.emptySet();
        }

        LinkedHashSet<String> result = new LinkedHashSet<>();
        ArrayList<CurrentState> rootsStack = new ArrayList<>();

        CurrentState prev = path.get(0);
        result.add(stateToString(rootsStack, prev));
        for (int i = 1; i < path.size(); i++) {
            CurrentState currentState = path.get(i);

            // надо разобраться, спуск или подъем происходит
            if (NullObjects.DOWN_INTO_SUB_PROCESS.equals(currentState)) {
                // провал в подпроцесс внутри подпроцесса
                rootsStack.add(prev);
            } else if (NullObjects.UP_TO_PARENT_PROCESS.equals(currentState)) {
                // подъем наверх, выход из подпроцесса
                rootsStack.remove(rootsStack.size() - 1);
            } else {
                // 1) только поднялись или опустились вглубь. Спокойно идем дальше.
                // или
                // 2) остались на том же уровне
                result.add(stateToString(rootsStack, currentState));
            }

            prev = currentState;
        }
        return result;
    }

    private String stateToString(List<CurrentState> rootsStack, CurrentState state) {
        StringBuilder result = new StringBuilder();
        for (CurrentState currentState : rootsStack) {
            result.append(currentState.sceneId).append(".").append(currentState.nodeId);
            result.append("/");
        }

        result.append(state.sceneId).append(".").append(state.nodeId);
        return result.toString();
    }

    private void printScene(
            DotVizSettings settings,
            StringBuilder result,
            String label,
            ElementId sceneId,
            Map<ElementId, ? extends SceneViz<?>> scenes,
            Set<String> nodePaths,
            String currentPath
    ) {
        SceneViz<?> sceneViz = scenes.get(sceneId);
        LinkedHashMap<ElementId, NodeElementVizData> nodes = sceneViz.getNodes();

        ArrayList<NodeElementVizData> sceneLinks = new ArrayList<>();
        result.append("subgraph cluster_").append(sceneId).append("_").append(label).append(" {\n");
        result.append("label=").append("\"").append(label).append("(").append(sceneId).append(")\"\n");

        String parentPath = "".equals(currentPath) ? currentPath : currentPath + "/";

        // отрисовка нод
        HashSet<ElementId> visitedNodes = new HashSet<>();
        for (Map.Entry<ElementId, NodeElementVizData> nodeEntry : nodes.entrySet()) {
            NodeElementVizData value = nodeEntry.getValue();
            if (isSceneLink(value.getNode())) {
                sceneLinks.add(value);
            }

            String cn = sceneId.getId() + "." + value.getNodeId().getId();
            boolean isNodeVisited = nodePaths.contains(parentPath + cn);
            if (isNodeVisited) {
                visitedNodes.add(value.getNodeId());
            }
            result.append(createNode(settings, label, value, isNodeVisited));
        }
        // отрисовка ребер
        LinkedHashMap<ElementId, Edge<?>> edges = sceneViz.getEdges();
        for (Map.Entry<ElementId, Edge<?>> edgeEntry : edges.entrySet()) {
            Edge<?> edge = edgeEntry.getValue();
            result.append(label).append("_").append(edge.getSourceId().getId())
                    .append(" -> ")
                    .append(label).append("_").append(edge.getDestId().getId());

            if (visitedNodes.contains(edge.getSourceId()) && visitedNodes.contains(edge.getDestId())) {
                result.append("[").append("color=green").append("]");
            }
            result.append(";\n");
        }

        result.append("\n}\n");

        // отрисовка вложенных подсценариев
        for (NodeElementVizData sceneLink : sceneLinks) {
            String cp = parentPath + sceneId.getId() + "." + sceneLink.getNodeId().getId();
            ElementId sceneLinkId = extractSceneLinkId(sceneLink.getNode());
            printScene(settings, result, sceneLink.getNodeId().getId(), sceneLinkId, scenes, nodePaths, cp);
            // создаем связь к подсценарию
            String parentNodeLabel = sceneLink.getNodeId().getId();
            SceneViz<?> subScene = scenes.get(sceneLinkId);
            String subSceneBeginNodeId = parentNodeLabel + "_" + subScene.getBeginNodeId().getId();
            result.append(label).append("_").append(parentNodeLabel)
                    .append(" -> ")
                    .append(subSceneBeginNodeId)
                    .append("[")
                    .append("style=dashed, color=gray")
                    .append("]")
                    .append(";\n");
        }
    }

    private String createNode(DotVizSettings settings, String label, NodeElementVizData value, boolean isVisited) {
        switch (settings.getNodeInfoType()) {
            case SHORT:
                return createShortNodeInfo(label, value, isVisited);
            case FULL:
                return createFullNodeInfo(label, value, isVisited);
            default:
                throw new IllegalArgumentException("NodeInfoType=[" + settings.getNodeInfoType() + "] is not defined");
        }
    }

    private String createFullNodeInfo(String label, NodeElementVizData value, boolean isVisited) {
        StringBuilder builder = new StringBuilder();
        String nodeId = createNodeId(label, value);
        builder
                .append(nodeId)
                .append(" [\n")
                .append("label=")
                .append(createLabelFullNodeInfo(nodeId, value))
                .append("\n")
                .append("shape=").append("plaintext").append("\n");

        if (isVisited) {
            builder.append("color=green").append("\n");
        }

        builder.append("];\n");
        return builder.toString();
    }

    private String createLabelFullNodeInfo(String nodeId, NodeElementVizData value) {
        return nodeElementVizFactory.createLabelFullNodeInfo(nodeId, value.getNode());
    }

    private String createShortNodeInfo(String label, NodeElementVizData value, boolean isVisited) {
        StringBuilder builder = new StringBuilder();
        String nodeId = createNodeId(label, value);
        builder
                .append(nodeId)
                .append(" [\n")
                .append("label=\"")
                .append(value.getNodeId().getId())
                .append("\"").append("\n")
                .append("shape=").append(getNodeShape(value.getNode())).append("\n");

        if (isVisited) {
            builder.append("color=green").append("\n");
        }

        builder.append("];\n");
        return builder.toString();
    }

    private String createNodeId(String label, NodeElementVizData value) {
        return label + "_" + value.getNodeId().getId();
    }

    private String getNodeShape(Node<?, ?> node) {
        return nodeElementVizFactory.getNodeShapeForShortInfo(node);
    }

    private boolean isSceneLink(Node<?, ?> node) {
        return node instanceof SceneLinkNode;
    }

    private ElementId extractSceneLinkId(Node<?, ?> node) {
        return ((SceneLinkNode<?,?>)node).getSceneTitle();
    }

}
