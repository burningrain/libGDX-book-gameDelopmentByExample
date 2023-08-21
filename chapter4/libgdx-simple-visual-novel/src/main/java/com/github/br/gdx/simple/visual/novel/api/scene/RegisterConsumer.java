package com.github.br.gdx.simple.visual.novel.api.scene;

import com.github.br.gdx.simple.visual.novel.api.context.UserContext;

public interface RegisterConsumer<UC extends UserContext> {

    void consume(NodeRegistrationBuilder<UC> builder);

}
