package com.github.br.ecs.simple.system;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.github.br.ecs.simple.component.RendererComponent;
import com.github.br.ecs.simple.component.TransformComponent;
import com.github.br.ecs.simple.node.RendererNode;

/**
 * Created by user on 02.04.2017.
 */
public class RenderSystem extends EcsSystem<RendererNode> {

    private SpriteBatch batch = new SpriteBatch();

    public RenderSystem() {
        super(RendererNode.class);
    }

    @Override
    public void update(float delta) {
        batch.begin();
        for (RendererNode node : nodes) {
            TransformComponent transform = node.transform;
            RendererComponent renderer = node.renderer;

            batch.draw(renderer.textureRegion.getTexture(),
                    transform.position.x,
                    transform.position.y,
                    renderer.textureRegion.getRegionWidth() * transform.scale.x * 0.5f,
                    renderer.textureRegion.getRegionHeight() * transform.scale.y * 0.5f,
                    renderer.textureRegion.getRegionWidth() * transform.scale.x,
                    renderer.textureRegion.getRegionHeight() * transform.scale.y,
                    transform.scale.x,
                    transform.scale.y,
                    transform.rotation,
                    renderer.textureRegion.getRegionX(),
                    renderer.textureRegion.getRegionY(),
                    renderer.textureRegion.getRegionWidth(),
                    renderer.textureRegion.getRegionHeight(),
                    renderer.flipX,
                    renderer.flipY
            );
        }
        batch.end();
    }
}
