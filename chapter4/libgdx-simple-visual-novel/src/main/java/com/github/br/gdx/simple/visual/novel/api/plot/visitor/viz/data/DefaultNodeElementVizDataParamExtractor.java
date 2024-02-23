package com.github.br.gdx.simple.visual.novel.api.plot.visitor.viz.data;

import com.github.br.gdx.simple.visual.novel.api.node.Node;

import java.util.Collections;
import java.util.Map;

public class DefaultNodeElementVizDataParamExtractor implements NodeElementVizDataParamExtractor {
    @Override
    public Map<String, String> extractNodeParams(Node<?, ?> params) {
        return Collections.emptyMap();
    }
}
