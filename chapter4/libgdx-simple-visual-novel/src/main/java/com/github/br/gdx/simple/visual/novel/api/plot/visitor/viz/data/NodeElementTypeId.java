package com.github.br.gdx.simple.visual.novel.api.plot.visitor.viz.data;

import com.github.br.gdx.simple.visual.novel.utils.Utils;

public class NodeElementTypeId {

    private final String id;

    private NodeElementTypeId(String id) {
        this.id = Utils.checkNotNull(id, "id");
    }

    public String getId() {
        return id;
    }

    public static NodeElementTypeId of(String id) {
        return new NodeElementTypeId(id);
    }

}