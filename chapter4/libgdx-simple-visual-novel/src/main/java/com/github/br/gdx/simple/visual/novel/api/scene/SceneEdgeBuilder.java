package com.github.br.gdx.simple.visual.novel.api.scene;

import com.github.br.gdx.simple.visual.novel.Utils;
import com.github.br.gdx.simple.visual.novel.api.ElementId;
import com.github.br.gdx.simple.visual.novel.api.Pair;
import com.github.br.gdx.simple.visual.novel.api.context.UserContext;
import com.github.br.gdx.simple.visual.novel.api.edge.EmptyPredicate;
import com.github.br.gdx.simple.visual.novel.api.edge.Predicate;

public class SceneEdgeBuilder<UC extends UserContext> {

    private final SceneNodeBuilder<UC> nodeBuilder;

    public SceneEdgeBuilder(SceneNodeBuilder<UC> nodeBuilder) {
        this.nodeBuilder = nodeBuilder;
    }

    public SceneNodeBuilder<UC> to(ElementId nodeId) {
        return this.to(nodeId, EmptyPredicate.<UC>empty());
    }

    public SceneNodeBuilder<UC> to(ElementId nodeId, Predicate<UC> predicate) {
        Utils.checkNotNull(nodeId, "nodeId");
        Utils.checkNotNull(nodeBuilder.currentNodeId, "prevNodeId");

        Edge<UC> edge = new Edge<>(nodeBuilder.currentNodeId, nodeId, predicate);
        if(nodeBuilder.edges.contains(edge)) {
            // TODO в логгер вывести warning о попытке переопределить существующее ребро
            return nodeBuilder;
        }

        nodeBuilder.edges.add(edge);
        return nodeBuilder;
    }

    public SceneNodeBuilder<UC> to(Pair<ElementId, Predicate<UC>>... pares) {
        for (Pair<ElementId, Predicate<UC>> pair : pares) {
            this.to(pair.getFirst(), pair.getSecond());
        }
        return nodeBuilder;
    }

    public SceneNodeBuilder<UC> to() {
        nodeBuilder.lazyBinding = true;
        return nodeBuilder;
    }

    public SceneNodeBuilder<UC> end() {
        return nodeBuilder.endBranch();
    }

}
