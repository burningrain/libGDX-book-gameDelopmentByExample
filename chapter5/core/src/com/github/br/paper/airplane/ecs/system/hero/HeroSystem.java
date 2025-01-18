package com.github.br.paper.airplane.ecs.system.hero;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.github.br.paper.airplane.GameManager;
import com.github.br.paper.airplane.ecs.component.*;

public class HeroSystem extends IteratingSystem {

    private final GameManager gameManager;
    private final Mappers mappers;

    private final HeroBulletFireSubsystem heroBulletFireSubsystem;
    private final HeroAngularSubsystem heroAngularSubsystem;

    public HeroSystem(GameManager gameManager, Mappers mappers) {
        super(Family.all(HeroComponent.class, Box2dComponent.class, TransformComponent.class).get());
        this.gameManager = gameManager;
        this.mappers = mappers;

        this.heroBulletFireSubsystem = new HeroBulletFireSubsystem();
        this.heroAngularSubsystem = new HeroAngularSubsystem();
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        Box2dComponent box2dComponent = mappers.box2dMapper.get(entity);
        if (box2dComponent.body == null) {
            return;
        }

        // subsystems
        heroBulletFireSubsystem.processHero(getEngine(), gameManager, mappers, entity);
        heroAngularSubsystem.processHero(getEngine(), gameManager, mappers, entity);
    }

}
