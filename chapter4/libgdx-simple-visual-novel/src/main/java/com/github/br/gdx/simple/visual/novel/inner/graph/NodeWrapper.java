package com.github.br.gdx.simple.visual.novel.inner.graph;

import com.github.br.gdx.simple.visual.novel.utils.Utils;
import com.github.br.gdx.simple.visual.novel.api.node.NodeType;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class NodeWrapper<N, E> {

    private final GraphElementId id;
    private final N node;
    private final NodeType nodeType;
    private final ArrayList<EdgeWrapper<N, E>> edges = new ArrayList<>();

    public NodeWrapper(GraphElementId id, N node, NodeType nodeType) {
        this.id = Utils.checkNotNull(id, "id");
        this.node = Utils.checkNotNull(node, "node");
        this.nodeType = Utils.checkNotNull(nodeType, "nodeType");
    }

    public GraphElementId getId() {
        return id;
    }

    public N getNode() {
        return node;
    }

    public NodeType getNodeType() {
        return nodeType;
    }

    public List<EdgeWrapper<N, E>> getEdges() {
        return edges;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NodeWrapper<?, ?> that = (NodeWrapper<?, ?>) o;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return id.toString();
    }
}
