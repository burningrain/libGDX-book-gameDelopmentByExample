package com.github.br.gdx.simple.visual.novel.viz;

import com.github.br.gdx.simple.visual.novel.api.ElementId;
import com.github.br.gdx.simple.visual.novel.api.node.Node;
import com.github.br.gdx.simple.visual.novel.api.node.NodeType;
import com.github.br.gdx.simple.visual.novel.api.node.NodeVisitor;
import com.github.br.gdx.simple.visual.novel.utils.Utils;

public class NodeWrapperViz<T extends NodeVisitor> {

    public final ElementId sceneId;
    public final ElementId nodeId;
    public final Node<?, T> node;
    public final NodeType nodeType;

    private NodeWrapperViz(ElementId sceneId, ElementId nodeId, Node<?, T> node, NodeType nodeType) {
        this.sceneId = Utils.checkNotNull(sceneId, "sceneId");
        this.nodeId = Utils.checkNotNull(nodeId, "nodeId");
        this.node = Utils.checkNotNull(node, "node");
        this.nodeType = Utils.checkNotNull(nodeType, "nodeType");
    }

    public static <T extends NodeVisitor> NodeWrapperViz of(ElementId sceneId, ElementId nodeId, Node<?, T> node, NodeType nodeType) {
        return new NodeWrapperViz(sceneId, nodeId, node, nodeType);
    }

}
