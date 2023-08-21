package com.github.br.gdx.simple.visual.novel.api.plot;

import com.github.br.gdx.simple.visual.novel.api.ElementId;
import com.github.br.gdx.simple.visual.novel.api.context.UserContext;
import com.github.br.gdx.simple.visual.novel.api.scene.Scene;

public interface SceneManager<UC extends UserContext> {

    Scene<UC> getScene(ElementId nextSceneId);

}
