package com.github.br.ecs.simple.engine.debug.drawobject;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.github.br.ecs.simple.engine.debug.LibGdxPanel;
import com.github.br.ecs.simple.engine.debug.data.DebugData;

/**
 * Created by user on 07.04.2018.
 */
public interface DebugDrawObject<D extends DebugData> {

    Skin DEFAULT_SKIN = new Skin(Gdx.files.internal("uiskin.json"));

    void draw(ShapeRenderer shapeRenderer, SpriteBatch spriteBatch, LibGdxPanel panel, D data);

}
