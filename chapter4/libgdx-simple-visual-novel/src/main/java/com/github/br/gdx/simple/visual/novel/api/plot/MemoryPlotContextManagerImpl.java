package com.github.br.gdx.simple.visual.novel.api.plot;

import com.github.br.gdx.simple.visual.novel.api.context.PlotContext;
import com.github.br.gdx.simple.visual.novel.api.context.UserContext;

import java.util.concurrent.ConcurrentHashMap;

public class MemoryPlotContextManagerImpl<ID, T extends UserContext> implements PlotContextManager<ID, T>  {

    private final ConcurrentHashMap<ID, PlotContext<ID, T>> concurrentHashMap = new ConcurrentHashMap<>();

    @Override
    public PlotContext<ID, T> getPlotContext(ID plotId) {
        return concurrentHashMap.get(plotId);
    }

    @Override
    public void savePlotContext(PlotContext<ID, T> plotContext) {
        concurrentHashMap.put(plotContext.getPlotId(), plotContext);
    }

}
