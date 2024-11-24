package com.github.br.paper.airplane.ecs.component;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.physics.box2d.ContactListener;

public abstract class Script implements ContactListener {

    private Mappers mappers;

    public abstract void update(Entity entity, float deltaTime);

    public void setMappers(Mappers mappers) {
        this.mappers = mappers;
    }

    public Mappers getMappers() {
        return mappers;
    }

    public boolean isHero(Entity entity) {
        return getMappers().heroMapper.get(entity) != null;
    }

}
