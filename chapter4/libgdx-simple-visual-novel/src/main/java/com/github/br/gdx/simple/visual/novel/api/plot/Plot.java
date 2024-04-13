package com.github.br.gdx.simple.visual.novel.api.plot;

import com.github.br.gdx.simple.visual.novel.api.ElementId;
import com.github.br.gdx.simple.visual.novel.api.context.AuxiliaryContext;
import com.github.br.gdx.simple.visual.novel.api.context.CurrentState;
import com.github.br.gdx.simple.visual.novel.api.context.PlotContext;
import com.github.br.gdx.simple.visual.novel.api.context.UserContext;
import com.github.br.gdx.simple.visual.novel.api.exception.PlotException;
import com.github.br.gdx.simple.visual.novel.api.exception.PlotExceptionHandler;
import com.github.br.gdx.simple.visual.novel.api.node.NodeResult;
import com.github.br.gdx.simple.visual.novel.api.node.NodeResultType;
import com.github.br.gdx.simple.visual.novel.api.node.NodeType;
import com.github.br.gdx.simple.visual.novel.api.node.NodeVisitor;
import com.github.br.gdx.simple.visual.novel.api.plot.visitor.DefaultPlotVisitorFactory;
import com.github.br.gdx.simple.visual.novel.api.plot.visitor.PlotVisitor;
import com.github.br.gdx.simple.visual.novel.api.plot.visitor.PlotVisitorFactory;
import com.github.br.gdx.simple.visual.novel.api.scene.Scene;
import com.github.br.gdx.simple.visual.novel.api.scene.SceneResult;
import com.github.br.gdx.simple.visual.novel.api.scene.SceneUtils;
import com.github.br.gdx.simple.visual.novel.utils.NullObjects;
import com.github.br.gdx.simple.visual.novel.utils.StateStack;
import com.github.br.gdx.simple.visual.novel.utils.Utils;

public class Plot<ID, UC extends UserContext, V extends NodeVisitor<?>> {

    private final SceneManager<UC, V> sceneManager;
    private final PlotContextManager<ID, UC> plotContextManager;
    private final PlotExceptionHandler<ID, UC> exceptionHandler;
    private final PlotConfig<ID> config;

    private final ElementId beginSceneId;

    private final PlotVisitorFactory<V> defaultPlotVisitorFactory;

    public Plot(Builder<ID, UC, V> builder) {
        this.sceneManager = Utils.checkNotNull(builder.sceneManager, "sceneManager");
        this.plotContextManager = Utils.checkNotNull(builder.plotContextManager, "plotContextManager");
        this.config = Utils.checkNotNull(builder.config, "config");
        this.defaultPlotVisitorFactory = Utils.checkNotNull(builder.defaultPlotVisitorFactory, "defaultPlotVisitorFactory");
        this.exceptionHandler = builder.exceptionHandler;

        this.beginSceneId = builder.beginSceneId;
    }

    private void changeCurrentSceneToChild(PlotContext<ID, UC> plotContext, ElementId nextSceneId) {
        Utils.checkNotNull(nextSceneId, "nextSceneId");

        AuxiliaryContext auxiliaryContext = plotContext.getAuxiliaryContext();
        StateStack stateStack = auxiliaryContext.stateStack;

        stateStack.push(CurrentState.of(nextSceneId, null));
        auxiliaryContext.getPath().add(NullObjects.DOWN_INTO_SUB_PROCESS);
    }

    private void changeCurrentSceneToParent(PlotContext<ID, UC> plotContext) {
        AuxiliaryContext auxiliaryContext = plotContext.getAuxiliaryContext();
        StateStack stateStack = auxiliaryContext.stateStack;

        CurrentState currentState = null;
        do {
            stateStack.pop();
            auxiliaryContext.getPath().add(NullObjects.UP_TO_PARENT_PROCESS);
            currentState = stateStack.peek();
            if (currentState == null) {
                break;
            }
            currentState.nodeId = getNextSceneNodeId(plotContext, currentState.sceneId, currentState.nodeId);
        } while (NullObjects.THIS_IS_END_ELEMENT_IN_THE_SCENE.equals(currentState.nodeId));
    }

    private ElementId getNextSceneNodeId(PlotContext<ID, UC> plotContext, ElementId nextSceneId, ElementId currentNodeId) {
        Utils.checkNotNull(nextSceneId, "nextSceneId");
        Utils.checkNotNull(currentNodeId, "nodeId");

        Scene<UC, ?> scene = sceneManager.getScene(nextSceneId);
        return scene.getNextNodeId(SceneUtils.toId(currentNodeId), plotContext);
    }

    public static <ID, UC extends UserContext, V extends NodeVisitor<?>> Builder<ID, UC, V> builder(PlotConfig<ID> config) {
        Utils.checkNotNull(config, "config");
        return new Builder<>(config);
    }

    public boolean execute(ID plotId) {
        return this.execute(plotId, null);
    }

    public boolean execute(UC userContext) {
        return this.execute(config.getGeneratorPlotId().nextId(), userContext);
    }

    public boolean execute(ID plotId, UC userContext) {
        return this.execute(plotId, userContext, beginSceneId);
    }

