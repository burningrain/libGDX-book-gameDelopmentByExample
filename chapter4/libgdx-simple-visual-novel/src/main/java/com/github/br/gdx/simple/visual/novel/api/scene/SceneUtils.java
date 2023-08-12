package com.github.br.gdx.simple.visual.novel.api.scene;

import com.badlogic.gdx.utils.ObjectSet;
import com.github.br.gdx.simple.visual.novel.api.ElementId;
import com.github.br.gdx.simple.visual.novel.graph.GraphElementId;

public final class SceneUtils {

    private SceneUtils(){}

    public static ObjectSet<GraphElementId> toId(ObjectSet<ElementId> nodes) {
        ObjectSet<GraphElementId> result = new ObjectSet<>();
        for (ElementId endNode : nodes) {
            result.add(toId(endNode));
        }

        return result;
    }

    public static GraphElementId toId(ElementId nodeId) {
        return GraphElementId.of(nodeId.getId());
    }

    public static ElementId toId(GraphElementId nodeId) {
        return ElementId.of(nodeId.getId());
    }

}
