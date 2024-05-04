package com.github.br.libgdx.games.nutty.birds;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

public class SpriteGenerator {

    public static Sprite generateSpriteForBody(AssetManager assetManager, String userData) {
        if (Constants.HORIZONTAL.equals(userData)) {
            return createSprite(assetManager, Constants.OBSTACLE_HORIZONTAL_PNG);
        }
        if (Constants.VERTICAL.equals(userData)) {
            return createSprite(assetManager, Constants.OBSTACLE_VERTICAL_PNG);
        }
        if (Constants.ENEMY.equals(userData)) {
            return createSprite(assetManager, Constants.BIRD_PNG);
        }
        return null;
    }

    private static Sprite createSprite(AssetManager assetManager, String textureName) {
        Sprite sprite = new Sprite(assetManager.get(textureName, Texture.class));
        sprite.setOrigin(sprite.getWidth() / 2, sprite.getHeight() / 2);
        return sprite;
    }

}
