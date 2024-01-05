package com.github.br.gdx.simple.visual.novel.api.plot.visitor.viz.data;

import com.github.br.gdx.simple.visual.novel.api.ElementId;
import com.github.br.gdx.simple.visual.novel.api.scene.Edge;

public class EdgeElementVizData {

    public final ElementId edgeId;
    public final Edge<?> edge;

    public EdgeElementVizData(ElementId edgeId, Edge<?> edge) {
        this.edgeId = edgeId;
        this.edge = edge;
    }

}
