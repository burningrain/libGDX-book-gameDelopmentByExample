package com.github.br.gdx.simple.visual.novel.api.context;

import com.github.br.gdx.simple.visual.novel.Utils;
import com.github.br.gdx.simple.visual.novel.api.ElementId;

public class PlotContext<UC extends UserContext> {

    private final AuxiliaryContext auxiliaryContext;
    private UC userContext;

    public PlotContext(ElementId sceneId, boolean isMarkVisitedNodes) {
        Utils.checkNotNull(sceneId, "sceneId");

        auxiliaryContext = new AuxiliaryContext(isMarkVisitedNodes);
        auxiliaryContext.currentState.sceneId = sceneId;
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

}
