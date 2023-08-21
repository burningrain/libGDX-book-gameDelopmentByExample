package com.github.br.gdx.simple.visual.novel.api.scene;

import com.github.br.gdx.simple.visual.novel.Utils;
import com.github.br.gdx.simple.visual.novel.api.ElementId;
import com.github.br.gdx.simple.visual.novel.api.context.AuxiliaryContext;
import com.github.br.gdx.simple.visual.novel.api.context.CurrentState;
import com.github.br.gdx.simple.visual.novel.api.context.PlotContext;
import com.github.br.gdx.simple.visual.novel.api.context.UserContext;
import com.github.br.gdx.simple.visual.novel.api.node.Node;
import com.github.br.gdx.simple.visual.novel.api.node.NodeResult;
import com.github.br.gdx.simple.visual.novel.api.node.NodeResultType;
import com.github.br.gdx.simple.visual.novel.api.screen.ScreenManager;
import com.github.br.gdx.simple.visual.novel.graph.EdgeWrapper;
import com.github.br.gdx.simple.visual.novel.graph.Graph;
import com.github.br.gdx.simple.visual.novel.graph.GraphElementId;
import com.github.br.gdx.simple.visual.novel.graph.NodeWrapper;

import java.util.ArrayList;
import java.util.List;

public class Scene<UC extends UserContext, SM extends ScreenManager> {

    private final SceneConfig<SM> config;
    private final Graph<Node<UC, SM>, Edge> graph;

    private final GraphElementId beginNodeId;

    public Scene(SceneConfig<SM> config, Graph<Node<UC, SM>, Edge> graph, GraphElementId beginNodeId) {
        this.config = Utils.checkNotNull(config, "config");
        this.graph = Utils.checkNotNull(graph, "graph");
        this.beginNodeId = Utils.checkNotNull(beginNodeId, "beginNodeId");
    }

    public SceneConfig<SM> getConfig() {
        return config;
    }

    @Override
    public String toString() {
        return "Scene{" +
                "config=" + config + "\n" +
                ", graph=" + graph + "\n" +
                ", beginNodeId=" + beginNodeId + "\n" +
                '}';
    }


    public static <UC extends UserContext, SM extends ScreenManager> SceneBuilder<UC, SM> builder(SceneConfig<SM> config) {
        Utils.checkNotNull(config, "config");

        return new SceneBuilder<>(config);
    }

    public NodeResult execute(PlotContext<UC, SM> plotContext) {
        plotContext.getServiceContext().setCurrentScreenManager(this.config.getScreenManager());

        AuxiliaryContext auxiliaryContext = plotContext.getAuxiliaryContext();
        CurrentState currentState = auxiliaryContext.currentState;
        if (currentState.nodeId == null) {
            currentState.nodeId = SceneUtils.toId(beginNodeId);
        }

        GraphElementId graphElementId = SceneUtils.toId(currentState.nodeId);
        Node<UC, SM> node = graph.getNode(graphElementId);
        NodeResult nodeResult = node.execute(plotContext, auxiliaryContext.isVisited(currentState.sceneId, currentState.nodeId));
        auxiliaryContext.addToVisited(currentState.sceneId, currentState.nodeId);
        if (NodeResultType.NEXT != nodeResult.getType()) {
            return nodeResult;
        }

        // variant 'NEXT' only
//        ElementId nextId = nodeResult.getNextId();
//        if (nextId != null) {
//            if(graph.containsNode(SceneUtils.toId(nextId))) {
//                // 1) в пределах данной сцены/графа делаем прыжок либо по выбору из нескольких, либо телепортация вперед-назад
//                currentState.nodeId = nextId;
//            } else {
//                // выходим из текущего процесса в родительский и сразу имеем несколько вариантов на выбор
//
//            }
//            // TODO а если мы телепортировались на ноду ,которая сцена?!
//        } else {
        // 2) просто идем дальше, шаг за шагом, без хитростей
        ElementId nextStepNodeId = getNextNodeId(graphElementId, plotContext);
        GraphElementId nextNodeId = (nextStepNodeId != null) ? SceneUtils.toId(nextStepNodeId) : null;
        if (nextNodeId == null) {
            // дошли до конца текущей сцены и вообще всего процесса
            if (currentState.parentState == null) {
                currentState.nodeId = null;
            } else {
                // дошли до конца текущего сценария, делаем прыжок вверх. Переключает Plot.class
                return new NodeResult(NodeResultType.CHANGE_SCENE_OUT);
            }
        } else {
            currentState.nodeId = SceneUtils.toId(nextNodeId);
        }
        //}

        return nodeResult;
    }

    public ElementId getNextNodeId(GraphElementId graphElementId, PlotContext<UC, SM> plotContext) {
        GraphElementId nextNodeId = getGraphNextNodeId(graphElementId, plotContext);
        if (nextNodeId == null) {
            return null;
        }

        return SceneUtils.toId(nextNodeId);
    }

    @SuppressWarnings("unchecked")
    public GraphElementId getGraphNextNodeId(GraphElementId currentNodeId, PlotContext<UC, SM> plotContext) {
        NodeWrapper<Node<UC, SM>, Edge> nodeWrapper = Utils.checkNotNull(graph.getNodeWrapper(currentNodeId), "nodeId='" + currentNodeId + "'");
        List<EdgeWrapper<Node<UC, SM>, Edge>> edges = nodeWrapper.getEdges();

        NodeWrapper<Node<UC, SM>, Edge> defaultTransition = null;
        ArrayList<NodeWrapper<Node<UC, SM>, Edge>> children = new ArrayList<>(edges.size());
        for (EdgeWrapper<Node<UC, SM>, Edge> edge : edges) {
            NodeWrapper<Node<UC, SM>, Edge> source = edge.getSource();
            if (nodeWrapper.equals(source)) {
                Edge e = edge.getEdge();
                if (e.isTransitionAvailable(plotContext)) {
                    if (defaultTransition == null && e.isEmptyPredicate()) {
                        defaultTransition = edge.getDest();
                    } else {
                        children.add(edge.getDest());
                    }
                }
            }
        }

        if (children.size() == 0) {
            // случай, когда мы пришли к концу и дальше идти некуда
            if (defaultTransition == null) {
                return null;
            } else {
                // просто берем следующую ноду в цепочке
                return defaultTransition.getId();
            }
        } else if (children.size() == 1) {
            // по приоритету над дефолтовым переходом берем ноду с выполненным условием перехода
            return children.get(0).getId();
        }

        // нельзя просто перейти на следующую ноду, ибо их несколько
        throw new IllegalStateException("current nodeId=[" + currentNodeId + "] You must choose the next node by nodeId. " + children.size() + " nodes are available.");
    }

}