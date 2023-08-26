package com.github.br.gdx.simple.visual.novel.api.plot;

import com.github.br.gdx.simple.visual.novel.api.ElementId;
import com.github.br.gdx.simple.visual.novel.api.context.UserContext;
import com.github.br.gdx.simple.visual.novel.api.node.NodeVisitor;
import com.github.br.gdx.simple.visual.novel.api.scene.Scene;

import java.util.Collection;

public interface SceneManager<UC extends UserContext, V extends NodeVisitor<?>> {

    Scene<UC, V> getScene(ElementId nextSceneId);

    Collection<ElementId> getSceneIds();

}
