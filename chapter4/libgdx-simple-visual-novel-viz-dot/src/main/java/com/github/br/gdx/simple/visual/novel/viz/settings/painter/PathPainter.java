package com.github.br.gdx.simple.visual.novel.viz.settings.painter;

import com.github.br.gdx.simple.visual.novel.api.context.CurrentState;
import com.github.br.gdx.simple.visual.novel.api.context.UserContext;
import com.github.br.gdx.simple.visual.novel.viz.PLotViz;
import com.github.br.gdx.simple.visual.novel.viz.settings.DotVizSettings;

import java.util.List;

public interface PathPainter {

    String createPath(DotVizSettings settings, PLotViz<?> pLotViz, List<CurrentState> states);

}
