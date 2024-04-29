package com.github.br.gdx.simple.visual.novel.inner;

import com.github.br.gdx.simple.visual.novel.api.node.*;
import com.github.br.gdx.simple.visual.novel.utils.Utils;
import com.github.br.gdx.simple.visual.novel.api.ElementId;
import com.github.br.gdx.simple.visual.novel.api.context.PlotContext;
import com.github.br.gdx.simple.visual.novel.api.context.UserContext;

public class SceneLinkNode<UC extends UserContext, V extends NodeVisitor> implements Node<UC, V> {

    private final ElementId sceneTitle;

    public SceneLinkNode(ElementId sceneTitle) {
        this.sceneTitle = Utils.checkNotNull(sceneTitle, "sceneTitle");
    }

    @Override
    public NodeResult execute(PlotContext<?, UC> plotContext, boolean isVisited) {
        return new NodeResult(sceneTitle, NodeResultType.CHANGE_SCENE_IN);
    }

    @Override
    public void accept(ElementId sceneId, ElementId nodeId, NodeType nodeType, V visitor) {
        visitor.visit(sceneId, nodeId, nodeType, this);
    }

    public ElementId getSceneTitle() {
        return sceneTitle;
    }

}
