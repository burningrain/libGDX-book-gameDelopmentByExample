package com.github.br.gdx.simple.visual.novel.api.node;

import com.github.br.gdx.simple.visual.novel.api.ElementId;
import com.github.br.gdx.simple.visual.novel.inner.SceneLinkNode;

public interface NodeVisitor<V extends NodeVisitor<?>> {

    void visit(ElementId sceneId, ElementId nodeId,  NodeType nodeType, CompositeNode<?, V> ucvCompositeNode);

    void visit(ElementId sceneId, ElementId nodeId, NodeType nodeType, SceneLinkNode<?, V> ucvSceneLinkNode);

}
