package com.github.br.gdx.simple.visual.novel.api.plot;

import com.github.br.gdx.simple.visual.novel.api.context.PlotContext;
import com.github.br.gdx.simple.visual.novel.api.context.UserContext;

public interface PlotContextManager<ID, UC extends UserContext> {

    PlotContext<ID, UC> getPlotContext(ID plotId);

    void savePlotContext(PlotContext<ID, UC> plotContext);

    void handleFinishedPlot(PlotContext<ID, UC> plotContext);

}
