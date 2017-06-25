package com.github.br.ecs.simple.system.render;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.github.br.ecs.simple.engine.EntityId;
import com.github.br.ecs.simple.system.transform.TransformComponent;

import java.util.HashMap;

/**
 * Created by user on 22.06.2017.
 */
public class Layer {

    private String title;
    private HashMap<EntityId, RendererNode> nodes = new HashMap<EntityId, RendererNode>();

    public Layer(String title){
        this.title = title;
    }

    public void addNode(RendererNode node){
        nodes.put(node.entityId, node);
    }

    public void removeNode(EntityId entityId){
        nodes.remove(entityId);
    }

    public void render(SpriteBatch batch){
        for (RendererNode node : nodes.values()) {
            TransformComponent transform = node.transform;
            RendererComponent renderer = node.renderer;
            batch.draw(renderer.textureRegion.getTexture(),
                    transform.position.x,
                    transform.position.y,
                    renderer.textureRegion.getRegionWidth() * transform.scale.x,
                    renderer.textureRegion.getRegionHeight() * transform.scale.y,
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
    }

    public String getTitle() {
        return title;
    }
}
