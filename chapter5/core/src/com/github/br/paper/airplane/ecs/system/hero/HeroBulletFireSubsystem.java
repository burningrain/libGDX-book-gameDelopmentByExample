package com.github.br.paper.airplane.ecs.system.hero;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.math.Vector2;
import com.github.br.paper.airplane.GameManager;
import com.github.br.paper.airplane.GamePlaySettings;
import com.github.br.paper.airplane.bullet.BulletStrategy;
import com.github.br.paper.airplane.ecs.component.*;
import com.github.br.paper.airplane.ecs.component.render.ParticleEffectData;
import com.github.br.paper.airplane.ecs.component.render.RenderComponent;
import com.github.br.paper.airplane.ecs.component.render.RenderUtils;

public class HeroBulletFireSubsystem implements HeroSybSystem {

    private ParticleEffectData heroSmokeEffect; // TODO вынести отсюда этот объект в кэш с пулами

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
            renderComponent.effectData[0] = null;
        } else if (inputComponent.isPressedDown) {
            // если не стреляем, значит поднимаем самолетик вверх
            box2dComponent.body.applyForce(gamePlaySettings.getUpForce(), box2dComponent.body.getWorldCenter(), true);

            // TODO создавать при старте, а не лениво
            if (heroSmokeEffect == null) {
                float width = transformComponent.width;
                heroSmokeEffect = gameManager.gameEntityFactory.createHeroSmokeEffect(new Vector2(-width / 2, 0));
            }

            RenderUtils.setEffect(renderComponent, heroSmokeEffect, 0);
        } else if (!inputComponent.isPressedDown) {
            RenderUtils.unsetEffect(renderComponent,  0);
        }
    }

}
