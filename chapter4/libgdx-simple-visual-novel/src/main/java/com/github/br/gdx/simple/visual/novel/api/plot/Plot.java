package com.github.br.gdx.simple.visual.novel.api.plot;

import com.github.br.gdx.simple.visual.novel.Utils;
import com.github.br.gdx.simple.visual.novel.api.ElementId;
import com.github.br.gdx.simple.visual.novel.api.context.*;
import com.github.br.gdx.simple.visual.novel.api.node.NodeResult;
import com.github.br.gdx.simple.visual.novel.api.node.NodeResultType;
import com.github.br.gdx.simple.visual.novel.api.scene.Scene;
import com.github.br.gdx.simple.visual.novel.api.scene.SceneUtils;
import com.github.br.gdx.simple.visual.novel.api.screen.ScreenManager;

public class Plot<UC extends UserContext, SC extends ScreenManager> {

    private final SceneManager<UC, SC> sceneManager;
    private final PlotConfig config;

    private final PlotContext<UC, SC> plotContext;

    public Plot(Builder<UC, SC> builder) {
        this.sceneManager = Utils.checkNotNull(builder.sceneManager, "sceneManager");
        this.config = builder.config;

        ServiceContext<SC> serviceContext = new ServiceContext<>();
        plotContext = new PlotContext<>(serviceContext, builder.beginSceneId, this.config.isMarkVisitedNodes());

        AuxiliaryContext auxiliaryContext = plotContext.getAuxiliaryContext();
        CurrentState currentState = auxiliaryContext.currentState;
        currentState.sceneId = builder.beginSceneId;
    }

    private void changeCurrentSceneToChild(ElementId nextSceneId) {
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

    private void changeCurrentSceneToParent() {
        AuxiliaryContext auxiliaryContext = plotContext.getAuxiliaryContext();
        CurrentState currentState = auxiliaryContext.currentState;

        CurrentState parentState = currentState.parentState;
        currentState.sceneId = parentState.sceneId;
        currentState.nodeId = getNextSceneNodeId(parentState.sceneId, parentState.nodeId);
        currentState.parentState = parentState.parentState;
    }

    private ElementId getNextSceneNodeId(ElementId nextSceneId, ElementId currentNodeId) {
        Utils.checkNotNull(nextSceneId, "nextSceneId");
        Utils.checkNotNull(currentNodeId, "nodeId");

        Scene<UC, SC> scene = sceneManager.getScene(nextSceneId);
        return scene.getNextNodeId(SceneUtils.toId(currentNodeId), plotContext);
    }

    public static <UC extends UserContext, SC extends ScreenManager> Builder<UC, SC> builder(PlotConfig config) {
        Utils.checkNotNull(config, "config");
        return new Builder<>(config);
    }

    // return окончился the plot или еще нет. 'true' если окончился
    public boolean execute(float delta, UC userContext) {
        plotContext.setUserContext(userContext);
        AuxiliaryContext auxiliaryContext = plotContext.getAuxiliaryContext();
        if(auxiliaryContext.isProcessFinished()) {
            return true;
        }
        CurrentState currentState = auxiliaryContext.currentState;
        ElementId sceneId = currentState.sceneId;

        executeSceneStep(delta, sceneId);

        if(currentState.nodeId == null && currentState.parentState == null) {
            auxiliaryContext.setProcessFinished(true);
        }
        return auxiliaryContext.isProcessFinished();
    }

    private void executeSceneStep(float delta, ElementId sceneId) {
        Scene<UC, SC> currentScene = sceneManager.getScene(sceneId);
        NodeResult nodeResult = currentScene.execute(delta, plotContext);
        NodeResultType type = nodeResult.getType();
        if (NodeResultType.CHANGE_SCENE_IN == type) {
            ElementId nextSceneId = nodeResult.getSceneTitle();
            changeCurrentSceneToChild(nextSceneId);
            executeSceneStep(delta, nextSceneId);
        } else if(NodeResultType.CHANGE_SCENE_OUT == type) {
            changeCurrentSceneToParent();
        }
    }

    public static class Builder<UC extends UserContext, SC extends ScreenManager> {

        private final PlotConfig config;
        private SceneManager<UC, SC> sceneManager;
        private ElementId beginSceneId;


        public Builder(PlotConfig config) {
            this.config = Utils.checkNotNull(config, "config");
        }

        public Builder<UC, SC> setSceneManager(SceneManager<UC, SC> sceneManager) {
            this.sceneManager = Utils.checkNotNull(sceneManager, "sceneManager");
            return this;
        }

        public Builder<UC, SC> setBeginSceneId(ElementId beginSceneId) {
            this.beginSceneId = Utils.checkNotNull(beginSceneId, "beginSceneId");
            return this;
        }

        public Plot<UC, SC> build() {
            Utils.checkNotNull(beginSceneId, "beginSceneId");
            Utils.checkNotNull(sceneManager, "sceneManager");

            return new Plot<>(this);
        }

    }


}
