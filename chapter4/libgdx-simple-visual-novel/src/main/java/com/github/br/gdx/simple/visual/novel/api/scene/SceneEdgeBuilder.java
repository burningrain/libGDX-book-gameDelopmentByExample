package com.github.br.gdx.simple.visual.novel.api.scene;

import com.github.br.gdx.simple.visual.novel.Utils;
import com.github.br.gdx.simple.visual.novel.api.ElementId;
import com.github.br.gdx.simple.visual.novel.api.context.UserContext;
import com.github.br.gdx.simple.visual.novel.api.screen.ScreenManager;

public class SceneEdgeBuilder<UC extends UserContext, SM extends ScreenManager> {

    private final SceneNodeBuilder<UC, SM> nodeBuilder;

    public SceneEdgeBuilder(SceneNodeBuilder<UC, SM> nodeBuilder) {
        this.nodeBuilder = nodeBuilder;
    }

    public SceneNodeBuilder<UC, SM> to(ElementId nodeId) {
        Utils.checkNotNull(nodeId, "nodeId");
        Utils.checkNotNull(nodeBuilder.currentNodeId, "prevNodeId");

        nodeBuilder.edges.add(new Edge(nodeBuilder.currentNodeId, nodeId));
        return nodeBuilder;
    }

    public SceneNodeBuilder<UC, SM> to(ElementId... nodeIds) {
        for (ElementId nodeId : nodeIds) {
            this.to(nodeId);
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
