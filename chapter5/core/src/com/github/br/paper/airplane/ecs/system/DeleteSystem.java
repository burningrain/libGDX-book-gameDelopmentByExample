package com.github.br.paper.airplane.ecs.system;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.github.br.paper.airplane.ecs.component.*;

public class DeleteSystem extends IteratingSystem {

    private final Mappers mappers;

    public DeleteSystem(Mappers mappers) {
        super(Family.all(TransformComponent.class, Box2dComponent.class, InitComponent.class).get());
        this.mappers = mappers;
    }

    @Override
    protected void processEntity(Entity entity, float delta) {
        // удаляем компонент, если вышел за границу экрана
        TransformComponent transformComponent = mappers.transformMapper.get(entity);
        if (transformComponent.position.x + transformComponent.width < -5) {
            entity.add(new DestroyComponent());
        }

        //TODO здесь только левая граница, а остальные?!
    }

}