    // return окончился the plot или еще нет. 'true' если окончился
    public boolean execute(ID plotId, UC userContext, ElementId beginSceneId) {
        Utils.checkNotNull(plotId, "plotId");
        PlotContext<ID, UC> plotContext = plotContextManager.getPlotContext(plotId);
        if (plotContext == null) {
            // первичная инициализация. Ожидаем, что пользователь все-таки передает какой-либо контекст
            Utils.checkNotNull(userContext, "Aren't you executing the finished plot? Problem: userContext");
            plotContext = createStartPlotContext(
                    plotId,
                    beginSceneId,
                    this.config.isMarkVisitedNodes(),
                    this.config.isSavePath(),
                    userContext
            );
        }
        if (userContext != null) {
            plotContext.setUserContext(userContext);
        }

        AuxiliaryContext auxiliaryContext = plotContext.getAuxiliaryContext();
        if (auxiliaryContext.isProcessFinished()) {
            return true;
        }

        Exception ex = null;
        SceneResult sceneResult;
        do {
            CurrentState currentState = auxiliaryContext.stateStack.peek();
            ElementId sceneId = currentState.sceneId;

            try {
                sceneResult = executeSceneStep(plotContext, sceneId);
            } catch (Exception e) {
                ex = e;
                auxiliaryContext.setHasError(true);
                auxiliaryContext.setProcessFinished(true);
                break;
            }

            CurrentState parentState = auxiliaryContext.stateStack.peekParent();
            if (NullObjects.THIS_IS_END_ELEMENT_IN_THE_SCENE.equals(currentState.nodeId) && parentState == null) {
                auxiliaryContext.setProcessFinished(true);
            }
        } while (NodeType.NOT_WAITING == sceneResult.getNodeType() && !auxiliaryContext.isProcessFinished());

        plotContextManager.savePlotContext(plotContext);

        boolean isProcessFinished = auxiliaryContext.isProcessFinished();
        if (isProcessFinished) {
            if (ex != null) {
                try {
                    handleError(plotContext, ex);
                } finally {
                    plotContextManager.handleFinishedPlot(plotContext);
                }
            } else {
                plotContextManager.handleFinishedPlot(plotContext);
            }
        }

        return isProcessFinished;
    }

    private void handleError(PlotContext<ID, UC> plotContext, Exception ex) {
        AuxiliaryContext auxiliaryContext = plotContext.getAuxiliaryContext();
        if (exceptionHandler != null) {
            exceptionHandler.handle(ex, plotContext);
        } else {
            throw new PlotException(plotContext, "Use https://dreampuf.github.io/GraphvizOnline/ for visualising of the graph:\n" +
                    getPlotAsString("ex message: " + ex.getMessage(), auxiliaryContext, defaultPlotVisitorFactory.createVisitor(), ex), ex);
        }
    }

    private String getPlotAsString(String messageForCurrentState, AuxiliaryContext auxiliaryContext, PlotVisitor<V> plotVisitor, Exception ex) {
        this.accept(plotVisitor);

        CurrentState currentState = auxiliaryContext.stateStack.peek();
        plotVisitor.visitCurrentNodeId(currentState.sceneId, currentState.nodeId, messageForCurrentState);
        if (ex != null) {
            plotVisitor.visitException(ex);
        }
        plotVisitor.visitPlotPath(auxiliaryContext.getPath());
        return plotVisitor.buildString();
    }

    public String getPlotAsString(ID plotId) {
        return getPlotAsString(defaultPlotVisitorFactory.createVisitor(), plotId);
    }

    public String getPlotAsString(PlotVisitor<V> plotVisitor, ID plotId) {
        Utils.checkNotNull(plotId, "plotId");
        PlotContext<ID, UC> plotContext = plotContextManager.getPlotContext(plotId);
        if (plotContext == null) {
            throw new IllegalArgumentException("Plot with id=[" + plotId + "] is not found");
        }

        return getPlotAsString("-- you are here --", plotContext.getAuxiliaryContext(), plotVisitor, null);
    }

    public String getPlotAsString(PlotVisitor<V> plotVisitor) {
        this.accept(plotVisitor);

        return plotVisitor.buildString();
    }

    public String getPlotAsString() {
        return getPlotAsString(defaultPlotVisitorFactory.createVisitor());
    }

    private PlotContext<ID, UC> createStartPlotContext(
            ID plotId,
            ElementId beginSceneId,
            boolean isMarkVisitedNodes,
            boolean isSavePath,
            UC userContext
    ) {
        PlotContext<ID, UC> plotContext = new PlotContext<>(plotId, beginSceneId, isMarkVisitedNodes, isSavePath);
        plotContext.setUserContext(userContext);

        AuxiliaryContext auxiliaryContext = plotContext.getAuxiliaryContext();
        CurrentState currentState = auxiliaryContext.stateStack.peek();
        if (currentState == null) {
            currentState = CurrentState.of(null, null);
            auxiliaryContext.stateStack.push(currentState);
        }
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

        private final PlotConfig<ID> config;
        private SceneManager<UC, V> sceneManager;
        private PlotContextManager<ID, UC> plotContextManager = new ThreadLocalPlotContextManagerImpl<>();
        private PlotExceptionHandler<ID, UC> exceptionHandler;
        private PlotVisitorFactory<V> defaultPlotVisitorFactory = new DefaultPlotVisitorFactory<>();

        private ElementId beginSceneId;


        public Builder(PlotConfig<ID> config) {
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

        public Builder<ID, UC, V> setExceptionHandler(PlotExceptionHandler<ID, UC> exceptionHandler) {
            this.exceptionHandler = Utils.checkNotNull(exceptionHandler, "exceptionHandler");
            return this;
        }

        public Builder<ID, UC, V> setDefaultPlotVisitorFactory(PlotVisitorFactory<V> plotVisitorFactory) {
            this.defaultPlotVisitorFactory = Utils.checkNotNull(plotVisitorFactory, "plotVisitorFactory");
            return this;
        }

        public Plot<ID, UC, V> build() {
            Utils.checkNotNull(beginSceneId, "beginSceneId");
            Utils.checkNotNull(sceneManager, "sceneManager");
            Utils.checkNotNull(plotContextManager, "plotContextManager");
            Utils.checkNotNull(defaultPlotVisitorFactory, "defaultPlotVisitorFactory");

            return new Plot<>(this);
        }

    }

}
