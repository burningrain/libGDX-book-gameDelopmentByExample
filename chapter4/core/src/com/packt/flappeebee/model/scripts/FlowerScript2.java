package com.packt.flappeebee.model.scripts;

import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.github.br.ecs.simple.engine.EcsScript;
import com.github.br.ecs.simple.system.animation.AnimationController;
import com.github.br.ecs.simple.system.animation.AnimationComponent;
import com.github.br.ecs.simple.system.physics.PhysicsComponent;
import com.github.br.ecs.simple.system.transform.TransformComponent;

/**
 * Created by user on 28.03.2017.
 */
public class FlowerScript2 extends EcsScript {

    private TransformComponent transform;
    private PhysicsComponent physics;
    private AnimationComponent animation;
    private AnimationController animator;

    private Rectangle flower;
    private float count = 0f;

    @Override
    public void init() {
        transform = getComponent(TransformComponent.class);
        physics = getComponent(PhysicsComponent.class);
        animation = getComponent(AnimationComponent.class);
        animator = animation.controller;
        animator.getCurrentAnimator().gotoAndStop(5);

        flower = (Rectangle) physics.shape;
    }

    @Override
    public void update(float delta) {
        flower.x += 10*MathUtils.cos(count);
        flower.y += 10*MathUtils.sin(count);
        count+=0.1;

        blockFlappeeLeavingTheWorld();
    }

    private void blockFlappeeLeavingTheWorld() {
        if(transform.position.y <= 5){
            physics.movement.y = 10;
        }
    }
}
