package com.github.br.gdx.simple.visual.novel.viz;

import com.github.br.gdx.simple.visual.novel.viz.settings.DotVizSettings;

public interface VizConverter {

    String convert(PLotViz<?> pLotViz, DotVizSettings settings);

}
