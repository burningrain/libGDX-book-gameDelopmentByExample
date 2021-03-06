package com.packt.flappeebee.model.scripts;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.MathUtils;
import com.github.br.ecs.simple.engine.EcsScript;
import com.github.br.ecs.simple.system.physics.PhysicsComponent;
import com.github.br.ecs.simple.system.render.RendererComponent;
import com.github.br.ecs.simple.system.transform.TransformComponent;
import com.packt.flappeebee.model.GameConstants;
import com.packt.flappeebee.model.LayerEnum;

/**
 * Created by user on 16.04.2017.
 */
public class CrabScript extends EcsScript {

    private TransformComponent transform;
    private PhysicsComponent physics;

    private RendererComponent rendererComponent;

    @Override
    public void init() {
        transform = getComponent(TransformComponent.class);
        physics = getComponent(PhysicsComponent.class);
        rendererComponent = getComponent(RendererComponent.class);
    }

    @Override
    public void dispose() {
        transform = null;
        physics = null;
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

        if(Gdx.input.isKeyPressed(Input.Keys.F)) {
            rendererComponent.newLayerTitle = LayerEnum.FRONT_EFFECTS.name();
        }
        if(Gdx.input.isKeyPressed(Input.Keys.G)) {
            rendererComponent.newLayerTitle = LayerEnum.MAIN_LAYER.name();
        }
    }

    private void blockFlappeeLeavingTheWorld() {
        int height = Gdx.graphics.getHeight();

        if (transform.position.y <= 0 || transform.position.y >= height) {
            physics.movement.y = 0;
        }
        transform.position.y = MathUtils.clamp(transform.position.y, 0, height);
    }

    public void flyUp() {
        physics.movement.add(GameConstants.CRAB_DIVE_ACCEL_ACCEL);
    }

}
