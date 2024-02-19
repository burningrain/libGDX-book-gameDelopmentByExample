package com.github.br.gdx.simple.visual.novel.api.plot.visitor.viz.data;

import com.github.br.gdx.simple.visual.novel.api.node.CompositeNode;
import com.github.br.gdx.simple.visual.novel.api.node.Node;
import com.github.br.gdx.simple.visual.novel.inner.SceneLinkNode;

public class DefaultNodeElementVizDataFactory implements NodeElementVizDataFactory {

    @Override
    public String getNodeShapeForShortInfo(Node<?, ?> node) {
        if (node instanceof CompositeNode) {
            return NodeElementType.COMPOSITE_NODE.getDotShape();
        } else if (node instanceof SceneLinkNode) {
            return NodeElementType.SCENE_LINK.getDotShape();
        } else {
            return NodeElementType.SIMPLE_NODE.getDotShape();
        }
    }

    @Override
    public String createLabelFullNodeInfo(String nodeId, NodeElementVizData value) {
        StringBuilder builder = new StringBuilder();
        builder


        return builder.toString();
    }

}
