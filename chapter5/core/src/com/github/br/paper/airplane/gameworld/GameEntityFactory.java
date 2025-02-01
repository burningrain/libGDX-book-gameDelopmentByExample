package com.github.br.paper.airplane.gameworld;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.ParticleEmitter;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Shape;
import com.github.br.paper.airplane.GameSettings;
import com.github.br.paper.airplane.Utils;
import com.github.br.paper.airplane.bullet.BulletType;
import com.github.br.paper.airplane.ecs.component.*;
import com.github.br.paper.airplane.ecs.component.render.ParticleEffectData;
import com.github.br.paper.airplane.ecs.component.render.RenderComponent;
import com.github.br.paper.airplane.ecs.component.render.RenderPosition;
import com.github.br.paper.airplane.ecs.component.render.TextureData;
import com.github.br.paper.airplane.ecs.script.BulletTypeScript;
import com.github.br.paper.airplane.ecs.script.CoinScript;
import com.github.br.paper.airplane.level.GameComponentFactory;

public class GameEntityFactory {

    private final AssetManager assetManager;
    private final GameComponentFactory componentFactory;

    private final GameSettings gameSettings;
    private final Utils utils;

    public GameEntityFactory(AssetManager assetManager, GameSettings gameSettings, Utils utils) {
        this.assetManager = assetManager;
        componentFactory = new GameComponentFactory(assetManager);
        this.gameSettings = gameSettings;
        this.utils = utils;
    }

    public Entity createBackground(Engine engine) {
        Entity entity = engine.createEntity();
        RenderPosition renderPosition = new RenderPosition();
        renderPosition.layer = RenderLayers.BACKGROUND.getLayer();
        RenderComponent renderComponent = componentFactory.createTextureComponent(
                Res.Pictures.BACKGROUND_PNG,
                renderPosition
        );
        entity.add(renderComponent);

        TextureData textureDatum = renderComponent.textureData[0];
        int width = textureDatum.region.getRegionWidth();
        int height = textureDatum.region.getRegionHeight();
        TransformComponent transformComponent = componentFactory.createTransformComponent(
                // позиция у меня считается от центра, не забывать
                new Vector2(
                        gameSettings.getVirtualScreenWidth() / 2f - width / 2f,
                        gameSettings.getVirtualScreenHeight() / 2f - height / 2f
                ),
                new Vector2(
                        ((float) gameSettings.getVirtualScreenWidth()) / width,
                        ((float) gameSettings.getVirtualScreenHeight()) / height
                ),
                0f,
                width,
                height
        );
        entity.add(transformComponent);

        return entity;
    }

    public Entity createHero(Engine engine, InputComponent inputComponent) {
        Entity entity = engine.createEntity();

        RenderPosition position = new RenderPosition();
        RenderComponent renderComponent = componentFactory.createTextureComponent(Res.Pictures.AIR_HERO_PNG, position);
        renderComponent.effectData = new ParticleEffectData[2];

        entity.add(renderComponent);

        TextureData textureDatum = renderComponent.textureData[0];
        int width = textureDatum.region.getRegionWidth();
        int height = textureDatum.region.getRegionHeight();
        entity.add(componentFactory.createTransformComponent(
                new Vector2(
                        gameSettings.getVirtualScreenWidth() / 3f - width,
                        gameSettings.getVirtualScreenHeight() / 3f - height / 2f
                ),
                new Vector2(1f, 1f),
                0f,
                width,
                height
        ));

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.bullet = true;

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.density = 1.4f;

        Box2dComponent box2dComponent = componentFactory.createBox2dComponent(
                Shape.Type.Polygon,
                bodyDef,
                fixtureDef
        );
        box2dComponent.isGravityOff = false;
        entity.add(box2dComponent);

        entity.add(new HeroComponent());
        entity.add(new HealthComponent((short) 5, (short) 0));
        entity.add(inputComponent);

        return entity;
    }

    public Entity createWall(Engine engine, int x, int y, int width, int height, int angle, Vector2 velocity) {
        Entity entity = engine.createEntity();

//        TextureComponent textureComponent = componentFactory.createTextureComponent("badlogic.jpg");
//        entity.add(textureComponent);
        entity.add(componentFactory.createTransformComponent(
                new Vector2(x, y),
                new Vector2(1f, 1f),
                angle,
                width,
                height
        ));

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.bullet = false;

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.density = 3_000f;

        entity.add(componentFactory.createBox2dComponent(
                Shape.Type.Polygon,
                bodyDef,
                fixtureDef
        ));
        entity.add(new InitComponent(velocity));

        entity.add(new HealthComponent(gameSettings.getGamePlaySettings().getWallLife(), (short) 1));
        return entity;
    }

