package com.github.br.ecs.simple.engine;

import com.github.br.ecs.simple.engine.debug.DebugDataContainer;

/**
 * Created by user on 12.06.2018.
 */
public abstract class DebugSystem extends EcsSystem {

    public DebugSystem(Class... componentsClasses) {
        super(componentsClasses);
    }

    public abstract boolean isDebugMode();

    public abstract void setDebugMode(boolean debugMode);

    public abstract DebugDataContainer getDebugData();

    public abstract long getExecutionTime();

    public abstract int getNodesAmount();

}
