package com.github.br.gdx.simple.visual.novel.api.context;

import com.github.br.gdx.simple.visual.novel.Utils;
import com.github.br.gdx.simple.visual.novel.api.ElementId;
import com.github.br.gdx.simple.visual.novel.api.screen.ScreenManager;

public class PlotContext<UC extends UserContext, SM extends ScreenManager> {

    private final ServiceContext<SM> serviceContext;
    private final AuxiliaryContext auxiliaryContext;
    private UC userContext;

    public PlotContext(ServiceContext<SM> serviceContext, ElementId sceneId, boolean isMarkVisitedNodes) {
        this.serviceContext = Utils.checkNotNull(serviceContext, "serviceContext");
        Utils.checkNotNull(sceneId, "sceneId");

        auxiliaryContext = new AuxiliaryContext(isMarkVisitedNodes);
        auxiliaryContext.currentState.sceneId = sceneId;
    }

    public ServiceContext<SM> getServiceContext() {
        return serviceContext;
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
