package com.github.br.gdx.simple.visual.novel.api.plot;

import com.github.br.gdx.simple.visual.novel.api.context.UserContext;
import com.github.br.gdx.simple.visual.novel.api.scene.Scene;

public interface SceneSupplier<UC extends UserContext> {

    Scene<UC> get();

}
