package com.github.br.paper.airplane.level;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Shape;
import com.github.br.paper.airplane.ecs.component.Box2dComponent;
import com.github.br.paper.airplane.ecs.component.TextureComponent;
import com.github.br.paper.airplane.ecs.component.TransformComponent;

public class GameComponentFactory {

    private final AssetManager assetManager;

    public GameComponentFactory(AssetManager assetManager) {
        this.assetManager = assetManager;
    }

    public TransformComponent createTransformComponent(Vector2 position, Vector2 scale, float angle, float width, float height) {
        TransformComponent component = new TransformComponent();
        component.position = position;
        component.scale = scale;
        component.origin = new Vector2(width / 2f, height / 2f);
        component.angle = angle;

        component.width = width;
        component.height = height;
        component.radius = (width + height) / 2;

        return component;
    }

    public TextureComponent createTextureComponent(String path) {
        TextureComponent component = new TextureComponent();
        component.region = new TextureRegion(assetManager.get(path, Texture.class));

        return component;
    }

    public Box2dComponent createBox2dComponent(
            Shape.Type shapeType,
            BodyDef bodyDef,
            FixtureDef fixtureDef
    ) {
        Box2dComponent component = new Box2dComponent();
        component.shapeType = shapeType;

        component.fixtureDef = fixtureDef;
        component.bodyDef = bodyDef;

        return component;
    }

}
