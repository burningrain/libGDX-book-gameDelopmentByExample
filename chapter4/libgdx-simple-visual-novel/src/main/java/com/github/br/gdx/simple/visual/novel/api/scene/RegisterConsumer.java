package com.github.br.gdx.simple.visual.novel.api.scene;

import com.github.br.gdx.simple.visual.novel.api.context.UserContext;
import com.github.br.gdx.simple.visual.novel.api.screen.ScreenManager;

public interface RegisterConsumer<UC extends UserContext, SM extends ScreenManager> {

    void consume(NodeRegistrationBuilder<UC, SM> builder);

}