    public Entity createCeil(Engine engine) {
        Entity entity = engine.createEntity();
        entity.add(componentFactory.createTransformComponent(
                new Vector2(0, gameSettings.getVirtualScreenHeight()),
                new Vector2(1f, 1f),
                0,
                gameSettings.getVirtualScreenWidth(),
                1
        ));

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;
        FixtureDef fixtureDef = new FixtureDef();

        entity.add(componentFactory.createBox2dComponent(
                Shape.Type.Polygon,
                bodyDef,
                fixtureDef
        ));

        return entity;
    }

    public Entity createItemBulletsAmount(Engine engine, int x, int y, Vector2 velocity) {
        Entity entity = engine.createEntity();
        entity.add(componentFactory.createTransformComponent(
                new Vector2(x, y),
                new Vector2(1f, 1f),
                0,
                16,
                16
        ));

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.KinematicBody;
        bodyDef.bullet = true;

        FixtureDef fixtureDef = new FixtureDef();

        entity.add(componentFactory.createBox2dComponent(
                Shape.Type.Circle,
                bodyDef,
                fixtureDef
        ));

        RenderPosition renderPosition = new RenderPosition();
        entity.add(componentFactory.createParticleEffectComponent(Res.Particles.PARTICLE_COIN_P, renderPosition));
        entity.add(new InitComponent(velocity));

        ScriptComponent scriptComponent = new ScriptComponent();
        scriptComponent.scripts = new Script[]{
                new CoinScript(gameSettings)
        };

        entity.add(scriptComponent);

        return entity;
    }

    public Entity createItemBulletType(Engine engine, int x, int y, Vector2 velocity, BulletType bulletType) {
        Entity entity = engine.createEntity();
        entity.add(componentFactory.createTransformComponent(
                new Vector2(x, y),
                new Vector2(1f, 1f),
                0,
                16,
                16
        ));

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.KinematicBody;
        bodyDef.bullet = true;

        FixtureDef fixtureDef = new FixtureDef();

        entity.add(componentFactory.createBox2dComponent(
                Shape.Type.Circle,
                bodyDef,
                fixtureDef
        ));

        RenderPosition renderPosition = new RenderPosition();
        RenderComponent peComponent = componentFactory.createParticleEffectComponent(Res.Particles.PARTICLE_BULLET_TYPE_P, renderPosition);
        ParticleEffectData effectDatum = peComponent.effectData[0];

        ParticleEmitter emitter1 = effectDatum.particleEffect.getEmitters().get(0);
        ParticleEmitter.GradientColorValue tint1 = emitter1.getTint();
        float[] colors1 = tint1.getColors();
        // смотри https://corecoding.com/utilities/rgb-or-hex-to-float.php
        switch (bulletType) {
            case FIRE:
                colors1[0] = 0.765f;
                colors1[1] = 0.773f;
                colors1[2] = 0.396f;
                break;
            case ELECTRICITY:
                colors1[0] = 0.243f;
                colors1[1] = 0.824f;
                colors1[2] = 0.906f;
                break;
            case VENOM:
                colors1[0] = 0.396f;
                colors1[1] = 0.843f;
                colors1[2] = 0.341f;
                break;
        }

        ParticleEmitter emitter = effectDatum.particleEffect.getEmitters().get(1);
        ParticleEmitter.GradientColorValue tint = emitter.getTint();
        float[] colors = tint.getColors();
        // смотри https://corecoding.com/utilities/rgb-or-hex-to-float.php
        switch (bulletType) {
            case FIRE:
                colors[6] = 1;
                colors[7] = 0;
                colors[8] = 0;
                break;
            case ELECTRICITY:
                colors[6] = 0;
                colors[7] = 0;
                colors[8] = 1;
                break;
            case VENOM:
                colors[6] = 0;
                colors[7] = 1;
                colors[8] = 0;
                break;
        }

        entity.add(peComponent);
        entity.add(new BulletTypeComponent(bulletType));
        entity.add(new InitComponent(velocity));

        ScriptComponent scriptComponent = new ScriptComponent();
        scriptComponent.scripts = new Script[]{
                new BulletTypeScript(gameSettings)
        };

        entity.add(scriptComponent);

        return entity;
    }

