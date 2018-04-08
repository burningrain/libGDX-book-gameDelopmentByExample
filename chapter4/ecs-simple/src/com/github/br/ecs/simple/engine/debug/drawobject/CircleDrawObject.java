package com.github.br.ecs.simple.engine.debug.drawobject;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.github.br.ecs.simple.engine.debug.LibGdxPanel;
import com.github.br.ecs.simple.engine.debug.data.CircleData;

/**
 * Created by user on 07.04.2018.
 */
public class CircleDrawObject implements DebugDrawObject<CircleData> {


    @Override
    public void draw(ShapeRenderer shapeRenderer, SpriteBatch spriteBatch, LibGdxPanel panel, CircleData data) {
        shapeRenderer.setColor(Color.GOLD);

        shapeRenderer.circle(data.getX() + data.getRadius(), data.getY() + data.getRadius(), data.getRadius());
        shapeRenderer.line(data.getX() + data.getRadius(), data.getY() + data.getRadius(),
                (data.getX() + data.getRadius()) + data.getRadius() * MathUtils.cosDeg(data.getDegree()), data.getY() +data.getRadius() * MathUtils.sinDeg(data.getDegree()));
    }

}
