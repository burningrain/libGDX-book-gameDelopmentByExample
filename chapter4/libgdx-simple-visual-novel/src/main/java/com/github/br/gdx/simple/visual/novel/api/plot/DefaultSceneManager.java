package com.github.br.gdx.simple.visual.novel.api.plot;

import com.github.br.gdx.simple.visual.novel.Utils;
import com.github.br.gdx.simple.visual.novel.api.ElementId;
import com.github.br.gdx.simple.visual.novel.api.context.UserContext;
import com.github.br.gdx.simple.visual.novel.api.scene.Scene;

import java.util.HashMap;

public class DefaultSceneManager<UC extends UserContext> implements SceneManager<UC> {

    private final HashMap<ElementId, SceneSupplier<UC>> scenes = new HashMap<>();

    public DefaultSceneManager<UC> addScene(ElementId elementId, SceneSupplier<UC> supplier) {
        Utils.checkNotNull(elementId, "elementId");
        Utils.checkNotNull(supplier, "supplier");
        scenes.put(elementId, supplier);

        return this;
    }

    @Override
    public Scene<UC> getScene(ElementId nextSceneId) {
        SceneSupplier<UC> sceneSupplier = Utils.checkNotNull(scenes.get(nextSceneId), "nextSceneId=" + nextSceneId);
        return Utils.checkNotNull(sceneSupplier.get(), "nextSceneId=" + nextSceneId);
    }


}
