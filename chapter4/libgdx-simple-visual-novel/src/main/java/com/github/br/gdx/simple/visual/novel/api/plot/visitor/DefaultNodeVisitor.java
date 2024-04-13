package com.github.br.gdx.simple.visual.novel.api.plot.visitor;

import com.github.br.gdx.simple.visual.novel.api.ElementId;
import com.github.br.gdx.simple.visual.novel.api.node.CompositeNode;
import com.github.br.gdx.simple.visual.novel.api.node.NodeVisitor;
import com.github.br.gdx.simple.visual.novel.inner.SceneLinkNode;

public class DefaultNodeVisitor implements NodeVisitor {

    private String data;

    private String toString(CompositeNode<?, DefaultNodeVisitor> ucvCompositeNode) {
        return "composite_node_length=" + ucvCompositeNode.getNodes().length;
    }

    @Override
    public void visit(ElementId sceneId, ElementId nodeId, CompositeNode ucvCompositeNode) {
        this.data = "sceneId=" + sceneId + " nodeId=" + nodeId + " " + toString(ucvCompositeNode);
    }

    @Override
    public void visit(ElementId sceneId, ElementId nodeId, SceneLinkNode ucvSceneLinkNode) {
        this.data = "sceneId=" + sceneId + " nodeId=" + nodeId + " scene_link_node=" + ucvSceneLinkNode.getSceneTitle();
    }

    public String getData() {
        String result = this.data;
        this.data = null;

        return result;
    }

}
