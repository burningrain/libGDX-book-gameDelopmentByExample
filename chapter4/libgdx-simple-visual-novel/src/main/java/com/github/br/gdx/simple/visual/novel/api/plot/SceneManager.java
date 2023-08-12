package com.github.br.gdx.simple.visual.novel.api.plot;

import com.github.br.gdx.simple.visual.novel.api.ElementId;
import com.github.br.gdx.simple.visual.novel.api.context.UserContext;
import com.github.br.gdx.simple.visual.novel.api.scene.Scene;
import com.github.br.gdx.simple.visual.novel.api.screen.ScreenManager;

public interface SceneManager<UC extends UserContext, SC extends ScreenManager> {

    Scene<UC, SC> getScene(ElementId nextSceneId);

}
