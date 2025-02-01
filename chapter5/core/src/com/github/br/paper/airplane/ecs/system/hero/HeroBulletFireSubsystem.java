package com.github.br.paper.airplane.ecs.system.hero;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.math.Vector2;
import com.github.br.paper.airplane.GameManager;
import com.github.br.paper.airplane.GamePlaySettings;
import com.github.br.paper.airplane.bullet.BulletStrategy;
import com.github.br.paper.airplane.ecs.component.*;
import com.github.br.paper.airplane.ecs.component.render.EffectType;
import com.github.br.paper.airplane.ecs.component.render.ParticleEffectData;
import com.github.br.paper.airplane.ecs.component.render.RenderComponent;
import com.github.br.paper.airplane.ecs.component.render.RenderUtils;
import com.github.br.paper.airplane.gameworld.DelayComponentName;
import com.github.br.paper.airplane.gameworld.HeroEffects;

public class HeroBulletFireSubsystem implements HeroSybSystem {

    private ParticleEffectData heroSmokeEffect; // TODO вынести отсюда этот объект в кэш с пулами
    private Vector2 smokeAnchor; //TODO см. выше

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
        renderComponent = mappers.renderMapper.get(entity);

        HeroComponent heroComponent = mappers.heroMapper.get(entity);
        //TODO кривота, надо не высчитывать лениво, а все-таки задавать сразу числами. Размеры-то известны!
        if (smokeAnchor == null) {
            float width = transformComponent.width;
            smokeAnchor = new Vector2(-width / 2, 0);
        }
        // TODO создавать при старте, а не лениво
        if (heroFireEffect == null) {
            float width = transformComponent.width;
            heroFireEffect = gameManager.gameEntityFactory.createHeroFireEffect(new Vector2(-width / 2, 0));
        }

        // либо стреляем, либо просто поднимаем вверх. Два действия повешены на тапы, поэтому так.
        // два быстрых тапа - выстрел. Один тап и удержание - подъем вверх
        if (inputComponent.isDoubleClick) {
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
            inputComponent.isDoubleClick = false;
            unsetSmokeEffect(engine, gameManager, renderComponent, transformComponent, smokeAnchor);
        } else if (inputComponent.isPressedDown) {
            // если не стреляем, значит поднимаем самолетик вверх
            box2dComponent.body.applyForce(gamePlaySettings.getUpForce(), box2dComponent.body.getWorldCenter(), true);

            // TODO создавать при старте, а не лениво
            if (heroSmokeEffect == null) {
                heroSmokeEffect = gameManager.gameEntityFactory.createHeroSmokeFastFadeOutEffect(smokeAnchor);
            }

            if (inputComponent.isClick) {
                setSmokeEffect(renderComponent);

                RenderUtils.setEffect(renderComponent, heroFireEffect, HeroEffects.FIRE);
                entity.add(delayComponent);
            }
        } else if (inputComponent.isPressedUp) {
            unsetSmokeEffect(engine, gameManager, renderComponent, transformComponent, smokeAnchor);
        }
    }

    private void setSmokeEffect(RenderComponent renderComponent) {
        heroSmokeEffect.reset();
        ParticleEffectData smokeEffect = RenderUtils.setEffect(renderComponent, heroSmokeEffect, HeroEffects.SMOKE);
        RenderUtils.unsetEffectType(smokeEffect);
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
        RenderUtils.setEffectType(smokeEffect, EffectType.TRANSPARENCY_FADE_OUT);
        Entity smokeEntity = gameManager.gameEntityFactory.createFadeOutSmokeEntity(engine, anchor, transformComponent.copy(), smokeEffect); //todo
        engine.addEntity(smokeEntity);

        RenderUtils.unsetEffect(renderComponent, HeroEffects.SMOKE);
    }

}
