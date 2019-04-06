package com.packt.flappeebee.model.scripts;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.MathUtils;
import com.github.br.ecs.simple.engine.EcsScript;
import com.github.br.ecs.simple.system.physics.PhysicsComponent;
import com.github.br.ecs.simple.system.transform.TransformComponent;
import com.github.br.ecs.simple.utils.ViewHelper;
import com.packt.flappeebee.model.GameConstants;

/**
 * Created by user on 16.04.2017.
 */
public class CrabScript extends EcsScript {

    private TransformComponent transform;
    private PhysicsComponent physics;


    @Override
    public void init() {
        transform = getComponent(TransformComponent.class);
        physics = getComponent(PhysicsComponent.class);
    }

    @Override
    public void update(float delta) {
        blockFlappeeLeavingTheWorld();
        if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
            flyUp();
        }
        if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            physics.movement.x = 50 * delta;
        } else if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            physics.movement.x = -50 * delta;
        } else {
            physics.movement.x = 0;
        }

    }

    private void blockFlappeeLeavingTheWorld() {
        if (transform.position.y <= 0 || transform.position.y >= ViewHelper.WORLD_HEIGHT) {
            physics.movement.y = 0;
        }
        transform.position.y = MathUtils.clamp(transform.position.y, 0, ViewHelper.WORLD_HEIGHT);
    }

    public void flyUp() {
        physics.movement.add(GameConstants.CRAB_DIVE_ACCEL_ACCEL);
    }

}
