package com.github.br.paper.airplane.gameworld;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Shape;
import com.github.br.paper.airplane.GameSettings;
import com.github.br.paper.airplane.Utils;
import com.github.br.paper.airplane.ecs.component.HeroComponent;
import com.github.br.paper.airplane.ecs.component.WallComponent;
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

    public Entity createBadLogicLogo(Engine engine) {
        Entity entity = engine.createEntity();

//        TextureComponent textureComponent = componentFactory.createTextureComponent("badlogic.jpg");
//        entity.add(textureComponent);

        int width = 128;
        int height = 128;
        entity.add(componentFactory.createTransformComponent(
                new Vector2(
                        gameSettings.getVirtualScreenWidth() / 2 - width,
                        gameSettings.getVirtualScreenHeight() / 2 - height / 2
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

        entity.add(componentFactory.createBox2dComponent(
                Shape.Type.Polygon,
                bodyDef,
                fixtureDef,
                null
        ));

        entity.add(new HeroComponent());

        return entity;
    }

    public Entity createWall(Engine engine, int x, int y, int width, int height, int angle) {
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
        bodyDef.type = BodyDef.BodyType.KinematicBody;
        bodyDef.bullet = false;

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.density = 0.1f;

        entity.add(componentFactory.createBox2dComponent(
                Shape.Type.Polygon,
                bodyDef,
                fixtureDef,
                null
        ));

        entity.add(new WallComponent());

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
                fixtureDef,
                null
        ));

        return entity;
    }

}
