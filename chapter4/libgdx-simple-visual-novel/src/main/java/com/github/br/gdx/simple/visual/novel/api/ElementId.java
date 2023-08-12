package com.github.br.gdx.simple.visual.novel.api;

import com.github.br.gdx.simple.visual.novel.Utils;

import java.util.Objects;

public class ElementId implements Comparable<ElementId> {

    private final String id;

    private ElementId(String id) {
        this.id = Utils.checkNotNull(id, "id");
    }

    public static ElementId of(String id) {
        return new ElementId(id);
    }

    public String getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ElementId nodeId = (ElementId) o;
        return id.equals(nodeId.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public int compareTo(ElementId o) {
        return this.id.compareTo(o.id);
    }

    @Override
    public String toString() {
        return id;
    }
}
