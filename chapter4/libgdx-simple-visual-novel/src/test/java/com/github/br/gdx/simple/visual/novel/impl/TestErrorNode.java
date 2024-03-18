package com.github.br.gdx.simple.visual.novel.impl;

import com.github.br.gdx.simple.visual.novel.api.ElementId;
import com.github.br.gdx.simple.visual.novel.api.context.PlotContext;
import com.github.br.gdx.simple.visual.novel.api.context.UserContext;
import com.github.br.gdx.simple.visual.novel.api.node.Node;
import com.github.br.gdx.simple.visual.novel.api.node.NodeResult;

public class TestErrorNode<T extends UserContext> implements Node<T, CustomNodeVisitor> {
    @Override
    public NodeResult execute(PlotContext<?, T> plotContext, boolean isVisited) {
        throw new RuntimeException();
    }

    @Override
    public void accept(ElementId sceneId, ElementId nodeId, CustomNodeVisitor visitor) {
        visitor.visit(sceneId, nodeId, this);
    }

}
