package com.github.br.gdx.simple.visual.novel.api.generator;

import com.github.br.gdx.simple.visual.novel.api.ElementId;

public class DefaultGeneratorEdgeId implements GeneratorEdgeId {

    private int counter = 0;

    @Override
    public ElementId nextId(ElementId source, ElementId dest) {
        counter++;
        return ElementId.of("edge_" + counter);
    }

}
