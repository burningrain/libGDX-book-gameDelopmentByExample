package com.github.br.gdx.simple.visual.novel.impl;

import com.github.br.gdx.simple.visual.novel.api.context.CurrentState;
import com.github.br.gdx.simple.visual.novel.api.context.PlotContext;
import com.github.br.gdx.simple.visual.novel.api.context.UserContext;
import com.github.br.gdx.simple.visual.novel.api.node.Node;
import com.github.br.gdx.simple.visual.novel.api.node.NodeResult;
import com.github.br.gdx.simple.visual.novel.api.node.NodeResultType;
import com.github.br.gdx.simple.visual.novel.api.screen.ScreenManager;

public class TestNode<T extends UserContext, SC extends ScreenManager> implements Node<T, SC> {

    @Override
    public NodeResult execute(PlotContext<T, SC> plotContext, boolean isVisited) {
        CurrentState currentState = plotContext.getAuxiliaryContext().currentState;
        System.out.println("execute: " + currentState);

        return new NodeResult(NodeResultType.NEXT);
    }

}
