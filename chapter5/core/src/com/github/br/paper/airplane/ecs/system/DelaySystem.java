package com.github.br.paper.airplane.ecs.system;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.github.br.paper.airplane.ecs.component.DelayComponent;
import com.github.br.paper.airplane.ecs.component.Mappers;

public class DelaySystem extends IteratingSystem {

    private final Mappers mappers;

    public DelaySystem(Mappers mappers) {
        super(Family.all(DelayComponent.class).get());
        this.mappers = mappers;
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        DelayComponent delayComponent = mappers.delayMapper.get(entity);
        delayComponent.currentTime -= deltaTime;
        if (delayComponent.currentTime <= 0) {
            delayComponent.executeOnFinish.run();
            delayComponent.reset();
            entity.remove(DelayComponent.class);
        }
    }

}
