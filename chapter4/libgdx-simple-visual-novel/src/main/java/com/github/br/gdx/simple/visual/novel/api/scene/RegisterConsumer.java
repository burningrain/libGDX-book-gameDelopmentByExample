package com.github.br.gdx.simple.visual.novel.api.scene;

import com.github.br.gdx.simple.visual.novel.api.context.UserContext;
import com.github.br.gdx.simple.visual.novel.api.node.NodeVisitor;

public interface RegisterConsumer<UC extends UserContext, V extends NodeVisitor<?>> {

    void consume(NodeRegistrationBuilder<UC, V> builder);

}
