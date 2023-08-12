package com.github.br.gdx.simple.visual.novel.api.scene;

import com.github.br.gdx.simple.visual.novel.api.ElementId;
import com.github.br.gdx.simple.visual.novel.api.context.UserContext;
import com.github.br.gdx.simple.visual.novel.api.node.Node;
import com.github.br.gdx.simple.visual.novel.api.screen.ScreenManager;

public interface NodeRegistrationBuilder<UC extends UserContext, SM extends ScreenManager> {

    ElementId registerNode(Node<UC, SM> node);

    ElementId registerNode(ElementId nodeId, Node<UC, SM> node);

}
