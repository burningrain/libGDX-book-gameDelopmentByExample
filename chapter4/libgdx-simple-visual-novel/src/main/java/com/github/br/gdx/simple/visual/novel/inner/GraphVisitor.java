package com.github.br.gdx.simple.visual.novel.inner;

import com.github.br.gdx.simple.visual.novel.api.node.NodeType;
import com.github.br.gdx.simple.visual.novel.inner.graph.GraphElementId;

public interface GraphVisitor<N, E> {

    void visitNode(GraphElementId nodeId, N node, NodeType nodeType);

    void visitEdge(GraphElementId edgeId, E edge);

}
