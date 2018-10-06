package com.github.br.ecs.simple.engine;

import java.util.Collection;
import java.util.HashMap;

public abstract class EcsSystem<T extends EcsNode> implements IEcsSystem<T> {

    private final Class<T> nodeClazz;
    private HashMap<EntityId, T> allNodes = new HashMap<EntityId, T>();

    public EcsSystem(Class<T> clazz){
        this.nodeClazz = clazz;
    }

    @Override
    public void addNode(T node){
        allNodes.put(node.entityId, node);
    }

    @Override
    public void removeNode(EntityId entityId){
        allNodes.remove(entityId);
    }

    @Override
    public Class<T> getNodeClass() {
        return nodeClazz;
    }

    @Override
    public void update(float delta){
        update(delta, allNodes.values());
    }

    protected abstract void update(float delta, Collection<T> nodes);

}
