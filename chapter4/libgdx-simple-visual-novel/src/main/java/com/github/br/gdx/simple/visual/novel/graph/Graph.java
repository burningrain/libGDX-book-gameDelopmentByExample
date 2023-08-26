package com.github.br.gdx.simple.visual.novel.graph;

import com.github.br.gdx.simple.visual.novel.Utils;
import com.github.br.gdx.simple.visual.novel.inner.GraphVisitor;

import java.util.LinkedHashMap;
import java.util.Map;

public class Graph<N, E> {

    private final LinkedHashMap<GraphElementId, NodeWrapper<N, E>> nodes = new LinkedHashMap<>();
    private final LinkedHashMap<GraphElementId, EdgeWrapper<N, E>> edges = new LinkedHashMap<>();

    public void addNode(GraphElementId elementId, N node) {
        Utils.checkNotNull(elementId, "elementId");
        Utils.checkNotNull(node, "node");

        nodes.put(elementId, new NodeWrapper<N, E>(elementId, node));
    }

    public void addEdge(GraphElementId edgeId, GraphElementId sourceId, GraphElementId destId, E edge) {
        Utils.checkNotNull(edgeId, "edgeId");
        Utils.checkNotNull(sourceId, "sourceId");
        Utils.checkNotNull(destId, "destId");
        Utils.checkNotNull(edge, "edge");

        NodeWrapper<N, E> source = Utils.checkNotNull(nodes.get(sourceId), "node sourceId='" + sourceId + "'");
        NodeWrapper<N, E> dest = Utils.checkNotNull(nodes.get(destId), "node destId='" + destId + "'");
        EdgeWrapper<N, E> edgeWrapper = new EdgeWrapper<>(edgeId, edge, source, dest);

        source.getEdges().add(edgeWrapper);
        dest.getEdges().add(edgeWrapper);
        edges.put(edgeId, edgeWrapper);
    }

    public N getNode(GraphElementId nodeId) {
        return Utils.checkNotNull(nodes.get(nodeId), "nodeId='" + nodeId + "'").getNode();
    }

    public NodeWrapper<N, E> getNodeWrapper(GraphElementId nodeId) {
        return Utils.checkNotNull(nodes.get(nodeId), "nodeId='" + nodeId + "'");
    }



    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("digraph graphname {");
        for (Map.Entry<GraphElementId, NodeWrapper<N, E>> node : nodes.entrySet()) {
            builder.append(node.getKey().getId()).append(";");
        }
        for (Map.Entry<GraphElementId, EdgeWrapper<N, E>> edge : edges.entrySet()) {
            EdgeWrapper<N, E> value = edge.getValue();
            builder.append(value.getSource().getId()).append(" -> ").append(value.getDest().getId()).append(";");
        }
        builder.append("}");

        return builder.toString();
    }

    public void accept(GraphVisitor<N, E> graphVisitor) {
        for (Map.Entry<GraphElementId, NodeWrapper<N, E>> nodesEntry : nodes.entrySet()) {
            graphVisitor.visitNode(nodesEntry.getKey(), nodesEntry.getValue().getNode());
        }
        for (Map.Entry<GraphElementId, EdgeWrapper<N, E>> edgesEntry : edges.entrySet()) {
            graphVisitor.visitEdge(edgesEntry.getKey(), edgesEntry.getValue().getEdge());
        }
    }

}
