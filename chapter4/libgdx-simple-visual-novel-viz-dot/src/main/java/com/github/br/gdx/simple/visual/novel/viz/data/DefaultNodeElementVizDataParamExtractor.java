package com.github.br.gdx.simple.visual.novel.viz.data;

import com.github.br.gdx.simple.visual.novel.api.node.Node;

import java.util.Collections;
import java.util.Map;

public class DefaultNodeElementVizDataParamExtractor implements NodeElementVizDataParamExtractor {

    @Override
    public Map<String, String> extractNodeParams(Node<?, ?> node) {
        return Collections.emptyMap();
    }

}
