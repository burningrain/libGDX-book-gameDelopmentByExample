package com.github.br.gdx.simple.visual.novel.viz;

import com.github.br.gdx.simple.visual.novel.api.node.CompositeNode;
import com.github.br.gdx.simple.visual.novel.api.node.Node;
import com.github.br.gdx.simple.visual.novel.viz.data.NodeElementType;
import com.github.br.gdx.simple.visual.novel.viz.data.NodeElementTypeId;
import com.github.br.gdx.simple.visual.novel.inner.SceneLinkNode;

public class DefaultElementTypeDeterminant implements ElementTypeDeterminant {

    @Override
    public NodeElementTypeId determineType(Node<?, ?> node) {
        if (node instanceof CompositeNode) {
            return NodeElementType.COMPOSITE_NODE.getElementId();
        } else if (node instanceof SceneLinkNode) {
            return NodeElementType.SCENE_LINK.getElementId();
        } else {
            return NodeElementType.SIMPLE_NODE.getElementId();
        }
    }

}
