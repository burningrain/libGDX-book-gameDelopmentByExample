package com.github.br.gdx.simple.visual.novel.api.node;

import com.github.br.gdx.simple.visual.novel.Utils;
import com.github.br.gdx.simple.visual.novel.api.ElementId;

public class NodeResult {

    private final ElementId nextId;
    private final NodeResultType type;

    public NodeResult(ElementId nextId, NodeResultType type) {
        this.nextId = nextId;
        this.type = Utils.checkNotNull(type, "type");
    }

    public ElementId getNextId() {
        return nextId;
    }

    public NodeResultType getType() {
        return type;
    }

}
