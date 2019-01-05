package com.github.br.ecs.simple.engine;

/**
 * Created by user on 23.06.2017.
 */
public interface IEcsSystem<T extends EcsNode> {

    void addNode(T node);

    void removeNode(EntityId entityId);

    Class<T> getNodeClass();

    void update(float delta);

}