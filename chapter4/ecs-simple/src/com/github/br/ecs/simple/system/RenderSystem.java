package com.github.br.ecs.simple.system;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.github.br.ecs.simple.component.RendererComponent;
import com.github.br.ecs.simple.component.TransformComponent;
import com.github.br.ecs.simple.node.RendererNode;

/**
 * Created by user on 02.04.2017.
 */
public class RenderSystem extends EcsSystem<RendererNode> {

    //TODO
    private SpriteBatch batch = new SpriteBatch();

    public RenderSystem() {
        super(RendererNode.class);
    }

    @Override
    public void update(float delta) {
        batch.begin();
        for(RendererNode node : nodes){
            TransformComponent transform = node.transform;
            RendererComponent renderer = node.renderer;

            renderer.sprite.setX(transform.position.x);
            renderer.sprite.setY(transform.position.y);
            renderer.sprite.setRotation(transform.rotation);

            renderer.sprite.draw(batch);
        }
        batch.end();
    }
}
