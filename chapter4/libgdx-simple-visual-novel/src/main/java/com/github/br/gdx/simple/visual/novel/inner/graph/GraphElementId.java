package com.github.br.gdx.simple.visual.novel.inner.graph;

import com.github.br.gdx.simple.visual.novel.utils.Utils;

import java.util.Objects;

public class GraphElementId implements Comparable<GraphElementId> {

    private final String id;

    private GraphElementId(String id) {
        this.id = Utils.checkNotNull(id, "id");
    }

    public static GraphElementId of(String id) {
        return new GraphElementId(id);
    }

    public String getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GraphElementId that = (GraphElementId) o;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public int compareTo(GraphElementId o) {
        return this.id.compareTo(o.id);
    }

    @Override
    public String toString() {
        return id;
    }
}
