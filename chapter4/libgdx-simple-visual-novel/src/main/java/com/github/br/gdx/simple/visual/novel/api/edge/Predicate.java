package com.github.br.gdx.simple.visual.novel.api.edge;

import com.github.br.gdx.simple.visual.novel.api.context.PlotContext;
import com.github.br.gdx.simple.visual.novel.api.context.UserContext;
import com.github.br.gdx.simple.visual.novel.api.screen.ScreenManager;

public interface Predicate<UC extends UserContext, SM extends ScreenManager> {

    boolean test(PlotContext<UC, SM> context);

}
