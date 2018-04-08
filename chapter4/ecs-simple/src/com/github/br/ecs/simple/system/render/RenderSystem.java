package com.github.br.ecs.simple.system.render;


import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.github.br.ecs.simple.engine.debug.DebugDataContainer;
import com.github.br.ecs.simple.engine.EntityId;
import com.github.br.ecs.simple.engine.IEcsSystem;
import com.github.br.ecs.simple.engine.debug.data.TableData;
import com.github.br.ecs.simple.utils.ViewHelper;

import java.util.LinkedHashMap;

import static java.lang.String.format;

/**
 * Created by user on 02.04.2017.
 */
public class RenderSystem implements IEcsSystem<RendererNode> {

    private LinkedHashMap<String, Layer> layers = new LinkedHashMap<String, Layer>();
    private LinkedHashMap<EntityId, String> entityLayerMap = new LinkedHashMap<EntityId, String>();

    private SpriteBatch batch = new SpriteBatch();

    private boolean debugMode;
    private DebugDataContainer debugDataContainer;

    public RenderSystem(String[] layers) {
        for(String layerTitle : layers){
            addLayer(layerTitle);
        }
    }

    private void addLayer(String title) {
        layers.put(title, new Layer(title));
    }

    @Override
    public void addNode(RendererNode node) {
        String layerTitle = node.renderer.layer;
        Layer layer = layers.get(layerTitle);
        if (layer == null) {
            throw new IllegalArgumentException(format("Слой '%s' не найден", layerTitle));
        }

        layer.addNode(node);
        entityLayerMap.put(node.entityId, layerTitle);
    }

    @Override
    public void removeNode(EntityId entityId) {
        String layerTitle = entityLayerMap.get(entityId);
        layers.get(layerTitle).removeNode(entityId);
    }

    @Override
    public Class<RendererNode> getNodeClass() {
        return RendererNode.class;
    }

    @Override
    public boolean isDebugMode() {
        return debugMode;
    }

    @Override
    public void setDebugMode(boolean debugMode) {
        this.debugMode = debugMode;
    }

    @Override
    public void update(float delta) {
        ViewHelper.applyCameraAndViewPort(batch);

        if(isDebugMode()) {
            if(debugDataContainer == null) {
                debugDataContainer = new DebugDataContainer();
            }
            debugDataContainer.clear(); // очищаем предыдущее состояние
        }

        batch.begin();
        for(Layer layer : layers.values()) {
            layer.render(batch);
            if (isDebugMode()) {
                debugDataContainer.put(new TableData(layer.getTitle(), "123"));
            }
        }
        batch.end();
    }

    @Override
    public DebugDataContainer getDebugData() {
        return debugDataContainer;
    }

}
