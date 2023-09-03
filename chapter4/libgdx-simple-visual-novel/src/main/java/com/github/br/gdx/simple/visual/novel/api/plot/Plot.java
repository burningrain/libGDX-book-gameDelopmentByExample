package com.github.br.gdx.simple.visual.novel.api.plot;

import com.github.br.gdx.simple.visual.novel.Utils;
import com.github.br.gdx.simple.visual.novel.api.ElementId;
import com.github.br.gdx.simple.visual.novel.api.context.*;
import com.github.br.gdx.simple.visual.novel.api.node.NodeResult;
import com.github.br.gdx.simple.visual.novel.api.node.NodeResultType;
import com.github.br.gdx.simple.visual.novel.api.node.NodeType;
import com.github.br.gdx.simple.visual.novel.api.node.NodeVisitor;
import com.github.br.gdx.simple.visual.novel.api.scene.Scene;
import com.github.br.gdx.simple.visual.novel.api.scene.SceneResult;
import com.github.br.gdx.simple.visual.novel.api.scene.SceneUtils;

public class Plot<ID, UC extends UserContext, V extends NodeVisitor<?>> {

    private final SceneManager<UC, V> sceneManager;
    private final PlotContextManager<ID, UC> plotContextManager;
    private final PlotConfig config;

    private final ElementId beginSceneId;

    public Plot(Builder<ID, UC, V> builder) {
        this.sceneManager = Utils.checkNotNull(builder.sceneManager, "sceneManager");
        this.plotContextManager = Utils.checkNotNull(builder.plotContextManager, "plotContextManager");
        this.config = builder.config;

        this.beginSceneId = builder.beginSceneId;
    }

    private void changeCurrentSceneToChild(PlotContext<ID, UC> plotContext, ElementId nextSceneId) {
        Utils.checkNotNull(nextSceneId, "nextSceneId");

        AuxiliaryContext auxiliaryContext = plotContext.getAuxiliaryContext();
        CurrentState currentState = auxiliaryContext.currentState;

        CurrentState parent = new CurrentState();
        parent.parentState = currentState.parentState;
        parent.sceneId = currentState.sceneId;
        parent.nodeId = currentState.nodeId;

        currentState.parentState = parent;
        currentState.sceneId = nextSceneId;
        currentState.nodeId = null;
    }

    private void changeCurrentSceneToParent(PlotContext<ID, UC> plotContext) {
        AuxiliaryContext auxiliaryContext = plotContext.getAuxiliaryContext();
        CurrentState currentState = auxiliaryContext.currentState;

        CurrentState parentState = currentState.parentState;
        currentState.sceneId = parentState.sceneId;
        currentState.nodeId = getNextSceneNodeId(plotContext, parentState.sceneId, parentState.nodeId);
        currentState.parentState = parentState.parentState;
    }

    private ElementId getNextSceneNodeId(PlotContext<ID, UC> plotContext, ElementId nextSceneId, ElementId currentNodeId) {
        Utils.checkNotNull(nextSceneId, "nextSceneId");
        Utils.checkNotNull(currentNodeId, "nodeId");

        Scene<UC, ?> scene = sceneManager.getScene(nextSceneId);
        return scene.getNextNodeId(SceneUtils.toId(currentNodeId), plotContext);
    }

    public static <ID, UC extends UserContext, V extends NodeVisitor<?>> Builder<ID, UC, V> builder(PlotConfig config) {
        Utils.checkNotNull(config, "config");
        return new Builder<>(config);
    }

    public boolean execute(ID plotId) {
        return this.execute(plotId, null);
    }

