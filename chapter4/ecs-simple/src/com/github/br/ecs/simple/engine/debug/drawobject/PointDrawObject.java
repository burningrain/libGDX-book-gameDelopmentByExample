package com.github.br.ecs.simple.engine.debug.drawobject;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.github.br.ecs.simple.engine.debug.LibGdxPanel;
import com.github.br.ecs.simple.engine.debug.data.PointData;

/**
 * Created by user on 07.04.2018.
 */
public class PointDrawObject implements DebugDrawObject<PointData> {

    @Override
    public void draw(ShapeRenderer shapeRenderer, SpriteBatch spriteBatch, LibGdxPanel panel, PointData data) {
        shapeRenderer.setColor(Color.YELLOW);

        shapeRenderer.line(data.getX() - 2, data.getY() - 2, data.getX() + 2, data.getY() + 2);
        shapeRenderer.line(data.getX() - 2, data.getY() + 2, data.getX() + 2, data.getY() - 2);
    }
}
