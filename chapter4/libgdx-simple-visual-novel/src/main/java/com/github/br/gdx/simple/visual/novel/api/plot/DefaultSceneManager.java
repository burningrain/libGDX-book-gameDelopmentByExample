package com.github.br.gdx.simple.visual.novel.api.plot;

import com.badlogic.gdx.utils.ObjectMap;
import com.github.br.gdx.simple.visual.novel.Utils;
import com.github.br.gdx.simple.visual.novel.api.ElementId;
import com.github.br.gdx.simple.visual.novel.api.context.UserContext;
import com.github.br.gdx.simple.visual.novel.api.scene.Scene;
import com.github.br.gdx.simple.visual.novel.api.screen.ScreenManager;

public class DefaultSceneManager<UC extends UserContext, SC extends ScreenManager> implements SceneManager<UC, SC> {

    private final ObjectMap<ElementId, SceneSupplier<UC, SC>> scenes = new ObjectMap<>();

    public DefaultSceneManager<UC, SC> addScene(ElementId elementId, SceneSupplier<UC, SC> supplier) {
        Utils.checkNotNull(elementId, "elementId");
        Utils.checkNotNull(supplier, "supplier");
        scenes.put(elementId, supplier);

        return this;
    }

    @Override
    public Scene<UC, SC> getScene(ElementId nextSceneId) {
        SceneSupplier<UC, SC> sceneSupplier = Utils.checkNotNull(scenes.get(nextSceneId), "nextSceneId=" + nextSceneId);
        return Utils.checkNotNull(sceneSupplier.get(), "nextSceneId=" + nextSceneId);
    }


}
