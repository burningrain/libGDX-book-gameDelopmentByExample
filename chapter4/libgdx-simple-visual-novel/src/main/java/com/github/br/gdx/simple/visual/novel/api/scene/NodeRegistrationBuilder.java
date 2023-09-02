package com.github.br.gdx.simple.visual.novel.api.scene;

import com.github.br.gdx.simple.visual.novel.api.ElementId;
import com.github.br.gdx.simple.visual.novel.api.context.UserContext;
import com.github.br.gdx.simple.visual.novel.api.node.Node;
import com.github.br.gdx.simple.visual.novel.api.node.NodeType;
import com.github.br.gdx.simple.visual.novel.api.node.NodeVisitor;

public interface NodeRegistrationBuilder<UC extends UserContext, V extends NodeVisitor<?>> {

    ElementId registerNode(Node<UC, V> node);

    ElementId registerNode(ElementId nodeId, Node<UC, V> node, NodeType nodeType);

}
