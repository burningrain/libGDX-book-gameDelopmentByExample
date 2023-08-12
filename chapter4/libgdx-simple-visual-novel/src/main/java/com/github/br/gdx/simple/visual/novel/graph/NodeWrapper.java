package com.github.br.gdx.simple.visual.novel.graph;

import com.badlogic.gdx.utils.Array;
import com.github.br.gdx.simple.visual.novel.Utils;

import java.util.Objects;

public class NodeWrapper<N, E> {

    private final GraphElementId id;
    private final N node;
    private final Array<EdgeWrapper<N, E>> edges = new Array<>();

    public NodeWrapper(GraphElementId id, N node) {
        this.id = Utils.checkNotNull(id, "id");
        this.node = Utils.checkNotNull(node, "node");
    }

    public GraphElementId getId() {
        return id;
    }

    public N getNode() {
        return node;
    }

    public Array<EdgeWrapper<N, E>> getEdges() {
        return edges;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NodeWrapper<?, ?> that = (NodeWrapper<?, ?>) o;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return id.toString();
    }
}
