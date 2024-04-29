package com.github.br.gdx.simple.visual.novel.api.scene;

import com.github.br.gdx.simple.visual.novel.utils.NullObjects;
import com.github.br.gdx.simple.visual.novel.utils.Utils;
import com.github.br.gdx.simple.visual.novel.api.ElementId;
import com.github.br.gdx.simple.visual.novel.api.context.AuxiliaryContext;
import com.github.br.gdx.simple.visual.novel.api.context.CurrentState;
import com.github.br.gdx.simple.visual.novel.api.context.PlotContext;
import com.github.br.gdx.simple.visual.novel.api.context.UserContext;
import com.github.br.gdx.simple.visual.novel.api.node.*;
import com.github.br.gdx.simple.visual.novel.api.plot.visitor.PlotVisitor;
import com.github.br.gdx.simple.visual.novel.inner.graph.EdgeWrapper;
import com.github.br.gdx.simple.visual.novel.inner.graph.Graph;
import com.github.br.gdx.simple.visual.novel.inner.graph.GraphElementId;
import com.github.br.gdx.simple.visual.novel.inner.graph.NodeWrapper;
import com.github.br.gdx.simple.visual.novel.inner.GraphVisitor;

import java.util.ArrayList;
import java.util.List;

public class Scene<UC extends UserContext, V extends NodeVisitor<?>> {

    private final SceneConfig config;
    private final Graph<Node<UC, V>, Edge> graph;

    private final GraphElementId beginNodeId;

    public Scene(SceneConfig config, Graph<Node<UC, V>, Edge> graph, GraphElementId beginNodeId) {
        this.config = Utils.checkNotNull(config, "config");
        this.graph = Utils.checkNotNull(graph, "graph");
        this.beginNodeId = Utils.checkNotNull(beginNodeId, "beginNodeId");
    }

    public SceneConfig getConfig() {
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


    public static <UC extends UserContext, V extends NodeVisitor<?>> SceneBuilder<UC, V> builder(SceneConfig config) {
        Utils.checkNotNull(config, "config");

        return new SceneBuilder<>(config);
    }

    public SceneResult execute(PlotContext<?, UC> plotContext) {
        AuxiliaryContext auxiliaryContext = plotContext.getAuxiliaryContext();
        CurrentState currentState = auxiliaryContext.stateStack.peek();
        if (currentState.nodeId == null) {
            currentState.nodeId = SceneUtils.toId(beginNodeId);
        }

        GraphElementId graphElementId = SceneUtils.toId(currentState.nodeId);
        NodeWrapper<Node<UC, V>, Edge> nodeWrapper = graph.getNodeWrapper(graphElementId);
        Node<UC, V> node = nodeWrapper.getNode();

        NodeResult nodeResult = node.execute(plotContext, auxiliaryContext.isVisited(currentState.sceneId, currentState.nodeId));

        if (NodeResultType.STAY != nodeResult.getType()) {
            // сохраняем ноду как посещенную только когда будет переключение. Если переходит сама в себя - не отмечаем как посещенную
            auxiliaryContext.addToVisited(currentState);
        }
        if (NodeResultType.NEXT != nodeResult.getType()) {
            // STAY/IN - берется тип текущей ноды. OUT здесь не будет
            return new SceneResult(nodeResult, nodeWrapper.getNodeType());
        }

        ElementId nextStepNodeId = getNextNodeId(graphElementId, plotContext);
        GraphElementId nextNodeId = (!NullObjects.THIS_IS_END_ELEMENT_IN_THE_SCENE.equals(nextStepNodeId)) ? SceneUtils.toId(nextStepNodeId) : null;
        if (nextNodeId == null) {
            // дошли до конца текущей сцены и вообще всего процесса
            CurrentState parentState = auxiliaryContext.stateStack.peekParent();
            if (parentState == null) {
                currentState.nodeId = NullObjects.THIS_IS_END_ELEMENT_IN_THE_SCENE;
            } else {
                // дошли до конца текущего сценария, делаем прыжок вверх. Переключает Plot.class
                return new SceneResult(new NodeResult(NodeResultType.CHANGE_SCENE_OUT), null);
            }
        } else {
             // просто идем дальше по процессу
            currentState.nodeId = SceneUtils.toId(nextNodeId);
        }

        NodeType nodeType = null;
        if(nextNodeId != null) {
            nodeType = graph.getNodeWrapper(nextNodeId).getNodeType();
        }
        return new SceneResult(nodeResult, nodeType);
    }

    public ElementId getNextNodeId(GraphElementId graphElementId, PlotContext<?, UC> plotContext) {
        GraphElementId nextNodeId = getGraphNextNodeId(graphElementId, plotContext);
        if (nextNodeId == null) {
            return NullObjects.THIS_IS_END_ELEMENT_IN_THE_SCENE;
        }

        return SceneUtils.toId(nextNodeId);
    }

    @SuppressWarnings("unchecked")
    public GraphElementId getGraphNextNodeId(GraphElementId currentNodeId, PlotContext<?, UC> plotContext) {
        NodeWrapper<Node<UC, V>, Edge> nodeWrapper = Utils.checkNotNull(graph.getNodeWrapper(currentNodeId), "nodeId='" + currentNodeId + "'");
        List<EdgeWrapper<Node<UC, V>, Edge>> edges = nodeWrapper.getEdges();

        NodeWrapper<Node<UC, V>, Edge> defaultTransition = null;
        ArrayList<NodeWrapper<Node<UC, V>, Edge>> children = new ArrayList<>(edges.size());
        for (EdgeWrapper<Node<UC, V>, Edge> edge : edges) {
            NodeWrapper<Node<UC, V>, Edge> source = edge.getSource();
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

        // нельзя просто перейти на следующую ноду, ибо их несколько подходящих
        throw new IllegalStateException("current nodeId=[" + currentNodeId + "] You must choose the next node by nodeId. [" + children.size() + "] nodes are available.");
    }

    public void accept(final ElementId sceneId, final PlotVisitor plotVisitor) {
        graph.accept(new GraphVisitor<Node<UC, V>, Edge>() {
            @Override
            public void visitNode(GraphElementId nodeId, Node<UC, V> node, NodeType nodeType) {
                plotVisitor.visitNode(sceneId, SceneUtils.toId(nodeId), node, nodeType);
            }

            @Override
            public void visitEdge(GraphElementId edgeId, Edge edge) {
                plotVisitor.visitEdge(sceneId, SceneUtils.toId(edgeId), edge);
            }
        });
        plotVisitor.visitBeginNodeId(sceneId, SceneUtils.toId(beginNodeId));
        //todo добавить конечные ноды процесса
    }

}
