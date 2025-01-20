package com.github.br.paper.airplane.ecs.system;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.github.br.paper.airplane.ecs.component.Box2dComponent;
import com.github.br.paper.airplane.ecs.component.DestroyedComponent;
import com.github.br.paper.airplane.ecs.component.Mappers;
import com.github.br.paper.airplane.ecs.system.physics.PhysicsUtils;

public class DestroySystem extends IteratingSystem {

    private final Mappers mappers;
    private final PhysicsUtils physicsUtils;

    public DestroySystem(Mappers mappers, PhysicsUtils physicsUtils) {
        super(Family.all(DestroyedComponent.class).get());
        this.mappers = mappers;
        this.physicsUtils = physicsUtils;
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        destroyObject(entity);
        getEngine().removeEntity(entity);
    }

    public void destroyObject(Entity entity) {
        Box2dComponent box2dComponent = mappers.box2dMapper.get(entity);
        if (box2dComponent != null) {
            physicsUtils.destroyObject(box2dComponent);
        }
    }

}
