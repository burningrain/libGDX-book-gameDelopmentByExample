package com.github.br.gdx.simple.visual.novel.api.scene;

import com.github.br.gdx.simple.visual.novel.Utils;
import com.github.br.gdx.simple.visual.novel.api.ElementId;
import com.github.br.gdx.simple.visual.novel.api.Pair;
import com.github.br.gdx.simple.visual.novel.api.context.UserContext;
import com.github.br.gdx.simple.visual.novel.api.edge.EmptyPredicate;
import com.github.br.gdx.simple.visual.novel.api.edge.Predicate;
import com.github.br.gdx.simple.visual.novel.api.node.NodeVisitor;

public class SceneEdgeBuilder<UC extends UserContext, V extends NodeVisitor<?>> {

    private final SceneNodeBuilder<UC, V> nodeBuilder;

    public SceneEdgeBuilder(SceneNodeBuilder<UC, V> nodeBuilder) {
        this.nodeBuilder = nodeBuilder;
    }

    public SceneNodeBuilder<UC, V> to(ElementId nodeId) {
        return this.to(nodeId, EmptyPredicate.<UC>empty());
    }

    public SceneNodeBuilder<UC, V> to(ElementId nodeId, Predicate<UC> predicate) {
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

    public SceneNodeBuilder<UC, V> to(Pair<ElementId, Predicate<UC>>... pares) {
        for (Pair<ElementId, Predicate<UC>> pair : pares) {
            this.to(pair.getFirst(), pair.getSecond());
        }
        return nodeBuilder;
    }

    public SceneNodeBuilder<UC, V> to() {
        nodeBuilder.lazyBinding = true;
        return nodeBuilder;
    }

    public SceneNodeBuilder<UC, V> end() {
        return nodeBuilder.endBranch();
    }

}
