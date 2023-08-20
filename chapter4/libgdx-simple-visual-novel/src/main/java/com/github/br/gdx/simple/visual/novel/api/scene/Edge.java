package com.github.br.gdx.simple.visual.novel.api.scene;

import com.github.br.gdx.simple.visual.novel.Utils;
import com.github.br.gdx.simple.visual.novel.api.ElementId;
import com.github.br.gdx.simple.visual.novel.api.context.PlotContext;
import com.github.br.gdx.simple.visual.novel.api.context.UserContext;
import com.github.br.gdx.simple.visual.novel.api.edge.EmptyPredicate;
import com.github.br.gdx.simple.visual.novel.api.edge.Predicate;
import com.github.br.gdx.simple.visual.novel.api.screen.ScreenManager;

import java.util.Objects;

public class Edge<UC extends UserContext, SC extends ScreenManager> implements Comparable<Edge<UC,SC>> {
    ElementId sourceId;
    ElementId destId;
    final Predicate<UC, SC> predicate;

    public Edge(ElementId sourceId, ElementId destId) {
        this(sourceId, destId, EmptyPredicate.<UC, SC>empty());
    }

    public Edge(ElementId sourceId, ElementId destId, Predicate<UC, SC> predicate) {
        this.sourceId = Utils.checkNotNull(sourceId, "sourceId");
        this.destId = Utils.checkNotNull(destId, "destId");
        this.predicate = Utils.checkNotNull(predicate, "predicate");
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

    public boolean isTransitionAvailable(PlotContext<UC, SC> plotContext) {
        return predicate.test(plotContext);
    }

    public boolean isEmptyPredicate() {
        return predicate instanceof EmptyPredicate;
    }

}


