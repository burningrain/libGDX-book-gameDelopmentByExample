package com.github.br.gdx.simple.visual.novel.viz.impl;

import com.github.br.gdx.simple.visual.novel.api.ElementId;
import com.github.br.gdx.simple.visual.novel.api.context.CurrentState;
import com.github.br.gdx.simple.visual.novel.api.context.PlotContext;
import com.github.br.gdx.simple.visual.novel.api.context.UserContext;
import com.github.br.gdx.simple.visual.novel.api.node.Node;
import com.github.br.gdx.simple.visual.novel.api.node.NodeResult;
import com.github.br.gdx.simple.visual.novel.api.node.NodeResultType;

public class TestNode<T extends UserContext> implements Node<T, CustomNodeVisitor> {

    private static final NodeResult NODE_RESULT = new NodeResult(NodeResultType.NEXT);

    @Override
    public NodeResult execute(PlotContext<?, T> plotContext, boolean isVisited) {
        CurrentState currentState = plotContext.getAuxiliaryContext().stateStack.peek();
        System.out.println("execute: " + currentState);

        return NODE_RESULT;
    }

    @Override
    public void accept(ElementId sceneId, ElementId nodeId, CustomNodeVisitor visitor) {
        visitor.visit(sceneId, nodeId, this);
    }

}
