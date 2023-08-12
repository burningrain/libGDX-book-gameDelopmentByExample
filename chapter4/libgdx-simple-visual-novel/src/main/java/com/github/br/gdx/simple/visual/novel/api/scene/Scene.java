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
import com.github.br.gdx.simple.visual.novel.graph.Graph;
import com.github.br.gdx.simple.visual.novel.graph.GraphElementId;
import com.github.br.gdx.simple.visual.novel.inner.SceneLinkNode;

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

    public NodeResult execute(float delta, PlotContext<UC, SM> plotContext) {
        plotContext.getServiceContext().setCurrentScreenManager(this.config.getScreenManager());

        AuxiliaryContext auxiliaryContext = plotContext.getAuxiliaryContext();
        CurrentState currentState = auxiliaryContext.currentState;
        if (currentState.nodeId == null) {
            currentState.nodeId = SceneUtils.toId(beginNodeId);
        }

        GraphElementId graphElementId = SceneUtils.toId(currentState.nodeId);
        Node<UC, SM> node = graph.getNode(graphElementId);
        NodeResult nodeResult = node.execute(delta, plotContext, auxiliaryContext.isVisited(currentState.sceneId, currentState.nodeId));
        auxiliaryContext.addToVisited(currentState.sceneId, currentState.nodeId);
        if (NodeResultType.NEXT != nodeResult.getType()) {
            return nodeResult;
        }

        // variant 'NEXT' only
        ElementId nextId = nodeResult.getNextId();
        if (nextId != null) {
            // 1) делаем прыжок либо по выбору из нескольких, либо телепортация вперед-назад
            currentState.nodeId = nextId; // TODO а если мы телепортировались на ноду ,которая сцена?!
        } else {
            // 2) просто идем дальше, шаг за шагом, без хитростей
            GraphElementId nextNodeId = graph.getNextNodeId(graphElementId);
            if (nextNodeId == null) {
                // дошли до конца текущей сцены и вообще всего процесса
                if (currentState.parentState == null) {
                    currentState.nodeId = null;
                } else {
                    // дошли до конца текущего сценария, делаем прыжок вверх. Переключает Plot.class
                    return new NodeResult(nodeResult.getNextId(), NodeResultType.CHANGE_SCENE_OUT);
                }
            } else {
                currentState.nodeId = SceneUtils.toId(nextNodeId);
            }
        }

        return nodeResult;
    }

    public ElementId getNextNodeId(ElementId currentNodeId) {
        GraphElementId nextNodeId = graph.getNextNodeId(SceneUtils.toId(currentNodeId));
        if(nextNodeId == null) {
            return null;
        }

        return SceneUtils.toId(nextNodeId);
    }

}
