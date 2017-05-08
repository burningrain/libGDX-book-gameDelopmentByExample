package com.packt.flappeebee.model.scripts;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.github.br.ecs.simple.EcsScript;
import com.github.br.ecs.simple.animation.AnimationController;
import com.github.br.ecs.simple.component.AnimationComponent;
import com.github.br.ecs.simple.component.PhysicsComponent;
import com.github.br.ecs.simple.component.RendererComponent;
import com.github.br.ecs.simple.fsm.FsmContext;

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
    private AnimationController controller;
    private FsmContext context;

    private boolean attack = false;
    private boolean jump = false;

    @Override
    public void init() {
        physics = getComponent(PhysicsComponent.class);
        renderer = getComponent(RendererComponent.class);
        AnimationComponent animation = getComponent(AnimationComponent.class);
        controller = animation.controller;
        context = controller.getAnimationContext();
    }

    @Override
    public void update(float delta) {
        if(Gdx.input.isKeyPressed(Input.Keys.ENTER) && !attack){
            attack = true;
            context.update(ATTACK, true);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.SPACE) && !jump) {
            jump = true;
            context.update(JUMP, true);
        }

        if(physics.movement.x < 0){
            renderer.flipX = true;
        } else {
            renderer.flipX = false;
        }
        context.update(MOVEMENT, Math.abs(physics.movement.x));

        if(physics.movement.y < -0.3){
            context.update(FLY, true);
        } else if(physics.movement.y == 0){
            context.update(FLY, false);
        }

        if(ATTACK.equals(controller.getCurrentAnimator().getName())){
            if(controller.getCurrentAnimator().isAnimationFinished()){
                attack = false;
                context.update(ATTACK, false);
            }
        }
        if(JUMP.equals(controller.getCurrentAnimator().getName())){
            if(controller.getCurrentAnimator().isAnimationFinished()){
                jump = false;
                context.update(JUMP, false);
                context.update(FLY, true);
            }
        }

    }
}
