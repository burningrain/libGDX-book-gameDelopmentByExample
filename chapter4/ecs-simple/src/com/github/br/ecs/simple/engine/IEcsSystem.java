package com.github.br.ecs.simple.engine;

/**
 * Created by user on 23.06.2017.
 */
public interface IEcsSystem<T extends EcsNode> {
    void addNode(T node);

    void removeNode(EntityId entityId);

    Class<T> getNodeClass();

    boolean isDebugMode();

    void setDebugMode(boolean debugMode);

    void update(float delta);
}
