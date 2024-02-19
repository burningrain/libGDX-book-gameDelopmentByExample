package com.github.br.gdx.simple.visual.novel.api.plot.visitor.viz.data;

import com.github.br.gdx.simple.visual.novel.utils.Utils;
import com.github.br.gdx.simple.visual.novel.api.ElementId;
import com.github.br.gdx.simple.visual.novel.api.node.CompositeNode;
import com.github.br.gdx.simple.visual.novel.api.node.Node;
import com.github.br.gdx.simple.visual.novel.api.scene.Edge;
import com.github.br.gdx.simple.visual.novel.inner.SceneLinkNode;

import java.util.ArrayList;
import java.util.List;

public class NodeElementVizData {

    private final ElementId nodeId;
    private final Node<?, ?> node;
    private final List<Edge<?>> edges = new ArrayList<>(3);

    public NodeElementVizData(ElementId nodeId, Node<?, ?> node) {
        this.nodeId = Utils.checkNotNull(nodeId, "nodeId");
        this.node = Utils.checkNotNull(node, "node");
    }

    public ElementId getNodeId() {
        return nodeId;
    }

    public List<Edge<?>> getEdges() {
        return edges;
    }

    public Node<?, ?> getNode() {
        return node;
    }

}
