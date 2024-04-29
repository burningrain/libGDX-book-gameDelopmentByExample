package com.github.br.gdx.simple.visual.novel.viz.impl;

import com.github.br.gdx.simple.visual.novel.api.ElementId;
import com.github.br.gdx.simple.visual.novel.api.context.PlotContext;
import com.github.br.gdx.simple.visual.novel.api.context.UserContext;
import com.github.br.gdx.simple.visual.novel.api.node.Node;
import com.github.br.gdx.simple.visual.novel.api.node.NodeResult;
import com.github.br.gdx.simple.visual.novel.api.node.NodeResultType;
import com.github.br.gdx.simple.visual.novel.api.node.NodeType;

public class TestInnerNodeB<T extends UserContext> implements Node<T, CustomNodeVisitor> {
    @Override
    public NodeResult execute(PlotContext<?, T> plotContext, boolean isVisited) {
        return new NodeResult(NodeResultType.NEXT);
    }

    @Override
    public void accept(ElementId sceneId, ElementId nodeId, NodeType nodeType, CustomNodeVisitor visitor) {
    }

}
