package com.github.br.ecs.simple.engine;

import com.github.br.ecs.simple.engine.debug.DebugDataContainer;

/**
 * Created by user on 12.06.2018.
 */
public interface IDebugSystem<T extends EcsNode> extends IEcsSystem<T> {

    boolean isDebugMode();

    void setDebugMode(boolean debugMode);

    DebugDataContainer getDebugData();

}
