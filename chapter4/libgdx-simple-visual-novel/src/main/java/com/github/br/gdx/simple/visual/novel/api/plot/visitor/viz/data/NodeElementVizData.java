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

    public final ElementId nodeId;

    public NodeElementType type;
    public ElementId sceneLinkId;
    public List<NodeElementVizData> innerCompositeNodes;

    public boolean isVisited;
    public final List<Edge<?>> edges = new ArrayList<>();

    public NodeElementVizData(ElementId nodeId, Node<?, ?> node) {
        this.nodeId = Utils.checkNotNull(nodeId, "nodeId");

        // scene link
        if (node instanceof SceneLinkNode) {
            this.type = NodeElementType.SCENE_LINK;
            SceneLinkNode sceneLinkNode = (SceneLinkNode) node;
            this.sceneLinkId = sceneLinkNode.getSceneTitle();
        } else if (node instanceof CompositeNode) {
            // composite
            this.type = NodeElementType.COMPOSITE_NODE;
            innerCompositeNodes = new ArrayList<>();

            CompositeNode compositeNode = (CompositeNode) node;
            for (Node compositeInnerNode : compositeNode.getNodes()) {
                innerCompositeNodes.add(new NodeElementVizData(
                        // todo необходимо разделить явно понятие "название" и "идентификатор"
                        // todo здесь использовать "название"
                        ElementId.of(compositeInnerNode.getClass().getName()),
                        compositeInnerNode
                ));
            }
        } else {
            this.type = NodeElementType.SIMPLE_NODE;
        }
    }

}
