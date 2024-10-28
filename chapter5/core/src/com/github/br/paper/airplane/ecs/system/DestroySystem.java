package com.github.br.paper.airplane.ecs.system;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.github.br.paper.airplane.ecs.component.DestroyComponent;

public class DestroySystem extends IteratingSystem {

    public DestroySystem() {
        super(Family.all(DestroyComponent.class).get());
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        getEngine().removeEntity(entity);
    }

}
