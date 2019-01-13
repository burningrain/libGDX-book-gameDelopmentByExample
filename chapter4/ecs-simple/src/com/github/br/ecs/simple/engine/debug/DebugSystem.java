package com.github.br.ecs.simple.engine.debug;

import com.badlogic.gdx.utils.IntMap;
import com.github.br.ecs.simple.engine.EcsNode;
import com.github.br.ecs.simple.engine.EcsSystem;
import com.github.br.ecs.simple.engine.IDebugSystem;
import com.github.br.ecs.simple.engine.debug.DebugDataContainer;

import java.util.Collection;

/**
 * Created by user on 12.06.2018.
 */
public abstract class DebugSystem<T extends EcsNode> extends EcsSystem<T> implements IDebugSystem<T> {

    private boolean debugMode;
    private DebugDataContainer debugDataContainer;

    private long executionTime;
    private int nodesAmount;

    public DebugSystem(Class<T> clazz) {
        super(clazz);
        debugDataContainer = new DebugDataContainer(); //todo не создавать, если дебаг отключен в настройках контейнера
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
    protected void update(float delta, IntMap.Values<T> nodes) {
        long before = 0;
        if(isDebugMode()) {
            nodesAmount = allNodes.size; //fixme хак
            debugDataContainer.clear(); // очищаем предыдущее состояние
            before = System.nanoTime();
        }

        update(delta, nodes, debugDataContainer);

        if(isDebugMode()) {
            executionTime = System.nanoTime() - before;
        }
    }

    protected abstract void update(float delta, IntMap.Values<T> nodes, DebugDataContainer debugDataContainer);

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

}
