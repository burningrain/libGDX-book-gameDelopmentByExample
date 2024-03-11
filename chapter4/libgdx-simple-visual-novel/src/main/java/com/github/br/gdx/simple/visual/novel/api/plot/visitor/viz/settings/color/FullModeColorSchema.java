package com.github.br.gdx.simple.visual.novel.api.plot.visitor.viz.settings.color;

import com.github.br.gdx.simple.visual.novel.api.node.Node;

import java.util.Map;

public interface FullModeColorSchema {

    String getCustomParamNameColor(String nodeId, Node<?, ?> node, Map<String, String> customParams, String key);

    String getCustomParamValueColor(String nodeId, Node<?, ?> node, Map<String, String> customParams, String key, String value);

}
