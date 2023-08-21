package com.github.br.gdx.simple.visual.novel.api.node;

import com.github.br.gdx.simple.visual.novel.api.context.PlotContext;
import com.github.br.gdx.simple.visual.novel.api.context.UserContext;
import com.github.br.gdx.simple.visual.novel.api.screen.ScreenManager;

public interface Node<UC extends UserContext, SC extends ScreenManager> {

    NodeResult execute(float delta, PlotContext<UC, SC> plotContext, boolean isVisited);

}
