package com.github.br.gdx.simple.visual.novel.api.plot.visitor.viz.data;

import com.github.br.gdx.simple.visual.novel.api.node.Node;

public interface NodeElementVizDataFactory {

    String getNodeShapeForShortInfo(Node<?, ?> node);

    String createLabelFullNodeInfo(String nodeId, Node<?, ?> value);

}
