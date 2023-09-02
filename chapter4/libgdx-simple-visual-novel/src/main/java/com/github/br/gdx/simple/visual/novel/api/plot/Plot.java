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

public class Plot<UC extends UserContext, V extends NodeVisitor<?>> {

    private final SceneManager<UC, V> sceneManager;
    private final PlotConfig config;

    private final PlotContext<UC> plotContext; //FIXME не потокобезопасно!!!
    private final ElementId beginSceneId;

    public Plot(Builder<UC, V> builder) {
        this.sceneManager = Utils.checkNotNull(builder.sceneManager, "sceneManager");
        this.config = builder.config;

        this.beginSceneId = builder.beginSceneId;
        plotContext = new PlotContext<>(beginSceneId, this.config.isMarkVisitedNodes());

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

        Scene<UC, ?> scene = sceneManager.getScene(nextSceneId);
        return scene.getNextNodeId(SceneUtils.toId(currentNodeId), plotContext);
    }

    public static <UC extends UserContext, V extends NodeVisitor<?>> Builder<UC, V> builder(PlotConfig config) {
        Utils.checkNotNull(config, "config");
        return new Builder<>(config);
    }

    // return окончился the plot или еще нет. 'true' если окончился
    public boolean execute(UC userContext) {
        plotContext.setUserContext(userContext);
        AuxiliaryContext auxiliaryContext = plotContext.getAuxiliaryContext();
        if (auxiliaryContext.isProcessFinished()) {
            return true;
        }

        SceneResult sceneResult;
        do {
            CurrentState currentState = auxiliaryContext.currentState;
            ElementId sceneId = currentState.sceneId;

            sceneResult = executeSceneStep(sceneId);
            if (currentState.nodeId == null && currentState.parentState == null) {
                auxiliaryContext.setProcessFinished(true);
            }
        } while (NodeType.NOT_WAITING == sceneResult.getNodeType() && !auxiliaryContext.isProcessFinished());

        return auxiliaryContext.isProcessFinished();
    }

    public void accept(PlotVisitor<V> plotVisitor) {
        for (ElementId sceneId : sceneManager.getSceneIds()) {
            Scene<UC, V> scene = sceneManager.getScene(sceneId);
            scene.accept(sceneId, plotVisitor);
        }
        plotVisitor.visitBeginSceneId(this.beginSceneId);
    }

    private SceneResult executeSceneStep(ElementId sceneId) {
        Scene<UC, ?> currentScene = sceneManager.getScene(sceneId);
        SceneResult sceneResult = currentScene.execute(plotContext);
        NodeResult nodeResult = sceneResult.getNodeResult();
        NodeResultType type = nodeResult.getType();
        if (NodeResultType.CHANGE_SCENE_IN == type) {
            ElementId nextSceneId = nodeResult.getSceneTitle();
            changeCurrentSceneToChild(nextSceneId);
            return executeSceneStep(nextSceneId);
        } else if (NodeResultType.CHANGE_SCENE_OUT == type) {
            changeCurrentSceneToParent();
        }

        return sceneResult;
    }

    public static class Builder<UC extends UserContext, V extends NodeVisitor<?>> {

        private final PlotConfig config;
        private SceneManager<UC, V> sceneManager;
        private ElementId beginSceneId;


        public Builder(PlotConfig config) {
            this.config = Utils.checkNotNull(config, "config");
        }

        public Builder<UC, V> setSceneManager(SceneManager<UC, V> sceneManager) {
            this.sceneManager = Utils.checkNotNull(sceneManager, "sceneManager");
            return this;
        }

        public Builder<UC, V> setBeginSceneId(ElementId beginSceneId) {
            this.beginSceneId = Utils.checkNotNull(beginSceneId, "beginSceneId");
            return this;
        }

        public Plot<UC, V> build() {
            Utils.checkNotNull(beginSceneId, "beginSceneId");
            Utils.checkNotNull(sceneManager, "sceneManager");

            return new Plot<>(this);
        }

    }


}
