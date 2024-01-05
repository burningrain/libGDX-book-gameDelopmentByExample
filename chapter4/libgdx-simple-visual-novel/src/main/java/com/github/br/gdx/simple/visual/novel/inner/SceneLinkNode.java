package com.github.br.gdx.simple.visual.novel.inner;

import com.github.br.gdx.simple.visual.novel.utils.Utils;
import com.github.br.gdx.simple.visual.novel.api.ElementId;
import com.github.br.gdx.simple.visual.novel.api.context.PlotContext;
import com.github.br.gdx.simple.visual.novel.api.context.UserContext;
import com.github.br.gdx.simple.visual.novel.api.node.Node;
import com.github.br.gdx.simple.visual.novel.api.node.NodeResult;
import com.github.br.gdx.simple.visual.novel.api.node.NodeResultType;
import com.github.br.gdx.simple.visual.novel.api.node.NodeVisitor;

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
    public void accept(ElementId sceneId, ElementId nodeId,V visitor) {
        visitor.visit(sceneId, nodeId, this);
    }

    public ElementId getSceneTitle() {
        return sceneTitle;
    }

}
