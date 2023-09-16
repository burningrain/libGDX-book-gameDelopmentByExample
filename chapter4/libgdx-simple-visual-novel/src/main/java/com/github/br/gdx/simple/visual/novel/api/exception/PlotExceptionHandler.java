package com.github.br.gdx.simple.visual.novel.api.exception;

import com.github.br.gdx.simple.visual.novel.api.context.PlotContext;
import com.github.br.gdx.simple.visual.novel.api.context.UserContext;

public interface PlotExceptionHandler<ID, UC extends UserContext> {

    void handle(Exception e, PlotContext<ID, UC> plotContext);

}
