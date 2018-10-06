package com.github.br.ecs.simple.engine;

import com.github.br.ecs.simple.engine.debug.DebugDataContainer;

import java.util.Collection;

/**
 * Created by user on 12.06.2018.
 */
public abstract class DebugSystem<T extends EcsNode> extends EcsSystem<T> implements IDebugSystem<T> {

    private boolean debugMode;
    private DebugDataContainer debugDataContainer;


    public DebugSystem(Class<T> clazz) {
        super(clazz);
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
    protected void update(float delta, Collection<T> nodes) {
        if(isDebugMode()) {
            if(debugDataContainer == null) {
                debugDataContainer = new DebugDataContainer();
            }
            debugDataContainer.clear(); // очищаем предыдущее состояние
        }
        update(delta, nodes, debugDataContainer);
    }

    protected abstract void update(float delta, Collection<T> nodes, DebugDataContainer debugDataContainer);

    @Override
    public DebugDataContainer getDebugData() {
        return debugDataContainer;
    }

}
