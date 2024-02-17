package com.github.br.gdx.simple.visual.novel.api.plot.visitor.viz;

public interface VizConverter {

    String convert(PLotViz<?> pLotViz, DotVizSettings settings);

}
