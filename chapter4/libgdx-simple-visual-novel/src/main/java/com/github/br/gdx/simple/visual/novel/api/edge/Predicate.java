package com.github.br.gdx.simple.visual.novel.api.edge;

import com.github.br.gdx.simple.visual.novel.api.context.PlotContext;
import com.github.br.gdx.simple.visual.novel.api.context.UserContext;

public interface Predicate<UC extends UserContext> {

    boolean test(PlotContext<?, UC> context);

}
