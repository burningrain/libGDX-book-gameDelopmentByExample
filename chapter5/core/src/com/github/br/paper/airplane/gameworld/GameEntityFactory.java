package com.github.br.paper.airplane.gameworld;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Shape;
import com.github.br.paper.airplane.GameSettings;
import com.github.br.paper.airplane.Utils;
import com.github.br.paper.airplane.ecs.component.TextureComponent;
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

        TextureComponent textureComponent = componentFactory.createTextureComponent("badlogic.jpg");
        entity.add(textureComponent);
        entity.add(componentFactory.createTransformComponent(
                new Vector2(
                        gameSettings.getVirtualScreenWidth() / 2 - textureComponent.region.getRegionWidth() / 2,
                        gameSettings.getVirtualScreenHeight() / 2 - textureComponent.region.getRegionHeight() / 2
                ),
                new Vector2(1f, 1f),
                new Vector2(0f, 0f),
                0f,
                textureComponent.region.getRegionWidth(),
                textureComponent.region.getRegionHeight()
        ));


        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;

        entity.add(componentFactory.createBox2dComponent(
                Shape.Type.Polygon,
                0,
                1f,
                bodyDef,
                null
        ));

        return entity;
    }

}
