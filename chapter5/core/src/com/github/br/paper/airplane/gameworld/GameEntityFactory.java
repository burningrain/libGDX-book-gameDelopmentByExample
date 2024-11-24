package com.github.br.paper.airplane.gameworld;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.assets.AssetManager;
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

    public Entity createHero(Engine engine) {
        Entity entity = engine.createEntity();

        RenderComponent renderComponent = componentFactory.createTextureComponent(Res.AIR_HERO_PNG);
        entity.add(renderComponent);

        int width = renderComponent.region.getRegionWidth();
        int height = renderComponent.region.getRegionHeight();
        entity.add(componentFactory.createTransformComponent(
                new Vector2(
                        gameSettings.getVirtualScreenWidth() / 3 - width,
                        gameSettings.getVirtualScreenHeight() / 3 - height / 2
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

        InitComponent initComponent = new InitComponent();
        initComponent.velocity = velocity;
        entity.add(initComponent);

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

        entity.add(componentFactory.createParticleEffectComponent(Res.PARTICLE_COIN_P, Vector2.Zero));

        InitComponent initComponent = new InitComponent();
        initComponent.velocity = velocity;
        entity.add(initComponent);

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

        RenderComponent peComponent = componentFactory.createParticleEffectComponent(Res.PARTICLE_BULLET_TYPE_P, Vector2.Zero);
        for (ParticleEmitter emitter : peComponent.particleEffect.getEmitters()) {
            ParticleEmitter.GradientColorValue tint = emitter.getTint();
            float[] colors = tint.getColors();
            // смотри https://corecoding.com/utilities/rgb-or-hex-to-float.php
            switch (bulletType) {
                case FIRE:
                    colors[0] = 1;
                    colors[1] = 0;
                    colors[2] = 0;
                    break;
                case ELECTRICITY:
                    colors[0] = 0;
                    colors[1] = 0;
                    colors[2] = 1;
                    break;
                case VENOM:
                    colors[0] = 0;
                    colors[1] = 1;
                    colors[2] = 0;
                    break;
            }
        }
        entity.add(peComponent);

        entity.add(new BulletTypeComponent(bulletType));

        InitComponent initComponent = new InitComponent();
        initComponent.velocity = velocity;
        entity.add(initComponent);

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
        entity.add(componentFactory.createParticleEffectComponent(pathToParticleEffect, anchor)); // TODO сделать пул
        InitComponent initComponent = new InitComponent();
        initComponent.velocity = new Vector2(
                // переводим полярные координаты в декартовые
                velocity * MathUtils.cos(angle * MathUtils.degreesToRadians),
                velocity * MathUtils.sin(angle * MathUtils.degreesToRadians)
        );
        entity.add(initComponent);
        entity.add(new HealthComponent(health, damage));

        return entity;
    }

}
