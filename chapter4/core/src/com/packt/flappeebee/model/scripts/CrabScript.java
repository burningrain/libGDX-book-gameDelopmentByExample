package com.packt.flappeebee.model.scripts;

import com.badlogic.gdx.math.Vector2;
import com.github.br.ecs.simple.engine.EcsScript;
import com.github.br.ecs.simple.system.render.RendererComponent;
import com.github.br.ecs.simple.system.transform.TransformComponent;
import com.packt.flappeebee.action.HeroActions;
import com.packt.flappeebee.model.LayerEnum;
import com.packt.flappeebee.screen.level.level.gameloop.GameLoopManager;
import com.packt.flappeebee.screen.level.level.physics.InputComponent;
import com.packt.flappeebee.screen.level.level.physics.PhysicsComponent;

/**
 * Created by user on 16.04.2017.
 */
public class CrabScript extends EcsScript {

    private TransformComponent transform;
    private PhysicsComponent physics;

    private RendererComponent rendererComponent;
    private InputComponent userInput;

    @Override
    public void init() {
        transform = getComponent(TransformComponent.class);
        physics = getComponent(PhysicsComponent.class);
        rendererComponent = getComponent(RendererComponent.class);
        userInput = getComponent(InputComponent.class);
    }

    @Override
    public void dispose() {
        transform = null;
        physics = null;
    }

    @Override
    public void update(float delta) {
        if (userInput.inputActions.getAction(HeroActions.JUMP)) {
            physics.velocity.add(getJumpVelocity());
        }
        if (userInput.inputActions.getAction(HeroActions.MOVE_RIGHT)) {
            physics.velocity.x = getVelocityX();
        } else if (userInput.inputActions.getAction(HeroActions.MOVE_LEFT)) {
            physics.velocity.x = -getVelocityX();
        } else {
            physics.velocity.x = 0;
        }

        if (userInput.inputActions.getAction(HeroActions.BLINK_ON)) {
            rendererComponent.newLayerTitle = LayerEnum.FRONT_EFFECTS.name();
        }
        if (userInput.inputActions.getAction(HeroActions.BLINK_OFF)) {
            rendererComponent.newLayerTitle = LayerEnum.MAIN_LAYER.name();
        }
    }

    private float getVelocityX() {
        if (GameLoopManager.version == 1) {
            return 2f;
        }
        return 200f;
    }

    private Vector2 getJumpVelocity() {
        if (GameLoopManager.version == 1) {
            return new Vector2(0, 10.5f);
        }
        return new Vector2(0, 1000.5f);
    }

}
