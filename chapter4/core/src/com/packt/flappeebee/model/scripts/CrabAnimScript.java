package com.packt.flappeebee.model.scripts;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.github.br.ecs.simple.engine.EcsScript;
import com.github.br.ecs.simple.system.animation.AnimationComponent;
import com.github.br.ecs.simple.system.physics.PhysicsComponent;
import com.github.br.ecs.simple.system.render.RendererComponent;
import com.github.br.gdx.simple.animation.component.SimpleAnimatorUtils;
import com.github.br.gdx.simple.animation.fsm.FsmContext;
import com.github.br.simple.input.controller.ControllerProxy;

/**
 * Created by user on 17.04.2017.
 */
public class CrabAnimScript extends EcsScript {

    public static final String IDLE = "IDLE";
    public static final String JUMP = "JUMP";
    public static final String MOVEMENT = "MOVEMENT";
    public static final String ATTACK = "ATTACK";
    public static final String FLY = "FLY";


    private PhysicsComponent physics;
    private RendererComponent renderer;
    private AnimationComponent animation;

    private boolean attack = false;
    private boolean jump = false;

    @Override
    public void init() {
        physics = getComponent(PhysicsComponent.class);
        renderer = getComponent(RendererComponent.class);
        animation = getComponent(AnimationComponent.class);
    }

    @Override
    public void dispose() {
        physics = null;
        renderer = null;
        animation = null;
    }

    @Override
    public void update(float delta) {
        ControllerProxy controller = ControllerProxy.INSTANCE;
        FsmContext context = animation.simpleAnimationComponent.fsmContext;
        if ((Gdx.input.isKeyPressed(Input.Keys.ENTER) || controller.getButton(controller.getMapping().buttonB)) && !attack) {
            attack = true;
            context.update(ATTACK, true);
        }
        if ((Gdx.input.isKeyPressed(Input.Keys.SPACE) || controller.getButton(controller.getMapping().buttonA)) && !jump) {
            jump = true;
            context.update(JUMP, true);
        }

        if (physics.movement.x < 0) {
            renderer.flipX = true;
        } else {
            renderer.flipX = false;
        }
        context.update(MOVEMENT, Math.abs(physics.movement.x));

        if (physics.movement.y < -0.3) {
            context.update(FLY, true);
        } else if (physics.movement.y == 0) {
            context.update(FLY, false);
        }

        if (ATTACK.equals(context.getCurrentState())) {
            if (SimpleAnimatorUtils.isAnimationFinished(animation.simpleAnimationComponent.animatorDynamicPart)) {
                attack = false;
                context.update(ATTACK, false);
            }
        }
        if (JUMP.equals(context.getCurrentState())) {
            if (SimpleAnimatorUtils.isAnimationFinished(animation.simpleAnimationComponent.animatorDynamicPart)) {
                jump = false;
                context.update(JUMP, false);
                context.update(FLY, true);
            }
        }

    }
}