    public Entity createRectangleBullet(
            Engine engine,
            int x,
            int y,
            int width,
            int height,
            float angle,
            float velocity,
            short damage,
            String pathToParticleEffect,
            Vector2 anchor
    ) {
        TransformComponent transformComponent = componentFactory.createTransformComponent(
                new Vector2(x, y),
                new Vector2(1f, 1f),
                angle,
                width,
                height
        );

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.bullet = true;

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.density = 1f;
        Box2dComponent box2dComponent = componentFactory.createBox2dComponent(
                Shape.Type.Polygon,
                bodyDef,
                fixtureDef
        );
        return createBullet(
                engine,
                transformComponent,
                box2dComponent,
                angle,
                velocity,
                (short) 1,
                damage,
                pathToParticleEffect,
                anchor
        );
    }

    public Entity createCircleBullet(
            Engine engine,
            int x,
            int y,
            int radius,
            float angle,
            float velocity,
            short damage,
            String pathToParticleEffect
    ) {
        TransformComponent transformComponent = componentFactory.createTransformComponent(
                new Vector2(x, y),
                new Vector2(1f, 1f),
                angle,
                radius,
                radius
        );
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.bullet = true;

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.density = 1f;
        Box2dComponent box2dComponent = componentFactory.createBox2dComponent(
                Shape.Type.Circle,
                bodyDef,
                fixtureDef
        );
        return createBullet(
                engine,
                transformComponent,
                box2dComponent,
                angle,
                velocity,
                (short) 1,
                damage,
                pathToParticleEffect,
                Vector2.Zero
        );
    }

    public Entity createBullet(
            Engine engine,
            TransformComponent transformComponent,
            Box2dComponent box2dComponent,
            float angle,
            float velocity,
            short health,
            short damage,
            String pathToParticleEffect,
            Vector2 anchor
    ) {
        Entity entity = engine.createEntity();
        entity.add(transformComponent);
        entity.add(box2dComponent);

        RenderPosition renderPosition = new RenderPosition();
        renderPosition.anchorDelta = anchor;
        entity.add(componentFactory.createParticleEffectComponent(pathToParticleEffect, renderPosition)); // TODO сделать пул
        entity.add(new InitComponent(new Vector2(
                // переводим полярные координаты в декартовые
                velocity * MathUtils.cos(angle * MathUtils.degreesToRadians),
                velocity * MathUtils.sin(angle * MathUtils.degreesToRadians)
        )));
        entity.add(new HealthComponent(health, damage));

        return entity;
    }

    public Entity createFadeOutSmokeEntity(Engine engine, Vector2 anchor, TransformComponent transformComponent, ParticleEffectData heroSmokeEffect) {
        RenderComponent renderComponent = new RenderComponent();
        renderComponent.textureData = null;
        renderComponent.effectData = new ParticleEffectData[] {heroSmokeEffect};

        Entity entity = engine.createEntity();
        entity.add(transformComponent);
        entity.add(renderComponent);
        // TODO вынести в пул!!!
        entity.add(new DelayComponent(DelayComponentName.FADE_OUT_SMOKE_PARTICLE, 0.4f, new Runnable() {
            @Override
            public void run() {
                entity.add(new DestroyedComponent());
            }
        }));
        entity.add(new SimpleTweenComponent(new Vector2(-140, 0), Vector2.Zero)); //todo добавить направление, обратное углу наклона

        return entity;
    }

    public ParticleEffectData createHeroSmokeFastFadeOutEffect(Vector2 anchor) {
        return createParticleEffect(RenderLayers.BACK, anchor, Res.Particles.PARTICLE_SMOKE_FAST_FADE_OUT_P);
    }

    public ParticleEffectData createHeroFireEffect(Vector2 anchor) {
        return createParticleEffect(RenderLayers.BACK, anchor, Res.Particles.PARTICLE_FIRE_P);
    }

    public ParticleEffectData createParticleEffect(RenderLayers layers, Vector2 anchor, String path) {
        RenderPosition renderPosition = new RenderPosition();
        renderPosition.anchorDelta = anchor;
        renderPosition.layer = layers.getLayer();

        ParticleEffect particleEffect = componentFactory.createParticleEffect(path);
        return new ParticleEffectData(renderPosition, particleEffect);
    }

}
