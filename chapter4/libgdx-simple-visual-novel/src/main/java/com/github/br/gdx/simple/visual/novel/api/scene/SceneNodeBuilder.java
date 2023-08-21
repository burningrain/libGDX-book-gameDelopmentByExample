package com.github.br.gdx.simple.visual.novel.api.scene;

import com.github.br.gdx.simple.visual.novel.Utils;
import com.github.br.gdx.simple.visual.novel.api.ElementId;
import com.github.br.gdx.simple.visual.novel.api.context.UserContext;
import com.github.br.gdx.simple.visual.novel.api.node.Node;
import com.github.br.gdx.simple.visual.novel.graph.Graph;
import com.github.br.gdx.simple.visual.novel.inner.SceneLinkNode;

import java.util.HashSet;

public class SceneNodeBuilder<UC extends UserContext> {

    final SceneConfig config;
    final Graph<Node<UC>, Edge> graph = new Graph<>();
    final HashSet<Edge> edges = new HashSet<>();

    ElementId beginNodeId;

    ElementId prevNodeId;
    ElementId currentNodeId;

    boolean lazyBinding = false;

    private final SceneEdgeBuilder<UC> edgeBuilder;

    public SceneNodeBuilder(SceneConfig config) {
        this.config = Utils.checkNotNull(config, "config");
        edgeBuilder = new SceneEdgeBuilder<>(this);
    }


    public SceneNodeBuilder<UC> endBranch() {
        Utils.checkNotNull(currentNodeId, "currentNodeId");

        if (prevNodeId != null) {
            edges.add(new Edge<>(prevNodeId, currentNodeId));
            prevNodeId = null;
        }
        currentNodeId = null;

        return this;
    }

    public SceneEdgeBuilder<UC> node(ElementId nodeId) {
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

    public Scene<UC> build() {
        Utils.checkNotNull(beginNodeId, "beginNodeId");

        for (Edge edge : edges) {
            graph.addEdge(
                    SceneUtils.toId(config.getGeneratorEdgeId().nextId(edge.sourceId, edge.destId)),
                    SceneUtils.toId(edge.sourceId),
                    SceneUtils.toId(edge.destId),
                    new Edge(edge.sourceId, edge.destId, edge.predicate)
            );
        }

        return new Scene<UC>(config, graph, SceneUtils.toId(beginNodeId));
    }

    ElementId addNode(Node<UC> node) {
        return this.addNode(config.getGeneratorNodeId().nextId(), node);
    }

    ElementId addNode(ElementId nodeId, Node<UC> node) {
        Utils.checkNotNull(nodeId, "nodeId");
        Utils.checkNotNull(node, "node");

        graph.addNode(SceneUtils.toId(nodeId), node);

        return nodeId;
    }

    ElementId addScene(ElementId sceneId) {
        String generateId = this.config.getGeneratorNodeId().nextId().getId();
        ElementId nodeId = ElementId.of(generateId);
        this.addScene(nodeId, sceneId);

        return nodeId;
    }

    ElementId addScene(ElementId nodeId, ElementId sceneId) {
        this.addNode(nodeId, new SceneLinkNode<UC>(sceneId));

        return nodeId;
    }

}
