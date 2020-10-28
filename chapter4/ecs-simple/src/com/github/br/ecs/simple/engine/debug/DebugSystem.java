package com.github.br.ecs.simple.engine.debug;

import com.badlogic.gdx.utils.IntMap;
import com.github.br.ecs.simple.engine.EcsEntity;
import com.github.br.ecs.simple.engine.EcsSystem;

/**
 * Created by user on 12.06.2018.
 */
public abstract class DebugSystem extends EcsSystem {

    private boolean debugMode;
    private DebugDataContainer debugDataContainer;

    private long executionTime;
    private int nodesAmount;

    public DebugSystem(Class... componentsClasses) {
        super(componentsClasses);
        debugDataContainer = new DebugDataContainer(); //todo не создавать, если дебаг отключен в настройках контейнера
    }

    public boolean isDebugMode() {
        return debugMode;
    }

    public void setDebugMode(boolean debugMode) {
        this.debugMode = debugMode;
    }

    protected void update(float delta, IntMap.Values<EcsEntity> nodes) {
        long before = 0;
        if(isDebugMode()) {
            nodesAmount = entities.size; //fixme хак
            debugDataContainer.clear(); // очищаем предыдущее состояние
            before = System.nanoTime();
        }

        update(delta, nodes, debugDataContainer);

        if(isDebugMode()) {
            executionTime = System.nanoTime() - before;
        }
    }

    protected abstract void update(float delta, IntMap.Values<EcsEntity> nodes, DebugDataContainer debugDataContainer);

    public DebugDataContainer getDebugData() {
        return debugDataContainer;
    }

    public long getExecutionTime() {
        return executionTime;
    }

    public int getNodesAmount() {
        return nodesAmount;
    }

}
