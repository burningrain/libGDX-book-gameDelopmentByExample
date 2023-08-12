package com.github.br.gdx.simple.visual.novel.graph;

import com.github.br.gdx.simple.visual.novel.Utils;

import java.util.Objects;

public class EdgeWrapper<N, E> {

    private final GraphElementId id;
    private final E edge;
    private final NodeWrapper<N, E> source;
    private final NodeWrapper<N, E> dest;


    public EdgeWrapper(GraphElementId edgeId, E edge, NodeWrapper<N, E> source, NodeWrapper<N, E> dest) {
        this.id = Utils.checkNotNull(edgeId, "edgeId");
        this.edge = Utils.checkNotNull(edge, "edge");
        this.source = Utils.checkNotNull(source, "source");
        this.dest = Utils.checkNotNull(dest, "dest");
    }

    public E getEdge() {
        return edge;
    }

    public GraphElementId getId() {
        return id;
    }

    public NodeWrapper<N, E> getSource() {
        return source;
    }

    public NodeWrapper<N, E> getDest() {
        return dest;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EdgeWrapper<?, ?> that = (EdgeWrapper<?, ?>) o;
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
