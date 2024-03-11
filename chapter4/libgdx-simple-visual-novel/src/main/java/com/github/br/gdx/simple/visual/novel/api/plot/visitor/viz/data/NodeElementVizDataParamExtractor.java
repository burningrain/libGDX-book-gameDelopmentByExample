package com.github.br.gdx.simple.visual.novel.api.plot.visitor.viz.data;

import com.github.br.gdx.simple.visual.novel.api.node.Node;

import java.util.Map;

public interface NodeElementVizDataParamExtractor {

    Map<String, String> extractNodeParams(Node<?,?> node);

}
