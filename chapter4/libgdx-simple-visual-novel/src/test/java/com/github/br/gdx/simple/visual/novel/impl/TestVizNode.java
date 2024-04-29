package com.github.br.gdx.simple.visual.novel.impl;

import com.github.br.gdx.simple.visual.novel.api.ElementId;
import com.github.br.gdx.simple.visual.novel.api.context.CurrentState;
import com.github.br.gdx.simple.visual.novel.api.context.PlotContext;
import com.github.br.gdx.simple.visual.novel.api.context.UserContext;
import com.github.br.gdx.simple.visual.novel.api.node.Node;
import com.github.br.gdx.simple.visual.novel.api.node.NodeResult;
import com.github.br.gdx.simple.visual.novel.api.node.NodeResultType;
import com.github.br.gdx.simple.visual.novel.api.node.NodeType;
import com.github.br.gdx.simple.visual.novel.utils.Utils;

public class TestVizNode<T extends UserContext> implements Node<T, CustomNodeVisitor> {

    private static final NodeResult NODE_RESULT = new NodeResult(NodeResultType.NEXT);

    public enum TYPE {
        A,B,C,D;
    }

    private final TYPE type;

    public TestVizNode(TYPE type) {
        this.type = Utils.checkNotNull(type, "type");
    }

    public TYPE getType() {
        return type;
    }

    @Override
    public NodeResult execute(PlotContext<?, T> plotContext, boolean isVisited) {
        CurrentState currentState = plotContext.getAuxiliaryContext().stateStack.peek();
        System.out.println("execute: " + currentState);

        return NODE_RESULT;
    }

    @Override
    public void accept(ElementId sceneId, ElementId nodeId, NodeType nodeType, CustomNodeVisitor visitor) {
        visitor.visit(sceneId, nodeId, nodeType, this);
    }

}
