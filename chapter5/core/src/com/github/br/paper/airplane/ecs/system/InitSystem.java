package com.github.br.paper.airplane.ecs.system;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.github.br.paper.airplane.ecs.component.*;

public class InitSystem extends IteratingSystem {

    private final Mappers mappers;

    public InitSystem(Mappers mappers) {
        super(Family.all(TransformComponent.class, Box2dComponent.class, InitComponent.class).get());
        this.mappers = mappers;
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        //TODO вся эта система с компонентом - одна большая кривота. уже 2 обязанности!

        InitComponent initComponent = mappers.initMapper.get(entity);
        // если только что создали стену, то присваиваем ей скорость.
        // Это не делается при создании, так как body не существует. Кривота, конечно.
        if (initComponent.isNew) {
            Box2dComponent box2dComponent = mappers.box2dMapper.get(entity);
            if (box2dComponent.body != null) {
                box2dComponent.body.setLinearVelocity(initComponent.velocity);
                initComponent.isNew = false;
            }

            ScriptComponent scriptComponent = mappers.scriptMapper.get(entity);
            if (scriptComponent != null) {
                Script[] scripts = scriptComponent.scripts;
                if (scripts != null) {
                    for (Script script : scripts) {
                        script.setMappers(mappers);
                    }
                }
            }
        }
    }

}
