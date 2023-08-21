package com.github.br.gdx.simple.visual.novel.inner;

import com.github.br.gdx.simple.visual.novel.Utils;
import com.github.br.gdx.simple.visual.novel.api.ElementId;
import com.github.br.gdx.simple.visual.novel.api.context.PlotContext;
import com.github.br.gdx.simple.visual.novel.api.context.UserContext;
import com.github.br.gdx.simple.visual.novel.api.node.Node;
import com.github.br.gdx.simple.visual.novel.api.node.NodeResult;
import com.github.br.gdx.simple.visual.novel.api.node.NodeResultType;

public class SceneLinkNode<UC extends UserContext> implements Node<UC> {

    private final ElementId sceneTitle;

    public SceneLinkNode(ElementId sceneTitle) {
        this.sceneTitle = Utils.checkNotNull(sceneTitle, "sceneTitle");
    }

    @Override
    public NodeResult execute(PlotContext<UC> plotContext, boolean isVisited) {
        return new NodeResult(sceneTitle, NodeResultType.CHANGE_SCENE_IN);
    }

    public ElementId getSceneTitle() {
        return sceneTitle;
    }

}
