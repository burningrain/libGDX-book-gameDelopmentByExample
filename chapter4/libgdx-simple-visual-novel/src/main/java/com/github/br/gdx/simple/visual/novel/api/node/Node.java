package com.github.br.gdx.simple.visual.novel.api.node;

import com.github.br.gdx.simple.visual.novel.api.ElementId;
import com.github.br.gdx.simple.visual.novel.api.context.PlotContext;
import com.github.br.gdx.simple.visual.novel.api.context.UserContext;

public interface Node<UC extends UserContext, V extends NodeVisitor> {

    NodeResult execute(PlotContext<?, UC> plotContext, boolean isVisited);

    void accept(ElementId sceneId, ElementId nodeId, NodeType nodeType, V visitor);

}
