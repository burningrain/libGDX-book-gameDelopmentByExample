package com.github.br.gdx.simple.visual.novel.api.node;

import com.github.br.gdx.simple.visual.novel.api.context.PlotContext;
import com.github.br.gdx.simple.visual.novel.api.context.UserContext;

public interface Node<UC extends UserContext> {

    NodeResult execute(PlotContext<UC> plotContext, boolean isVisited);

}
