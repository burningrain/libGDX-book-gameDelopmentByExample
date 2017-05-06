package com.packt.flappeebee.model.scripts;

import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.MathUtils;
import com.github.br.ecs.simple.EcsScript;
import com.github.br.ecs.simple.component.PhysicsComponent;
import com.github.br.ecs.simple.component.TransformComponent;
import com.github.br.ecs.simple.physics.GroupShape;
import com.github.br.ecs.simple.utils.ViewHelper;

/**
 * Created by user on 27.03.2017.
 */
public class FlowerScript extends EcsScript {

    private TransformComponent transform;
    private PhysicsComponent physics;

    private Circle flower;

    private float maxY;
    private float minY;
    private float deltaY = 0.3f;

    @Override
    public void init() {
        transform = getComponent(TransformComponent.class);
        physics = getComponent(PhysicsComponent.class);
        GroupShape groupShape = (GroupShape) physics.boundary.shape;
        flower = groupShape.getLocalPosShape("flower");
        minY = flower.y;
        maxY = flower.y + 15;
    }

    @Override
    public void update(float delta) {
        if(flower.y > maxY || flower.y < minY){
            deltaY *= -1;
        }
        flower.y += deltaY;
        blockFlappeeLeavingTheWorld();
    }

    private void blockFlappeeLeavingTheWorld() {
        if(transform.position.y <= 0 || transform.position.y >= ViewHelper.WORLD_HEIGHT){
            physics.movement.y = 0;
        }
        transform.position.y = MathUtils.clamp(transform.position.y, 0, ViewHelper.WORLD_HEIGHT);
    }


}
