package com.github.br.gdx.simple.visual.novel.api.plot;

import com.github.br.gdx.simple.visual.novel.api.ElementId;
import com.github.br.gdx.simple.visual.novel.api.node.Node;
import com.github.br.gdx.simple.visual.novel.api.node.NodeVisitor;
import com.github.br.gdx.simple.visual.novel.api.scene.Edge;
import com.github.br.gdx.simple.visual.novel.graph.GraphElementId;

public interface PlotVisitor<V extends NodeVisitor> {

    void visitNode(ElementId sceneId, ElementId nodeId, Node<?, V> node);

    void visitEdge(ElementId sceneId, ElementId nodeId, Edge<?> node);

    void visitBeginNodeId(ElementId sceneId, GraphElementId beginNodeId);

    void visitBeginSceneId(ElementId sceneId);

}
