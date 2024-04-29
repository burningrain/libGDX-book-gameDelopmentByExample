package com.github.br.gdx.simple.visual.novel.api.plot.visitor;

import com.github.br.gdx.simple.visual.novel.api.ElementId;
import com.github.br.gdx.simple.visual.novel.api.context.CurrentState;
import com.github.br.gdx.simple.visual.novel.api.context.UserContext;
import com.github.br.gdx.simple.visual.novel.api.node.Node;
import com.github.br.gdx.simple.visual.novel.api.node.NodeType;
import com.github.br.gdx.simple.visual.novel.api.node.NodeVisitor;
import com.github.br.gdx.simple.visual.novel.api.scene.Edge;

import java.util.List;

public interface PlotVisitor<V extends NodeVisitor> {

    void visitNode(ElementId sceneId, ElementId nodeId, Node<?, V> node, NodeType nodeType);

    void visitEdge(ElementId sceneId, ElementId nodeId, Edge<?> node);

    void visitBeginNodeId(ElementId sceneId, ElementId beginNodeId);

    void visitBeginSceneId(ElementId sceneId);

    void visitCurrentNodeId(ElementId sceneId, ElementId nodeId, String currentNodeMessage);

    void visitPlotPath(List<CurrentState> path);

    void visitException(Exception ex);

    void setUserContext(UserContext userContext);

    String buildString();

}
