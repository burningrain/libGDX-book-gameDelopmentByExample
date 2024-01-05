package com.github.br.gdx.simple.visual.novel.api.scene;

import com.github.br.gdx.simple.visual.novel.utils.Utils;
import com.github.br.gdx.simple.visual.novel.api.ElementId;
import com.github.br.gdx.simple.visual.novel.api.context.UserContext;
import com.github.br.gdx.simple.visual.novel.api.node.Node;
import com.github.br.gdx.simple.visual.novel.api.node.NodeType;
import com.github.br.gdx.simple.visual.novel.api.node.NodeVisitor;
import com.github.br.gdx.simple.visual.novel.inner.graph.Graph;
import com.github.br.gdx.simple.visual.novel.inner.SceneLinkNode;

import java.util.HashSet;

public class SceneNodeBuilder<UC extends UserContext, V extends NodeVisitor<?>> {

    final SceneConfig config;
    final Graph<Node<UC, V>, Edge> graph = new Graph<>();
    final HashSet<Edge> edges = new HashSet<>();

    ElementId beginNodeId;

    ElementId prevNodeId;
    ElementId currentNodeId;

    boolean lazyBinding = false;

    private final SceneEdgeBuilder<UC, V> edgeBuilder;

    public SceneNodeBuilder(SceneConfig config) {
        this.config = Utils.checkNotNull(config, "config");
        edgeBuilder = new SceneEdgeBuilder<>(this);
    }


    public SceneNodeBuilder<UC, V> endBranch() {
        Utils.checkNotNull(currentNodeId, "currentNodeId");

        if (prevNodeId != null) {
            edges.add(new Edge<>(prevNodeId, currentNodeId));
            prevNodeId = null;
        }
        currentNodeId = null;

        return this;
    }

    public SceneEdgeBuilder<UC, V> node(ElementId nodeId) {
        Utils.checkNotNull(nodeId, "nodeId");

        if (beginNodeId == null) {
            beginNodeId = nodeId;
        }

        if (lazyBinding) {
            lazyBinding = false;
            edgeBuilder.to(nodeId);
        }

        prevNodeId = currentNodeId;
        currentNodeId = nodeId;

        return edgeBuilder;
    }

    public Scene<UC, V> build() {
        Utils.checkNotNull(beginNodeId, "beginNodeId");

        for (Edge edge : edges) {
            graph.addEdge(
                    SceneUtils.toId(config.getGeneratorEdgeId().nextId(edge.getSourceId(), edge.getDestId())),
                    SceneUtils.toId(edge.getSourceId()),
                    SceneUtils.toId(edge.getDestId()),
                    new Edge(edge.getSourceId(), edge.getDestId(), edge.getPredicate())
            );
        }

        return new Scene<>(config, graph, SceneUtils.toId(beginNodeId));
    }

    ElementId addNode(Node<UC, V> node, NodeType nodeType) {
        return this.addNode(config.getGeneratorNodeId().nextId(), node, nodeType);
    }

    ElementId addNode(ElementId nodeId, Node<UC, V> node, NodeType nodeType) {
        Utils.checkNotNull(nodeId, "nodeId");
        Utils.checkNotNull(node, "node");
        Utils.checkNotNull(nodeType, "nodeType");

        graph.addNode(SceneUtils.toId(nodeId), node, nodeType);

        return nodeId;
    }

    ElementId addScene(ElementId sceneId, NodeType nodeType) {
        String generateId = this.config.getGeneratorNodeId().nextId().getId();
        ElementId nodeId = ElementId.of(generateId);
        this.addScene(nodeId, sceneId, nodeType);

        return nodeId;
    }

    ElementId addScene(ElementId nodeId, ElementId sceneId, NodeType nodeType) {
        this.addNode(nodeId, new SceneLinkNode<UC, V>(sceneId), nodeType);

        return nodeId;
    }

}
