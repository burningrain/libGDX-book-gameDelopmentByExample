package com.github.br.gdx.simple.visual.novel.inner;

import com.github.br.gdx.simple.visual.novel.graph.GraphElementId;

public interface GraphVisitor<N, E> {

    void visitNode(GraphElementId nodeId, N node);

    void visitEdge(GraphElementId edgeId, E edge);

}
