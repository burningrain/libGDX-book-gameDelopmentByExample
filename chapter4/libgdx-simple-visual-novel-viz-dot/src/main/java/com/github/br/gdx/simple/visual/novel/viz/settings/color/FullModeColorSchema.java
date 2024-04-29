package com.github.br.gdx.simple.visual.novel.viz.settings.color;

import com.github.br.gdx.simple.visual.novel.viz.NodeWrapperViz;

import java.util.Map;

public interface FullModeColorSchema {

    String getCustomParamNameColor(NodeWrapperViz<?> nodeWrapper, Map<String, String> customParams, String key);

    String getCustomParamValueColor(NodeWrapperViz<?> nodeWrapper, Map<String, String> customParams, String key, String value);

}
