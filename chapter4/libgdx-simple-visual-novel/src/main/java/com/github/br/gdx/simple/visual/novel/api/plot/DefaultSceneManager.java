package com.github.br.gdx.simple.visual.novel.api.plot;

import com.github.br.gdx.simple.visual.novel.utils.Utils;
import com.github.br.gdx.simple.visual.novel.api.ElementId;
import com.github.br.gdx.simple.visual.novel.api.context.UserContext;
import com.github.br.gdx.simple.visual.novel.api.node.NodeVisitor;
import com.github.br.gdx.simple.visual.novel.api.scene.Scene;

import java.util.Collection;
import java.util.HashMap;

public class DefaultSceneManager<UC extends UserContext, V extends NodeVisitor<?>> implements SceneManager<UC, V> {

    private final HashMap<ElementId, SceneSupplier<UC, V>> scenes = new HashMap<>();

    public DefaultSceneManager<UC, V> addScene(ElementId elementId, SceneSupplier<UC, V> supplier) {
        Utils.checkNotNull(elementId, "elementId");
        Utils.checkNotNull(supplier, "supplier");
        scenes.put(elementId, supplier);

        return this;
    }

    @Override
    public Scene<UC, V> getScene(ElementId nextSceneId) {
        SceneSupplier<UC, V> sceneSupplier = Utils.checkNotNull(scenes.get(nextSceneId), "nextSceneId=" + nextSceneId);
        return Utils.checkNotNull(sceneSupplier.get(), "nextSceneId=" + nextSceneId);
    }

    @Override
    public Collection<ElementId> getSceneIds() {
        return scenes.keySet();
    }

}
