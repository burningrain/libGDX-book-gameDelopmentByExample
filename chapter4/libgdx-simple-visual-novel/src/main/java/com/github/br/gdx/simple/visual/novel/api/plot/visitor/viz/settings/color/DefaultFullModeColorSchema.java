package com.github.br.gdx.simple.visual.novel.api.plot.visitor.viz.settings.color;

import com.github.br.gdx.simple.visual.novel.api.node.Node;

import java.util.Map;

public class DefaultFullModeColorSchema implements FullModeColorSchema {

    @Override
    public String getCustomParamNameColor(String nodeId, Node<?, ?> node, Map<String, String> customParams, String key) {
        return GraphvizColor.SEA_SHELL;
    }

    @Override
    public String getCustomParamValueColor(String nodeId, Node<?, ?> node, Map<String, String> customParams, String key, String value) {
        return GraphvizColor.SEA_SHELL;
    }

}