    // return окончился the plot или еще нет. 'true' если окончился
    public boolean execute(ID plotId, UC userContext) {
        Utils.checkNotNull(plotId, "plotId");
        PlotContext<ID, UC> plotContext = plotContextManager.getPlotContext(plotId);
        if(plotContext == null) {
            // первичная инициализация. Ожидаем, что пользователь все-таки передает какой-либо контекст
            Utils.checkNotNull(userContext, "Aren't you executing the finished plot? Problem: userContext");
            plotContext = createStartPlotContext(plotId, userContext);
        }
        if(userContext != null) {
            plotContext.setUserContext(userContext);
        }

        AuxiliaryContext auxiliaryContext = plotContext.getAuxiliaryContext();
        if (auxiliaryContext.isProcessFinished()) {
            return true;
        }

        SceneResult sceneResult;
        do {
            CurrentState currentState = auxiliaryContext.currentState;
            ElementId sceneId = currentState.sceneId;

            sceneResult = executeSceneStep(plotContext, sceneId);
            if (currentState.nodeId == null && currentState.parentState == null) {
                auxiliaryContext.setProcessFinished(true);
            }
        } while (NodeType.NOT_WAITING == sceneResult.getNodeType() && !auxiliaryContext.isProcessFinished());

        plotContextManager.savePlotContext(plotContext);

        boolean isProcessFinished = auxiliaryContext.isProcessFinished();
        if(isProcessFinished) {
            plotContextManager.handleFinishedPlot(plotContext);
        }
        return isProcessFinished;
    }

    private PlotContext<ID, UC> createStartPlotContext(ID plotId, UC userContext) {
        PlotContext<ID, UC> plotContext = new PlotContext<>(plotId, beginSceneId, this.config.isMarkVisitedNodes());
        plotContext.setUserContext(userContext);

        AuxiliaryContext auxiliaryContext = plotContext.getAuxiliaryContext();
        CurrentState currentState = auxiliaryContext.currentState;
        currentState.sceneId = beginSceneId;

        return plotContext;
    }

    public void accept(PlotVisitor<V> plotVisitor) {
        for (ElementId sceneId : sceneManager.getSceneIds()) {
            Scene<UC, V> scene = sceneManager.getScene(sceneId);
            scene.accept(sceneId, plotVisitor);
        }
        plotVisitor.visitBeginSceneId(this.beginSceneId);
    }

    private SceneResult executeSceneStep(PlotContext<ID, UC> plotContext, ElementId sceneId) {
        Scene<UC, ?> currentScene = sceneManager.getScene(sceneId);
        SceneResult sceneResult = currentScene.execute(plotContext);
        NodeResult nodeResult = sceneResult.getNodeResult();
        NodeResultType type = nodeResult.getType();
        if (NodeResultType.CHANGE_SCENE_IN == type) {
            ElementId nextSceneId = nodeResult.getSceneTitle();
            changeCurrentSceneToChild(plotContext, nextSceneId);
            return executeSceneStep(plotContext, nextSceneId);
        } else if (NodeResultType.CHANGE_SCENE_OUT == type) {
            changeCurrentSceneToParent(plotContext);
        }

        return sceneResult;
    }

    public static class Builder<ID, UC extends UserContext, V extends NodeVisitor<?>> {

        private final PlotConfig config;
        private SceneManager<UC, V> sceneManager;
        private PlotContextManager<ID, UC> plotContextManager = new ThreadLocalPlotContextManagerImpl<>();
        private ElementId beginSceneId;


        public Builder(PlotConfig config) {
            this.config = Utils.checkNotNull(config, "config");
        }

        public Builder<ID, UC, V> setSceneManager(SceneManager<UC, V> sceneManager) {
            this.sceneManager = Utils.checkNotNull(sceneManager, "sceneManager");
            return this;
        }

        public Builder<ID, UC, V> setPlotContextManager(PlotContextManager<ID, UC> plotContextManager) {
            this.plotContextManager = Utils.checkNotNull(plotContextManager, "plotContextManager");
            return this;
        }

        public Builder<ID, UC, V> setBeginSceneId(ElementId beginSceneId) {
            this.beginSceneId = Utils.checkNotNull(beginSceneId, "beginSceneId");
            return this;
        }

        public Plot<ID, UC, V> build() {
            Utils.checkNotNull(beginSceneId, "beginSceneId");
            Utils.checkNotNull(sceneManager, "sceneManager");
            Utils.checkNotNull(plotContextManager, "plotContextManager");

            return new Plot<>(this);
        }

    }


}
