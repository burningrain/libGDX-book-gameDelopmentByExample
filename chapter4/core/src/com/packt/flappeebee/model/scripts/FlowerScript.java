package com.packt.flappeebee.model.scripts;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.github.br.ecs.simple.engine.EcsScript;
import com.github.br.ecs.simple.system.physics.PhysicsComponent;
import com.github.br.ecs.simple.system.transform.TransformComponent;
import com.github.br.ecs.simple.utils.ViewHelper;

/**
 * Created by user on 27.03.2017.
 */
public class FlowerScript extends EcsScript {

    private TransformComponent transform;
    private PhysicsComponent physics;

    private Rectangle flower;

    private float maxY;
    private float minY;
    private float deltaY = 0.3f;

    @Override
    public void init() {
        transform = getComponent(TransformComponent.class);
        physics = getComponent(PhysicsComponent.class);
        flower = (Rectangle) physics.shape;
        minY = flower.y;
        maxY = flower.y + 15;
    }

    @Override
    public void dispose() {
        transform = null;
        physics = null;
        flower = null;
    }

    @Override
    public void update(float delta) {
        if (flower.y > maxY || flower.y < minY) {
            deltaY *= -1;
        }
        flower.y += deltaY;
        blockFlappeeLeavingTheWorld();
    }

    private void blockFlappeeLeavingTheWorld() {
        int height = Gdx.graphics.getHeight();
        if (transform.position.y <= 0 || transform.position.y >= height) {
            physics.movement.y = 0;
        }
        transform.position.y = MathUtils.clamp(transform.position.y, 0, height);
    }


}
