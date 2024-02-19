package com.github.br.gdx.simple.visual.novel.api.plot.visitor.viz;

import com.github.br.gdx.simple.visual.novel.api.ElementId;
import com.github.br.gdx.simple.visual.novel.api.node.Node;
import com.github.br.gdx.simple.visual.novel.api.node.NodeVisitor;
import com.github.br.gdx.simple.visual.novel.api.plot.visitor.viz.data.NodeElementVizData;
import com.github.br.gdx.simple.visual.novel.api.scene.Edge;

import java.util.LinkedHashMap;

public class SceneViz<T extends NodeVisitor> {

    private final LinkedHashMap<ElementId, NodeElementVizData> nodes = new LinkedHashMap<>();
    private final LinkedHashMap<ElementId, Edge<?>> edges = new LinkedHashMap<>();
    private ElementId beginNodeId;

    public void putNode(ElementId nodeId, Node<?, T> node) {
        nodes.put(nodeId, new NodeElementVizData(nodeId, node));
    }

    public void putEdge(ElementId nodeId, Edge<?> edge) {
        edges.put(nodeId, edge);

        NodeElementVizData sourceNode = nodes.get(edge.getSourceId());
        NodeElementVizData destNode = nodes.get(edge.getDestId());

        sourceNode.getEdges().add(edge);
        destNode.getEdges().add(edge);
    }

    public void setBeginNodeId(ElementId beginNodeId) {
        this.beginNodeId = beginNodeId;
    }

    public ElementId getBeginNodeId() {
        return beginNodeId;
    }

    public LinkedHashMap<ElementId, NodeElementVizData> getNodes() {
        return nodes;
    }

    public LinkedHashMap<ElementId, Edge<?>> getEdges() {
        return edges;
    }

}
