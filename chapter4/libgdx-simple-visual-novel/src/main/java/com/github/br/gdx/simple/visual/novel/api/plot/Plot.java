package com.github.br.gdx.simple.visual.novel.api.plot;

import com.github.br.gdx.simple.visual.novel.Utils;
import com.github.br.gdx.simple.visual.novel.api.ElementId;
import com.github.br.gdx.simple.visual.novel.api.context.*;
import com.github.br.gdx.simple.visual.novel.api.node.NodeResult;
import com.github.br.gdx.simple.visual.novel.api.node.NodeResultType;
import com.github.br.gdx.simple.visual.novel.api.scene.Scene;
import com.github.br.gdx.simple.visual.novel.api.scene.SceneUtils;

public class Plot<UC extends UserContext> {

    private final SceneManager<UC> sceneManager;
    private final PlotConfig config;

    private final PlotContext<UC> plotContext;

    public Plot(Builder<UC> builder) {
        this.sceneManager = Utils.checkNotNull(builder.sceneManager, "sceneManager");
        this.config = builder.config;

        plotContext = new PlotContext<>(builder.beginSceneId, this.config.isMarkVisitedNodes());

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

        Scene<UC> scene = sceneManager.getScene(nextSceneId);
        return scene.getNextNodeId(SceneUtils.toId(currentNodeId), plotContext);
    }

    public static <UC extends UserContext> Builder<UC> builder(PlotConfig config) {
        Utils.checkNotNull(config, "config");
        return new Builder<>(config);
    }

    // return окончился the plot или еще нет. 'true' если окончился
    public boolean execute(UC userContext) {
        plotContext.setUserContext(userContext);
        AuxiliaryContext auxiliaryContext = plotContext.getAuxiliaryContext();
        if(auxiliaryContext.isProcessFinished()) {
            return true;
        }
        CurrentState currentState = auxiliaryContext.currentState;
        ElementId sceneId = currentState.sceneId;

        executeSceneStep(sceneId);

        if(currentState.nodeId == null && currentState.parentState == null) {
            auxiliaryContext.setProcessFinished(true);
        }
        return auxiliaryContext.isProcessFinished();
    }

    private void executeSceneStep(ElementId sceneId) {
        Scene<UC> currentScene = sceneManager.getScene(sceneId);
        NodeResult nodeResult = currentScene.execute(plotContext);
        NodeResultType type = nodeResult.getType();
        if (NodeResultType.CHANGE_SCENE_IN == type) {
            ElementId nextSceneId = nodeResult.getSceneTitle();
            changeCurrentSceneToChild(nextSceneId);
            executeSceneStep(nextSceneId);
        } else if(NodeResultType.CHANGE_SCENE_OUT == type) {
            changeCurrentSceneToParent();
        }
    }

    public static class Builder<UC extends UserContext> {

        private final PlotConfig config;
        private SceneManager<UC> sceneManager;
        private ElementId beginSceneId;


        public Builder(PlotConfig config) {
            this.config = Utils.checkNotNull(config, "config");
        }

        public Builder<UC> setSceneManager(SceneManager<UC> sceneManager) {
            this.sceneManager = Utils.checkNotNull(sceneManager, "sceneManager");
            return this;
        }

        public Builder<UC> setBeginSceneId(ElementId beginSceneId) {
            this.beginSceneId = Utils.checkNotNull(beginSceneId, "beginSceneId");
            return this;
        }

        public Plot<UC> build() {
            Utils.checkNotNull(beginSceneId, "beginSceneId");
            Utils.checkNotNull(sceneManager, "sceneManager");

            return new Plot<>(this);
        }

    }


}
