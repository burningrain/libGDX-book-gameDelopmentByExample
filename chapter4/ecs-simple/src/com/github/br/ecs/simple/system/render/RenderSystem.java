package com.github.br.ecs.simple.system.render;


import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.IntMap;
import com.badlogic.gdx.utils.OrderedMap;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.github.br.ecs.simple.engine.DebugSystem;
import com.github.br.ecs.simple.engine.EcsEntity;
import com.github.br.ecs.simple.engine.debug.DebugDataContainer;
import com.github.br.ecs.simple.engine.debug.data.TableData;
import com.github.br.ecs.simple.system.transform.TransformComponent;

import static java.lang.String.format;

/**
 * Created by user on 02.04.2017.
 */
public class RenderSystem extends DebugSystem {

    private Array<Layer> layersList = new Array<Layer>();
    private OrderedMap<String, Layer> layersMap = new OrderedMap<String, Layer>();
    private IntMap<String> entityLayerMap = new IntMap<String>();

    private boolean debugMode;
    private DebugDataContainer debugDataContainer;
    private long executionTime;
    private int nodesAmount;

    private ShaderSubsystem shaderSubsystem;

    public RenderSystem(Viewport viewport, LayerData[] layers) {
        super(TransformComponent.class, RendererComponent.class);
        shaderSubsystem = new ShaderSubsystem(viewport, layers);
        for (LayerData layerData : layers) {
            addLayer(layerData.title);
        }
        debugDataContainer = new DebugDataContainer(); //todo см. DebugSystem
    }

    @Override
    public void addEntity(EcsEntity entity) {
        String layerTitle = entity.getComponent(RendererComponent.class).layer;
        Layer layer = layersMap.get(layerTitle);
        if (layer == null) {
            throw new IllegalArgumentException(format("Слой '%s' не найден", layerTitle));
        }

        layer.addEntity(entity);
        entityLayerMap.put(entity.getId(), layerTitle);
    }

    @Override
    public void removeEntity(int entityId) {
        String layerTitle = entityLayerMap.get(entityId);
        layersMap.get(layerTitle).removeEntity(entityId);
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
    public void render(float delta) {
        long before = 0;
        TableData.Builder builder = null;
        if (isDebugMode()) {
            debugDataContainer.clear(); // очищаем предыдущее состояние
            before = System.nanoTime();
            nodesAmount = 0;
            builder = new TableData.Builder();
        }
        final TableData.Builder tableBuilder = builder;

        final Array<EcsEntity> ecsEntities = new Array<EcsEntity>();
        shaderSubsystem.update(new ShaderSubsystem.BatchListener() {
            @Override
            public void update(int layerNumber, SpriteBatch batch) {
                Layer layer = layersList.get(layerNumber);
                layer.render(batch, ecsEntities);
                if (isDebugMode()) {
                    nodesAmount += layer.getNodesAmount();
                    tableBuilder.put(layer.getTitle(), String.valueOf(layer.getNodesAmount()));
                }
            }

            @Override
            public boolean isNeedPaintLayer(int layerNumber) {
                return layersList.get(layerNumber).getNodesAmount() != 0;
            }
        });

        // меняем слои у сущностей. В частности - для шейдерных эффектов, так как "шейдер на слой"
        for (EcsEntity ecsEntity : ecsEntities) {
            changeLayerForEntity(ecsEntity);
        }

        if (isDebugMode()) {
            executionTime = System.nanoTime() - before;
            debugDataContainer.put(builder.build());
        }
    }

    //fixme косяк архитектуры, при переопределении render метод update не используется
    @Override
    protected void update(float delta, IntMap.Values<EcsEntity> nodes) {
    }

    @Override
    public void resize(int width, int height) {
        shaderSubsystem.resize(width, height);
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
        Layer layer = new Layer(title);
        layersList.add(layer);
        layersMap.put(title, layer);
    }

    private void changeLayerForEntity(EcsEntity ecsEntity) {
        removeEntity(ecsEntity.getId());
        RendererComponent component = ecsEntity.getComponent(RendererComponent.class);
        component.layer = component.newLayerTitle;
        component.newLayerTitle = null;
        addEntity(ecsEntity);
    }

}
