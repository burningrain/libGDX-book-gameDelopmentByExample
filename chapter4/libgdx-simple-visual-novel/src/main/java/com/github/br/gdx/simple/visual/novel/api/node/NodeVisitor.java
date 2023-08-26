package com.github.br.gdx.simple.visual.novel.api.node;

import com.github.br.gdx.simple.visual.novel.api.ElementId;
import com.github.br.gdx.simple.visual.novel.inner.SceneLinkNode;

public interface NodeVisitor<V extends NodeVisitor<?>> {

    void visit(ElementId sceneId, ElementId nodeId,  CompositeNode<?, V> ucvCompositeNode);

    void visit(ElementId sceneId, ElementId nodeId, SceneLinkNode<?, V> ucvSceneLinkNode);

}
