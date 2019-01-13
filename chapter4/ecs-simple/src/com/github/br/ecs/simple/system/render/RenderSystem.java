package com.github.br.ecs.simple.system.render;


import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.IntMap;
import com.badlogic.gdx.utils.OrderedMap;
import com.github.br.ecs.simple.engine.IDebugSystem;
import com.github.br.ecs.simple.engine.debug.DebugDataContainer;
import com.github.br.ecs.simple.engine.debug.data.TableData;
import com.github.br.ecs.simple.utils.ViewHelper;

import static java.lang.String.format;

/**
 * Created by user on 02.04.2017.
 */
public class RenderSystem implements IDebugSystem<RendererNode> {

    private OrderedMap<String, Layer> layers = new OrderedMap<String, Layer>();
    private IntMap<String> entityLayerMap = new IntMap<String>();

    private SpriteBatch batch = new SpriteBatch();

    private boolean debugMode;
    private DebugDataContainer debugDataContainer;
    private long executionTime;
    private int nodesAmount;

    public RenderSystem(String[] layers) {
        for(String layerTitle : layers) {
            addLayer(layerTitle);
        }
        debugDataContainer = new DebugDataContainer(); //todo см. DebugSystem
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
    public void removeNode(int entityId) {
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
        long before = 0;
        TableData.Builder builder = null;
        if(isDebugMode()) {
            debugDataContainer.clear(); // очищаем предыдущее состояние
            before = System.nanoTime();
            nodesAmount = 0;
            builder = new TableData.Builder();
        }

        ViewHelper.applyCameraAndViewPort(batch);
        batch.begin();
        for(Layer layer : layers.values()) {
            layer.render(batch);
            if (isDebugMode()) {
                nodesAmount += layer.getNodesAmount();
                builder.put(layer.getTitle(), "");
            }
        }
        batch.end();

        if(isDebugMode()) {
            executionTime = System.nanoTime() - before;
            debugDataContainer.put(builder.build());
        }
    }

    @Override
    public DebugDataContainer getDebugData() {
        return debugDataContainer;
    }

    @Override
    public long getExecutionTime() {
        return executionTime;
    }

    @Override
    public int getNodesAmount() {
        return nodesAmount;
    }

    private void addLayer(String title) {
        layers.put(title, new Layer(title));
    }

}
