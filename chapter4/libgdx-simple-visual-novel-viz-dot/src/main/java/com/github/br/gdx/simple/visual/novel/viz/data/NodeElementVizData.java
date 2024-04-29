package com.github.br.gdx.simple.visual.novel.viz.data;

import com.github.br.gdx.simple.visual.novel.api.ElementId;
import com.github.br.gdx.simple.visual.novel.api.scene.Edge;
import com.github.br.gdx.simple.visual.novel.utils.Utils;
import com.github.br.gdx.simple.visual.novel.viz.NodeWrapperViz;

import java.util.ArrayList;
import java.util.List;

public class NodeElementVizData {

    private final ElementId nodeId;
    private final NodeWrapperViz node;
    private final ArrayList<Edge<?>> edges = new ArrayList<>(3);

    public NodeElementVizData(ElementId nodeId, NodeWrapperViz<?> nodeWrapper) {
        this.nodeId = Utils.checkNotNull(nodeId, "nodeId");
        this.node = Utils.checkNotNull(nodeWrapper, "node");
    }

    public ElementId getNodeId() {
        return nodeId;
    }

    public List<Edge<?>> getEdges() {
        return edges;
    }

    public NodeWrapperViz<?> getNodeWrapper() {
        return node;
    }

}
