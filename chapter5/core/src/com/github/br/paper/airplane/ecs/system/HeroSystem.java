package com.github.br.paper.airplane.ecs.system;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.math.MathUtils;
import com.github.br.paper.airplane.GameManager;
import com.github.br.paper.airplane.GamePlaySettings;
import com.github.br.paper.airplane.ecs.component.Box2dComponent;
import com.github.br.paper.airplane.ecs.component.HeroComponent;
import com.github.br.paper.airplane.ecs.component.Mappers;

public class HeroSystem extends IteratingSystem {

    private final GameManager gameManager;
    private final Mappers mappers;

    private boolean lastState = true;

    public HeroSystem(GameManager gameManager, Mappers mappers) {
        super(Family.all(HeroComponent.class, Box2dComponent.class).get());
        this.gameManager = gameManager;
        this.mappers = mappers;
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        HeroComponent heroComponent = mappers.heroMapper.get(entity);
        Box2dComponent box2dComponent = mappers.box2dMapper.get(entity);

        GamePlaySettings gamePlaySettings = gameManager.gameSettings.getGamePlaySettings();
        if (lastState != heroComponent.isUp()) {
            heroComponent.setPitchForce(0);
            lastState = heroComponent.isUp();
        }

        if (!heroComponent.isUp()) {
            heroComponent.setPitchForce(
                    MathUtils.lerp(heroComponent.getPitchForce(), gamePlaySettings.getPitchMaxForce(), gamePlaySettings.getPitchDeltaProgress()));
        } else {
            heroComponent.setPitchForce(
                    MathUtils.lerp(heroComponent.getPitchForce(), gamePlaySettings.getPitchMinForce(), gamePlaySettings.getPitchDeltaProgress()));
        }
        float angularForce = MathUtils.clamp(heroComponent.getPitchForce(), gamePlaySettings.getPitchMinForce(), gamePlaySettings.getPitchMaxForce());
        System.out.println(angularForce);
        if (box2dComponent != null && box2dComponent.body != null) {
            box2dComponent.body.applyTorque(angularForce,true);
        }
    }
}
