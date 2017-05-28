package com.github.br.ecs.simple.system.render;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.github.br.ecs.simple.engine.EcsSystem;
import com.github.br.ecs.simple.system.transform.TransformComponent;

import java.util.Collection;
import java.util.List;

/**
 * Created by user on 02.04.2017.
 */
public class RenderSystem extends EcsSystem<RendererNode> {

    private SpriteBatch batch = new SpriteBatch();

    public RenderSystem() {
        super(RendererNode.class);
    }

    @Override
    public void update(float delta, Collection<RendererNode> nodes) {
        System.out.println(nodes.size());
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
