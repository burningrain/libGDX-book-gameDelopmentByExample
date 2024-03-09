package com.github.br.gdx.simple.visual.novel.api.plot.visitor.viz.settings.color;

import com.github.br.gdx.simple.visual.novel.api.node.CompositeNode;
import com.github.br.gdx.simple.visual.novel.api.node.Node;
import com.github.br.gdx.simple.visual.novel.inner.SceneLinkNode;

import java.util.Map;

public class DefaultFullModeColorSchema implements FullModeColorSchema {

    private String getHeaderColor(Node<?, ?> node) {
        String colour = null;
        if (node instanceof SceneLinkNode) {
            colour = SvgColor.LIGHT_CYAN;
        } else if (node instanceof CompositeNode) {
            colour = SvgColor.PEACH_PUFF;
        } else {
            colour = SvgColor.PAPAYA_WHIP;
        }

        return colour;
    }

    @Override
    public String getNodeIdColor(String fullNodeId, Node<?, ?> node) {
        return getHeaderColor(node);
    }

    @Override
    public String getClassNameColor(String fullNodeId, Node<?, ?> node) {
        return getHeaderColor(node);
    }

    @Override
    public String getCustomParamNameColor(String nodeId, Node<?, ?> node, Map<String, String> customParams, String key) {
        return SvgColor.SEA_SHELL;
    }

    @Override
    public String getCustomParamValueColor(String nodeId, Node<?, ?> node, Map<String, String> customParams, String key, String value) {
        return SvgColor.SEA_SHELL;
    }

}
