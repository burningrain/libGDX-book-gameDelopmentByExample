package com.github.br.paper.airplane.ecs.system.render;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.github.br.paper.airplane.Utils;
import com.github.br.paper.airplane.ecs.component.TransformComponent;
import com.github.br.paper.airplane.ecs.component.render.TextureData;

public class RenderTextureSubsystem {

    private final Utils utils;

    public RenderTextureSubsystem(Utils utils) {
        this.utils = utils;
    }

    public void render(TransformComponent transformComponent, SpriteBatch spriteBatch, float deltaTime, TextureData textureData) {
        Vector2 anchor = textureData.renderPosition.anchorDelta;
        TextureRegion region = textureData.region;
        Vector2 newPosition = utils.rotatePointToAngle(anchor.x, anchor.y, transformComponent.degreeAngle);
        spriteBatch.draw(
                region,
                transformComponent.position.x + newPosition.x,
                transformComponent.position.y + newPosition.y,
                transformComponent.origin.x,
                transformComponent.origin.y,
                region.getRegionWidth(),
                region.getRegionHeight(),
                transformComponent.scale.x,
                transformComponent.scale.y,
                transformComponent.degreeAngle
        );
    }

}
