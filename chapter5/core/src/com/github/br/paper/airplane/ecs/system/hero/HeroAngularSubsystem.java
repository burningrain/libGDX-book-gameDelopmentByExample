package com.github.br.paper.airplane.ecs.system.hero;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.github.br.paper.airplane.GameManager;
import com.github.br.paper.airplane.GamePlaySettings;
import com.github.br.paper.airplane.ecs.component.*;
import com.github.br.paper.airplane.ecs.component.render.ParticleEffectData;
import com.github.br.paper.airplane.ecs.component.render.RenderComponent;
import com.github.br.paper.airplane.ecs.component.render.RenderUtils;
import com.github.br.paper.airplane.gameworld.DelayComponentName;
import com.github.br.paper.airplane.gameworld.HeroEffects;

public class HeroAngularSubsystem implements HeroSybSystem {

    // TODO вынести отсюда этот код в кэш с пулами
    private ParticleEffectData heroFireEffect;
    private RenderComponent renderComponent;
    private DelayComponent delayComponent = new DelayComponent(DelayComponentName.DELETE_PARTICLE_FIRE, 0.2f, new Runnable() {
        @Override
        public void run() {
            RenderUtils.unsetEffect(renderComponent, HeroEffects.FIRE);
        }
    });
    // TODO вынести отсюда этот код в кэш с пулами

    public void processHero(
            Engine engine,
            GameManager gameManager,
            Mappers mappers,
            Entity entity
    ) {
        GamePlaySettings gamePlaySettings = gameManager.gameSettings.getGamePlaySettings();
        Box2dComponent box2dComponent = mappers.box2dMapper.get(entity);
        InputComponent inputComponent = mappers.inputMapper.get(entity);
        TransformComponent transformComponent = mappers.transformMapper.get(entity);
        HeroComponent heroComponent = mappers.heroMapper.get(entity);
        renderComponent = mappers.renderMapper.get(entity);

        // TODO создавать при старте, а не лениво
        if (heroFireEffect == null) {
            float width = transformComponent.width;
            heroFireEffect = gameManager.gameEntityFactory.createHeroFireEffect(new Vector2(-width / 2, 0));
        }

        if (inputComponent.isPressedDown) {
            // если не стреляем, значит поднимаем самолетик вверх
            if (!heroComponent.isApplyAngularImpulse()) {
                heroComponent.setApplyAngularImpulse(true);

                box2dComponent.body.applyAngularImpulse(-gamePlaySettings.getAngularImpulse(), true);
                RenderUtils.setEffect(renderComponent, heroFireEffect, HeroEffects.FIRE);
                entity.add(delayComponent);
            }
        } else {
            heroComponent.setApplyAngularImpulse(false);
        }

        box2dComponent.body.applyTorque(gamePlaySettings.getDefaultTorque(), true);
        float angle = box2dComponent.body.getAngle() * MathUtils.radiansToDegrees;
        if (angle >= gamePlaySettings.getEndAngle() || angle <= -gamePlaySettings.getEndAngle()) {
            inputComponent.turnOff();
        }
    }

}
