package com.github.br.gdx.simple.visual.novel.api.plot;

import com.github.br.gdx.simple.visual.novel.api.scene.Scene;
import com.github.br.gdx.simple.visual.novel.api.screen.ScreenManager;
import com.github.br.gdx.simple.visual.novel.api.context.UserContext;

public interface SceneSupplier<UC extends UserContext, SM extends ScreenManager> {

    Scene<UC, SM> get();

}
