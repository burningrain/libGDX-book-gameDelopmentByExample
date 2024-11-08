package com.github.br.paper.airplane.gameworld;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Shape;
import com.github.br.paper.airplane.GameSettings;
import com.github.br.paper.airplane.Utils;
import com.github.br.paper.airplane.ecs.component.*;
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
        entity.add(new HealthComponent(5, 0));

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

        entity.add(new HealthComponent(3, 1));
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

    public Entity createCoin(Engine engine, int x, int y, Vector2 velocity) {
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

        entity.add(componentFactory.createParticleEffectComponent(Res.PARTICLE_COIN_P));

        InitComponent initComponent = new InitComponent();
        initComponent.velocity = velocity;
        entity.add(initComponent);

        ScriptComponent scriptComponent = new ScriptComponent();
        scriptComponent.scripts = new Script[]{
                new CoinScript()
        };

        entity.add(scriptComponent);

        return entity;
    }

    public Entity createBullet(Engine engine, int x, int y, int radius, float angle) {
        Entity entity = engine.createEntity();
        entity.add(componentFactory.createTransformComponent(
                new Vector2(x, y),
                new Vector2(1f, 1f),
                angle,
                radius,
                radius
        ));

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.bullet = true;

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.density = 1f;

        entity.add(componentFactory.createBox2dComponent(
                Shape.Type.Circle,
                bodyDef,
                fixtureDef
        ));

        entity.add(componentFactory.createParticleEffectComponent(Res.PARTICLE_BULLET_P)); // TODO сделать пул

        InitComponent initComponent = new InitComponent();
        initComponent.velocity = new Vector2(
                5 * MathUtils.cos(angle * MathUtils.degreesToRadians),
                1 * MathUtils.sin(angle * MathUtils.degreesToRadians));
        entity.add(initComponent);
        entity.add(new HealthComponent(1, 1));

        return entity;
    }

}
