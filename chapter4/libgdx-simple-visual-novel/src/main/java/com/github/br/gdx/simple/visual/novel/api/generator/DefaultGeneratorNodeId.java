package com.github.br.gdx.simple.visual.novel.api.generator;

import com.github.br.gdx.simple.visual.novel.api.ElementId;

public class DefaultGeneratorNodeId implements GeneratorNodeId {

    private int counter = 0;

    @Override
    public ElementId nextId() {
        counter++;
        return ElementId.of("node_" + counter);
    }
}
