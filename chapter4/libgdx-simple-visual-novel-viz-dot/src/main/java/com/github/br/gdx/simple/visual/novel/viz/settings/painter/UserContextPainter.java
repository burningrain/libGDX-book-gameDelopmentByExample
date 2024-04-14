package com.github.br.gdx.simple.visual.novel.viz.settings.painter;

import com.github.br.gdx.simple.visual.novel.api.context.UserContext;
import com.github.br.gdx.simple.visual.novel.viz.settings.DotVizSettings;

public interface UserContextPainter {

    String createContext(DotVizSettings settings, UserContext userContext);

}
