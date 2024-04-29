package com.github.br.gdx.simple.visual.novel.impl;

import com.github.br.gdx.simple.visual.novel.api.ElementId;
import com.github.br.gdx.simple.visual.novel.api.context.UserContext;
import com.github.br.gdx.simple.visual.novel.api.node.CompositeNode;
import com.github.br.gdx.simple.visual.novel.api.node.NodeType;
import com.github.br.gdx.simple.visual.novel.api.node.NodeVisitor;
import com.github.br.gdx.simple.visual.novel.inner.SceneLinkNode;

public class CustomNodeVisitor implements NodeVisitor<CustomNodeVisitor> {

    public void visit(ElementId sceneId, ElementId nodeId, NodeType nodeType, TestNode<?> node) {
        System.out.println("scene: " + sceneId + "\nnodeId: " + nodeId +  "\ntestNode: " + node + "\n");
    }

    public void visit(ElementId sceneId, ElementId nodeId, NodeType nodeType, TestNode2<?> node) {
        System.out.println("scene: " + sceneId + "\nnodeId: " + nodeId +  "\ntestNode2: " + node + "\n");
    }

    public void visit(ElementId sceneId, ElementId nodeId, NodeType nodeType, TestNode3<?> node) {
        System.out.println("scene: " + sceneId + "\nnodeId: " + nodeId +  "\ntestNode3: " + node + "\n");
    }

    @Override
    public void visit(ElementId sceneId, ElementId nodeId, NodeType nodeType, SceneLinkNode<?, CustomNodeVisitor> node) {
        System.out.println("scene: " + sceneId + "\nnodeId: " + nodeId +  "\nscene link: " + node + "\n");
    }

    @Override
    public void visit(ElementId sceneId, ElementId nodeId, NodeType nodeType, CompositeNode<?, CustomNodeVisitor> compositeNode) {
        System.out.println("scene: " + sceneId + "\nnodeId: " + nodeId +  "\ncomposite node: " + compositeNode + "\n");
    }

    public <T extends UserContext> void visit(ElementId sceneId, ElementId nodeId, NodeType nodeType, TestErrorNode node) {
        System.out.println("scene: " + sceneId + "\nnodeId: " + nodeId +  "\nerror node: " + node + "\n");
    }

    public <T extends UserContext> void visit(ElementId sceneId, ElementId nodeId, NodeType nodeType, TestVizNode node) {
        System.out.println("scene: " + sceneId + "\nnodeId: " + nodeId +  "\ntestNode: " + node + "\n");
    }

}
