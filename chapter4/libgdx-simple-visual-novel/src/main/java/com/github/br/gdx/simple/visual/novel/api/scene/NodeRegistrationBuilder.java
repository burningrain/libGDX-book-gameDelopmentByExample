package com.github.br.gdx.simple.visual.novel.api.scene;

import com.github.br.gdx.simple.visual.novel.api.ElementId;
import com.github.br.gdx.simple.visual.novel.api.context.UserContext;
import com.github.br.gdx.simple.visual.novel.api.node.Node;

public interface NodeRegistrationBuilder<UC extends UserContext> {

    ElementId registerNode(Node<UC> node);

    ElementId registerNode(ElementId nodeId, Node<UC> node);

}
