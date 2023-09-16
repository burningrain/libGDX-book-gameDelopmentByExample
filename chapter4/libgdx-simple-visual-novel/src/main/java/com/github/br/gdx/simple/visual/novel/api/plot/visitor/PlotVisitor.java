package com.github.br.gdx.simple.visual.novel.api.plot.visitor;

import com.github.br.gdx.simple.visual.novel.api.ElementId;
import com.github.br.gdx.simple.visual.novel.api.node.Node;
import com.github.br.gdx.simple.visual.novel.api.node.NodeVisitor;
import com.github.br.gdx.simple.visual.novel.api.scene.Edge;
import com.github.br.gdx.simple.visual.novel.inner.graph.GraphElementId;

public interface PlotVisitor<V extends NodeVisitor> {

    void visitNode(ElementId sceneId, ElementId nodeId, Node<?, V> node);

    void visitEdge(ElementId sceneId, ElementId nodeId, Edge<?> node);

    void visitBeginNodeId(ElementId sceneId, ElementId beginNodeId);

    void visitBeginSceneId(ElementId sceneId);

}
