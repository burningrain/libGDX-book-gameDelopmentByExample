package com.github.br.gdx.simple.visual.novel.impl;

import com.github.br.gdx.simple.visual.novel.api.context.CurrentState;
import com.github.br.gdx.simple.visual.novel.api.context.PlotContext;
import com.github.br.gdx.simple.visual.novel.api.node.Node;
import com.github.br.gdx.simple.visual.novel.api.node.NodeResult;
import com.github.br.gdx.simple.visual.novel.api.node.NodeResultType;

public class TestNode implements Node<TestUserContext, TestScreenManager> {

    @Override
    public NodeResult execute(float delta, PlotContext<TestUserContext, TestScreenManager> plotContext, boolean isVisited) {
        CurrentState currentState = plotContext.getAuxiliaryContext().currentState;
        System.out.println("execute: " + currentState);

        return new NodeResult(null, NodeResultType.NEXT);
    }

}
