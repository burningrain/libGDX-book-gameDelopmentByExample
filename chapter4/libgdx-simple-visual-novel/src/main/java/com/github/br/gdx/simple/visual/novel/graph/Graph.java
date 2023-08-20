package com.github.br.gdx.simple.visual.novel.graph;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import com.github.br.gdx.simple.visual.novel.Utils;

public class Graph<N, E> {

    private final ObjectMap<GraphElementId, NodeWrapper<N, E>> nodes = new ObjectMap<>();
    private final ObjectMap<GraphElementId, EdgeWrapper<N, E>> edges = new ObjectMap<>();

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

    public boolean containsNode(GraphElementId nodeId) {
        return (nodes.get(nodeId) == null);
    }

    public NodeWrapper<N, E> getNodeWrapper(GraphElementId nodeId) {
        return Utils.checkNotNull(nodes.get(nodeId), "nodeId='" + nodeId + "'");
    }



    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("digraph graphname {");
        for (ObjectMap.Entry<GraphElementId, NodeWrapper<N, E>> node : nodes) {
            builder.append(node.key.getId()).append(";");
        }
        for (ObjectMap.Entry<GraphElementId, EdgeWrapper<N, E>> edge : edges) {
            EdgeWrapper<N, E> value = edge.value;
            builder.append(value.getSource().getId()).append(" -> ").append(value.getDest().getId()).append(";");
        }
        builder.append("}");

        return builder.toString();
    }

}
