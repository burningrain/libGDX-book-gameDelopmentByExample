package com.github.br.paper.airplane.ecs.system;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.github.br.paper.airplane.ecs.component.Box2dComponent;
import com.github.br.paper.airplane.ecs.component.HeroComponent;
import com.github.br.paper.airplane.ecs.component.Mappers;

public class HeroSystem extends IteratingSystem {

    private final Mappers mappers;

    public HeroSystem(Mappers mappers) {
        super(Family.all(HeroComponent.class, Box2dComponent.class).get());
        this.mappers = mappers;
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        Box2dComponent box2dComponent = mappers.box2dMapper.get(entity);
        if (box2dComponent.body == null) {
            return;
        }

        box2dComponent.body.setTransform(
                box2dComponent.body.getPosition(),
                box2dComponent.body.getAngle() + 0.01f
        );
    }
}
