package com.github.br.gdx.simple.visual.novel.api.context;

import com.github.br.gdx.simple.visual.novel.utils.Utils;
import com.github.br.gdx.simple.visual.novel.api.ElementId;

public class PlotContext<ID, UC extends UserContext> {

    private final ID plotId;
    private final AuxiliaryContext auxiliaryContext;
    private UC userContext;

    public PlotContext(ID plotId, ElementId sceneId, boolean isMarkVisitedNodes, boolean isSavePath) {
        this.plotId = Utils.checkNotNull(plotId, "plotId");
        Utils.checkNotNull(sceneId, "sceneId");

        auxiliaryContext = new AuxiliaryContext(isMarkVisitedNodes, isSavePath);
        auxiliaryContext.stateStack.push(CurrentState.of(sceneId, null));
    }

    public void setUserContext(UC userContext) {
        this.userContext = Utils.checkNotNull(userContext, "userContext");
    }

    public UC getUserContext() {
        return userContext;
    }

    public AuxiliaryContext getAuxiliaryContext() {
        return auxiliaryContext;
    }

    public ID getPlotId() {
        return plotId;
    }

}
