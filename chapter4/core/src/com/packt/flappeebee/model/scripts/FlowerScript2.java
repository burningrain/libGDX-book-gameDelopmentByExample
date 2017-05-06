package com.packt.flappeebee.model.scripts;

import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.MathUtils;
import com.github.br.ecs.simple.EcsScript;
import com.github.br.ecs.simple.animation.AnimationController;
import com.github.br.ecs.simple.component.AnimationComponent;
import com.github.br.ecs.simple.component.PhysicsComponent;
import com.github.br.ecs.simple.component.TransformComponent;
import com.github.br.ecs.simple.physics.GroupShape;

/**
 * Created by user on 28.03.2017.
 */
public class FlowerScript2 extends EcsScript {

    private TransformComponent transform;
    private PhysicsComponent physics;
    private AnimationComponent animation;
    private AnimationController animator;

    private Circle flower;
    private float count = 0f;

    @Override
    public void init() {
        transform = getComponent(TransformComponent.class);
        physics = getComponent(PhysicsComponent.class);
        animation = getComponent(AnimationComponent.class);
        animator = animation.controller;
        animator.getCurrentAnimator().gotoAndStop(5);

        GroupShape groupShape = (GroupShape) physics.boundary.shape;
        flower = groupShape.getLocalPosShape("flower");
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
