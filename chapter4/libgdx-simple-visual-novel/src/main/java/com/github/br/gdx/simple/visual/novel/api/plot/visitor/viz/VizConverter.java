package com.github.br.gdx.simple.visual.novel.api.plot.visitor.viz;

import com.github.br.gdx.simple.visual.novel.api.plot.visitor.viz.settings.DotVizSettings;

public interface VizConverter {

    String convert(PLotViz<?> pLotViz, DotVizSettings settings);

}
