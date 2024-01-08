package com.github.br.gdx.simple.visual.novel.api.plot.visitor.viz;

import com.github.br.gdx.simple.visual.novel.api.ElementId;
import com.github.br.gdx.simple.visual.novel.api.context.CurrentState;
import com.github.br.gdx.simple.visual.novel.api.plot.visitor.viz.data.NodeElementType;
import com.github.br.gdx.simple.visual.novel.api.plot.visitor.viz.data.NodeElementVizData;
import com.github.br.gdx.simple.visual.novel.api.scene.Edge;
import com.github.br.gdx.simple.visual.novel.utils.NullObjects;

import java.util.*;

public class DotVizConverter implements VizConverter {

    @Override
    public String convert(PLotViz<?> pLotViz) {
        StringBuilder builder = new StringBuilder();
        builder.append("digraph G {\n")
                .append("charset=\"UTF-8\"").append("\n");

        Map<ElementId, ? extends SceneViz<?>> scenes = pLotViz.getScenes();
        ElementId beginSceneId = pLotViz.getBeginSceneId();
        Set<String> nodePaths = convertToNodePaths(pLotViz.getPath());

        printScene(builder, "MAIN", beginSceneId, scenes, nodePaths, "");


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
        result.append("label=").append("\"").append(label).append(" (").append(sceneId).append(")\"\n");

        String parentPath = "".equals(currentPath) ? currentPath : currentPath + "/";

        // отрисовка нод
        HashSet<ElementId> visitedNodes = new HashSet<>();
        for (Map.Entry<ElementId, NodeElementVizData> nodeEntry : nodes.entrySet()) {
            NodeElementVizData value = nodeEntry.getValue();
            if (NodeElementType.SCENE_LINK == value.type) {
                sceneLinks.add(value);
            }

            String cn = sceneId.getId() + "." + value.nodeId.getId();
            boolean isNodeVisited = nodePaths.contains(parentPath + cn);
            if (isNodeVisited) {
                visitedNodes.add(value.nodeId);
            }
            result.append(createNode(label, value, isNodeVisited));
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
            String cp = parentPath + sceneId.getId() + "." + sceneLink.nodeId.getId();
            printScene(result, sceneLink.nodeId.getId(), sceneLink.sceneLinkId, scenes, nodePaths, cp);
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

    private String createNode(String label, NodeElementVizData value, boolean isVisited) {
        StringBuilder builder = new StringBuilder();

        String nodeId = label + "_" + value.nodeId.getId();
        builder
                .append(nodeId)
                .append(" [\n")
                .append("label=\"").append(value.nodeId.getId()).append("\"").append("\n")
                .append("shape=").append(getNodeShape(value.type)).append("\n");

        if (isVisited) {
            builder.append("color=green").append("\n");
        }

        builder.append("];\n");

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

}
