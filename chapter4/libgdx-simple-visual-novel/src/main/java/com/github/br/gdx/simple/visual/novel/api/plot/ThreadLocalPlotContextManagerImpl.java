package com.github.br.gdx.simple.visual.novel.api.plot;

import com.github.br.gdx.simple.visual.novel.api.context.PlotContext;
import com.github.br.gdx.simple.visual.novel.api.context.UserContext;

public class ThreadLocalPlotContextManagerImpl<ID, UC extends UserContext> implements PlotContextManager<ID, UC> {

    private final ThreadLocal<PlotContext<ID, UC>> context = new ThreadLocal<>();

    @Override
    public PlotContext<ID, UC> getPlotContext(ID plotId) {
        return context.get();
    }

    @Override
    public void savePlotContext(PlotContext<ID, UC> plotContext) {
        context.set(plotContext);
    }

    @Override
    public void handleFinishedPlot(PlotContext<ID, UC> plotContext) {
        context.set(null);
    }

}
