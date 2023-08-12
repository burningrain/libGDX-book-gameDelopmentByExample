package com.github.br.gdx.simple.visual.novel.api.generator;

import com.github.br.gdx.simple.visual.novel.api.ElementId;

public interface GeneratorEdgeId {

    ElementId nextId(ElementId source, ElementId dest);

}
