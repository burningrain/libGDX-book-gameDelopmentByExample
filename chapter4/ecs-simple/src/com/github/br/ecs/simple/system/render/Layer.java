package com.github.br.ecs.simple.system.render;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.IntMap;
import com.github.br.ecs.simple.engine.EcsEntity;
import com.github.br.ecs.simple.system.transform.TransformComponent;

/**
 * Created by user on 22.06.2017.
 */
public class Layer {

    private String title;
    private IntMap<EcsEntity> nodes = new IntMap<EcsEntity>();

    public Layer(String title) {
        this.title = title;
    }

    public void addEntity(EcsEntity entity) {
        nodes.put(entity.getId(), entity);
    }

    public EcsEntity removeEntity(int entityId) {
        return nodes.remove(entityId);
    }

    public void render(SpriteBatch batch, Array<EcsEntity> changeLayerEntities) {
        for (EcsEntity entity : nodes.values()) {
            TransformComponent transform = entity.getComponent(TransformComponent.class);
            RendererComponent renderer = entity.getComponent(RendererComponent.class);

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

            if(renderer.newLayerTitle != null) {
                changeLayerEntities.add(entity);
            }
        }
    }

    public String getTitle() {
        return title;
    }

    public int getNodesAmount() {
        return nodes.size;
    }

}
