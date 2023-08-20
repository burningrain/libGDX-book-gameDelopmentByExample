package com.github.br.gdx.simple.visual.novel.api.scene;

import com.github.br.gdx.simple.visual.novel.Utils;
import com.github.br.gdx.simple.visual.novel.api.ElementId;
import com.github.br.gdx.simple.visual.novel.api.Pair;
import com.github.br.gdx.simple.visual.novel.api.context.UserContext;
import com.github.br.gdx.simple.visual.novel.api.edge.EmptyPredicate;
import com.github.br.gdx.simple.visual.novel.api.edge.Predicate;
import com.github.br.gdx.simple.visual.novel.api.screen.ScreenManager;

public class SceneEdgeBuilder<UC extends UserContext, SM extends ScreenManager> {

    private final SceneNodeBuilder<UC, SM> nodeBuilder;

    public SceneEdgeBuilder(SceneNodeBuilder<UC, SM> nodeBuilder) {
        this.nodeBuilder = nodeBuilder;
    }

    public SceneNodeBuilder<UC, SM> to(ElementId nodeId) {
        return this.to(nodeId, EmptyPredicate.<UC, SM>empty());
    }

    public SceneNodeBuilder<UC, SM> to(ElementId nodeId, Predicate<UC, SM> predicate) {
        Utils.checkNotNull(nodeId, "nodeId");
        Utils.checkNotNull(nodeBuilder.currentNodeId, "prevNodeId");

        Edge<UC, SM> edge = new Edge<>(nodeBuilder.currentNodeId, nodeId, predicate);
        if(nodeBuilder.edges.contains(edge)) {
            // TODO в логгер вывести warning о попытке переопределить существующее ребро
            return nodeBuilder;
        }

        nodeBuilder.edges.add(edge);
        return nodeBuilder;
    }

    public SceneNodeBuilder<UC, SM> to(Pair<ElementId, Predicate<UC, SM>>... pares) {
        for (Pair<ElementId, Predicate<UC, SM>> pair : pares) {
            this.to(pair.getFirst(), pair.getSecond());
        }
        return nodeBuilder;
    }

    public SceneNodeBuilder<UC, SM> to() {
        nodeBuilder.lazyBinding = true;
        return nodeBuilder;
    }

    public SceneNodeBuilder<UC, SM> end() {
        return nodeBuilder.endBranch();
    }

}
