package com.github.br.gdx.simple.visual.novel.api.scene;

import com.github.br.gdx.simple.visual.novel.api.ElementId;
import com.github.br.gdx.simple.visual.novel.graph.GraphElementId;

public final class SceneUtils {

    private SceneUtils(){}

    public static GraphElementId toId(ElementId nodeId) {
        return GraphElementId.of(nodeId.getId());
    }

    public static ElementId toId(GraphElementId nodeId) {
        return ElementId.of(nodeId.getId());
    }

}
