package com.github.br.paper.airplane.ecs.system;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.github.br.paper.airplane.ecs.component.Box2dComponent;
import com.github.br.paper.airplane.ecs.component.Mappers;
import com.github.br.paper.airplane.ecs.component.SimpleTweenComponent;
import com.github.br.paper.airplane.ecs.component.TransformComponent;

public class SimpleTweenSystem extends IteratingSystem {

    private final Mappers mappers;

    public SimpleTweenSystem(Mappers mappers) {
        super(Family.all(TransformComponent.class, SimpleTweenComponent.class).get());
        this.mappers = mappers;
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        TransformComponent transformComponent = mappers.transformMapper.get(entity);
        SimpleTweenComponent simpleTweenComponent = mappers.simpleTweenMapper.get(entity);

        Box2dComponent box2dComponent = mappers.box2dMapper.get(entity);
        if (box2dComponent != null) {
            throw new IllegalArgumentException("Box2dComponent or SimpleTweenComponent must be null");
        }

        simpleTweenComponent.velocity.add(simpleTweenComponent.acceleration);
        transformComponent.position.x += simpleTweenComponent.velocity.x * deltaTime;
        transformComponent.position.y += simpleTweenComponent.velocity.y * deltaTime;
    }

}
