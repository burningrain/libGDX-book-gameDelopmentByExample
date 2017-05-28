package com.github.br.ecs.simple.engine;

import java.util.Collection;
import java.util.HashMap;

public abstract class EcsSystem<T extends EcsNode> {

    private final Class<T> nodeClazz;
    private HashMap<EntityId, T> allNodes = new HashMap<>();

    private boolean debugMode;

    public EcsSystem(Class<T> clazz){
        this.nodeClazz = clazz;
    }

    public void addNode(T node){
        allNodes.put(node.entityId, node);
    }

    public void removeNode(EntityId entityId){
        allNodes.remove(entityId);
    }

    public Class<T> getNodeClass() {
        return nodeClazz;
    }

    public boolean isDebugMode() {
        return debugMode;
    }

    public void setDebugMode(boolean debugMode) {
        this.debugMode = debugMode;
    }

    public void update(float delta){
        update(delta, allNodes.values());
    }

    protected abstract void update(float delta, Collection<T> nodes);

}
