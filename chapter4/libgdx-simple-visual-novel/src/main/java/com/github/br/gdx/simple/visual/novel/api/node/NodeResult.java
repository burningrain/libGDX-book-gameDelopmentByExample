package com.github.br.gdx.simple.visual.novel.api.node;

import com.github.br.gdx.simple.visual.novel.Utils;
import com.github.br.gdx.simple.visual.novel.api.ElementId;

public class NodeResult {

    private final ElementId sceneTitle;
    private final NodeResultType type;

    public NodeResult(NodeResultType type) {
        this(null, type);
    }

    public NodeResult(ElementId sceneTitle, NodeResultType type) {
        this.type = Utils.checkNotNull(type, "type");
        this.sceneTitle = sceneTitle;
    }

    public ElementId getSceneTitle() {
        return sceneTitle;
    }

    public NodeResultType getType() {
        return type;
    }

}
