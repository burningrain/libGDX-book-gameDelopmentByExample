package com.github.br.gdx.simple.visual.novel.viz;

import com.github.br.gdx.simple.visual.novel.api.ElementId;
import com.github.br.gdx.simple.visual.novel.api.context.CurrentState;
import com.github.br.gdx.simple.visual.novel.viz.data.NodeElementType;
import com.github.br.gdx.simple.visual.novel.viz.data.NodeElementTypeId;
import com.github.br.gdx.simple.visual.novel.viz.data.NodeElementVizData;
import com.github.br.gdx.simple.visual.novel.viz.settings.DotVizSettings;
import com.github.br.gdx.simple.visual.novel.viz.settings.color.NodeColorsSchema;
import com.github.br.gdx.simple.visual.novel.viz.settings.painter.GraphvizShape;
import com.github.br.gdx.simple.visual.novel.api.scene.Edge;
import com.github.br.gdx.simple.visual.novel.utils.NullObjects;

import java.util.*;

public class DotVizConverter implements VizConverter {


    @Override
    public String convert(PLotViz<?> pLotViz, DotVizSettings settings) {
        StringBuilder builder = new StringBuilder();
        builder.append("digraph G {\n")
                .append("charset=\"UTF-8\"").append("\n");
        builder.append("rankdir=").append(settings.getRankDirType()).append("\n");
        builder.append("graph [pad=\".75\", ranksep=\"0.25\", nodesep=\"0.25\"];").append("\n");

        if (settings.isShowLegend()) {
            String legend = settings.getCurrentDotVizModePainter().createLegend(settings);
            builder.append(legend).append("\n");
        }

        Map<ElementId, ? extends SceneViz<?>> scenes = pLotViz.getScenes();
        ElementId beginSceneId = pLotViz.getBeginSceneId();
        Set<String> nodePaths = convertToNodePaths(pLotViz.getPath());
        CurrentState exceptionNode = (pLotViz.getException() != null)? pLotViz.getCurrentNodeId() : null;
        String exceptionMessage = (pLotViz.getException() != null)? pLotViz.getException().getMessage() : null;

        NodeColorsSchema colorSchema = settings.getColorSchema();
        builder.append("subgraph cluster_plot {").append("\n");
        builder.append("color=").append(colorSchema.getBorderColor()).append("\n");

        printScene(settings, builder, "MAIN", beginSceneId, scenes, nodePaths, "", exceptionNode, exceptionMessage);
        builder.append("}").append("\n");
        builder.append("}");
        return builder.toString();
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
            String currentPath,
            CurrentState exceptionNode,
            String exceptionMessage
    ) {
        SceneViz<?> sceneViz = scenes.get(sceneId);
        LinkedHashMap<ElementId, NodeElementVizData> nodes = sceneViz.getNodes();

        ArrayList<NodeElementVizData> sceneLinks = new ArrayList<>();
        result.append("subgraph cluster_").append(sceneId).append("_").append(label).append(" {\n");
        result.append("label=").append("\"").append(label).append("(").append(sceneId).append(")\"\n");
        result.append("color=gray;").append("\n");

        String parentPath = "".equals(currentPath) ? currentPath : currentPath + "/";

        // отрисовка нод
        HashSet<ElementId> visitedNodes = new HashSet<>();
        for (Map.Entry<ElementId, NodeElementVizData> nodeEntry : nodes.entrySet()) {
            NodeElementVizData value = nodeEntry.getValue();
            if (DotUtils.isSceneLink(value.getNode())) {
                sceneLinks.add(value);
            }

            String cn = sceneId.getId() + "." + value.getNodeId().getId();
            boolean isNodeVisited = nodePaths.contains(parentPath + cn);
            if (isNodeVisited) {
                visitedNodes.add(value.getNodeId());
            }
            boolean isExceptionNode = exceptionNode != null &&
                    exceptionNode.sceneId.equals(sceneId) &&
                    exceptionNode.nodeId.equals(value.getNodeId());

            String nodeId = createNodeId(label, value);
            String node = createNode(nodeId, settings, label, value, isNodeVisited, isExceptionNode);
            result.append(node);

            if (isExceptionNode) {
                printErrorMessage(result, exceptionMessage, settings, nodeId);
            }
        }

        NodeColorsSchema colorSchema = settings.getColorSchema();
        // отрисовка ребер
        LinkedHashMap<ElementId, Edge<?>> edges = sceneViz.getEdges();
        for (Map.Entry<ElementId, Edge<?>> edgeEntry : edges.entrySet()) {
            Edge<?> edge = edgeEntry.getValue();
            result.append(label).append("_").append(edge.getSourceId().getId())
                    .append(" -> ")
                    .append(label).append("_").append(edge.getDestId().getId());

            if (visitedNodes.contains(edge.getSourceId()) && visitedNodes.contains(edge.getDestId())) {
                result.append("[").append("color=").append(colorSchema.getVisitedNodesColor()).append(", penwidth = 2").append("]");
            }
            result.append(";\n");
        }

        result.append("\n}\n");

        // отрисовка вложенных подсценариев
        for (NodeElementVizData sceneLink : sceneLinks) {
            String cp = parentPath + sceneId.getId() + "." + sceneLink.getNodeId().getId();
            ElementId sceneLinkId = DotUtils.extractSceneLinkId(sceneLink.getNode());
            printScene(settings, result, sceneLink.getNodeId().getId(), sceneLinkId, scenes, nodePaths, cp, exceptionNode, exceptionMessage);
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

    private void printErrorMessage(StringBuilder builder, String errorMessage, DotVizSettings settings, String nodeId) {
        // создает искусственную ноду с описанием ошибки, просто чтобы отображалась
        NodeColorsSchema colorSchema = settings.getColorSchema();

        // создаем ноду
        String exceptionNodeId = "exception_node";
        builder.append(exceptionNodeId)
                .append("[")
                .append("shape=").append(GraphvizShape.PLAINTEXT).append("\n")
                .append("label=").append("\"")
                .append(errorMessage)
                .append("\"")
                .append("color=").append(colorSchema.getErrorNodeColor())
                .append("\n")
                .append("]")
        ;
        // создаем связь с ошибочной нодой

        // создаем ребро
        builder.append(nodeId).append(" -> ").append(exceptionNodeId);
        builder
                .append("[")
                .append("color=").append(colorSchema.getErrorNodeColor()).append(", penwidth = 2")
                .append(", dir=none, style=dashed")
                .append(", label=")
                    .append("\"")
                        .append("error message")
                    .append("\"\n")
                .append("]")
                .append("\n")
        ;
    }

    private String createNode(String nodeId,
                              DotVizSettings settings,
                              String label,
                              NodeElementVizData value,
                              boolean isVisited,
                              boolean isExceptionNode) {
        NodeColorsSchema colorSchema = settings.getColorSchema();
        ElementTypeDeterminant typeDeterminant = colorSchema.getTypeDeterminant();
        NodeElementTypeId nodeElementTypeId = typeDeterminant.determineType(value.getNode());
        NodeElementType nodeType = colorSchema.getElementsTypes().get(nodeElementTypeId);

        return settings.getCurrentDotVizModePainter().createNodeInfo(settings, nodeType, nodeId, label, value, isVisited, isExceptionNode);
    }

    private String createNodeId(String label, NodeElementVizData value) {
        return createNodeId(label, value.getNodeId().getId());
    }

    private String createNodeId(CurrentState exceptionNode) {
        return createNodeId(exceptionNode.sceneId.getId(), exceptionNode.nodeId.getId());
    }

    private String createNodeId(String sceneId, String nodeId) {
        return sceneId + "_" + nodeId;
    }

}
