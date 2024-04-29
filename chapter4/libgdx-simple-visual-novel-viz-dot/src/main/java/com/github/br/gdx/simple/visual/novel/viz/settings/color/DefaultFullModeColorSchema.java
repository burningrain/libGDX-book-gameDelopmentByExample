package com.github.br.gdx.simple.visual.novel.viz.settings.color;

import com.github.br.gdx.simple.visual.novel.viz.NodeWrapperViz;

import java.util.Map;

public class DefaultFullModeColorSchema implements FullModeColorSchema {

    @Override
    public String getCustomParamNameColor(NodeWrapperViz<?> nodeWrapper, Map<String, String> customParams, String key) {
        return GraphvizColor.SEA_SHELL;
    }

    @Override
    public String getCustomParamValueColor(NodeWrapperViz<?> nodeWrapper, Map<String, String> customParams, String key, String value) {
        return GraphvizColor.SEA_SHELL;
    }

}
