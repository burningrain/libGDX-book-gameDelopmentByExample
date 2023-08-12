package com.github.br.gdx.simple.visual.novel.api.scene;

import com.github.br.gdx.simple.visual.novel.Utils;
import com.github.br.gdx.simple.visual.novel.api.ElementId;

import java.util.Objects;

public class Edge implements Comparable<Edge> {
    ElementId sourceId;
    ElementId destId;

    public Edge(ElementId sourceId, ElementId destId) {
        this.sourceId = Utils.checkNotNull(sourceId, "sourceId");
        this.destId = Utils.checkNotNull(destId, "destId");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Edge edge = (Edge) o;
        return sourceId.equals(edge.sourceId) && Objects.equals(destId, edge.destId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sourceId, destId);
    }

    @Override
    public int compareTo(Edge o) {
        int sourceCompare = this.sourceId.compareTo(o.sourceId);
        if (sourceCompare != 0) {
            return sourceCompare;
        }

        if (this.destId == null && o.destId == null) {
            return 0;
        } else if (this.destId != null && this.destId.compareTo(o.destId) == 0) {
            return 0;
        } else if (this.destId == null) {
            return -1;
        } else {
            return 1;
        }
    }
}


