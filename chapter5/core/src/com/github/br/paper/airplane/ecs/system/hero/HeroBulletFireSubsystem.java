package com.github.br.paper.airplane.ecs.system.hero;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.g2d.ParticleEmitter;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.github.br.paper.airplane.GameManager;
import com.github.br.paper.airplane.GamePlaySettings;
import com.github.br.paper.airplane.bullet.BulletStrategy;
import com.github.br.paper.airplane.ecs.component.*;
import com.github.br.paper.airplane.ecs.component.render.ParticleEffectData;
import com.github.br.paper.airplane.ecs.component.render.RenderComponent;
import com.github.br.paper.airplane.ecs.component.render.RenderUtils;
import com.github.br.paper.airplane.gameworld.HeroEffects;

public class HeroBulletFireSubsystem implements HeroSybSystem {

    private ParticleEffectData heroSmokeEffect; // TODO вынести отсюда этот объект в кэш с пулами
    private Vector2 smokeAnchor; //TODO см. выше

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
        RenderComponent renderComponent = mappers.renderMapper.get(entity);

        HeroComponent heroComponent = mappers.heroMapper.get(entity);
        //TODO кривота, надо не высчитывать лениво, а все-таки задавать сразу числами. Размеры-то известны!
        if (smokeAnchor == null) {
            float width = transformComponent.width;
            smokeAnchor = new Vector2(-width / 2, 0);
        }

        // либо стреляем, либо просто поднимаем вверх. Два действия повешены на тапы, поэтому так.
        // два быстрых тапа - выстрел. Один тап и удержание - подъем вверх
        if (inputComponent.isFire) {
            short bulletCount = heroComponent.getBulletCount();
            BulletStrategy bulletStrategy = gameManager.bulletFactory.getBulletStrategy(heroComponent.getBulletType());
            if (bulletCount == 0 || !bulletStrategy.isBulletsEnough(bulletCount)) {
                // нечем стрелять
                return;
            }

            heroComponent.setBulletCount(bulletStrategy.reduceBullets(heroComponent.getBulletCount()));
            Entity[] bullets = bulletStrategy.createBullet(engine, transformComponent);
            for (Entity bullet : bullets) {
                engine.addEntity(bullet);
            }
            inputComponent.isFire = false;
            unsetSmokeEffect(engine, gameManager, renderComponent, transformComponent, smokeAnchor);
        } else if (inputComponent.isPressedDown) {
            // если не стреляем, значит поднимаем самолетик вверх
            box2dComponent.body.applyForce(gamePlaySettings.getUpForce(), box2dComponent.body.getWorldCenter(), true);

            // TODO создавать при старте, а не лениво
            if (heroSmokeEffect == null) {
                heroSmokeEffect = gameManager.gameEntityFactory.createHeroSmokeFastFadeOutEffect(smokeAnchor);
            }

            setSmokeEffect(renderComponent);
        } else if (inputComponent.isPressedUp) {
            unsetSmokeEffect(engine, gameManager, renderComponent, transformComponent, smokeAnchor);
        }
    }

    private void setSmokeEffect(RenderComponent renderComponent) {
        RenderUtils.setEffect(renderComponent, heroSmokeEffect, HeroEffects.SMOKE);

        ParticleEffectData smokeEffect = renderComponent.effectData[HeroEffects.SMOKE];
        Array<ParticleEmitter> emitters = smokeEffect.particleEffect.getEmitters();
        emitters.get(0).setContinuous(true);
    }

    private void unsetSmokeEffect(
            Engine engine,
            GameManager gameManager,
            RenderComponent renderComponent,
            TransformComponent transformComponent,
            Vector2 anchor
    ) {
        ParticleEffectData smokeEffect = renderComponent.effectData[HeroEffects.SMOKE];
        if(smokeEffect == null) {
            return;
        }
        Array<ParticleEmitter> emitters = smokeEffect.particleEffect.getEmitters();
        emitters.get(0).setContinuous(false);

        Entity smokeEntity = gameManager.gameEntityFactory.createFadeOutSmokeEntity(engine, anchor, transformComponent.copy(), smokeEffect); //todo
        engine.addEntity(smokeEntity);

        RenderUtils.unsetEffect(renderComponent, HeroEffects.SMOKE);
    }

}
