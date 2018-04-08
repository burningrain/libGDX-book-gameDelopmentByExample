package com.github.br.ecs.simple.engine.debug.drawobject;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.github.br.ecs.simple.engine.debug.LibGdxPanel;
import com.github.br.ecs.simple.engine.debug.data.RectangleData;

/**
 * Created by user on 07.04.2018.
 */
public class RectangleDrawObject implements DebugDrawObject<RectangleData> {
    @Override
    public void draw(ShapeRenderer shapeRenderer, SpriteBatch spriteBatch, LibGdxPanel panel, RectangleData data) {
        shapeRenderer.setColor(Color.GOLD);
        shapeRenderer.rect(data.getX(), data.getY(), data.getWidth(), data.getHeight());
    }
}
