package com.github.br.gdx.simple.visual.novel.graph;

import com.github.br.gdx.simple.visual.novel.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class NodeWrapper<N, E> {

    private final GraphElementId id;
    private final N node;
    private final ArrayList<EdgeWrapper<N, E>> edges = new ArrayList<>();

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

    public List<EdgeWrapper<N, E>> getEdges() {
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
