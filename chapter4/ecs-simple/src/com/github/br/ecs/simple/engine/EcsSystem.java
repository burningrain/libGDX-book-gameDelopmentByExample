package com.github.br.ecs.simple.engine;

import com.badlogic.gdx.utils.IntMap;

public abstract class EcsSystem<T extends EcsNode> implements IEcsSystem<T> {

    private final Class<T> nodeClazz;
    protected IntMap<T> allNodes = new IntMap<T>();

    public EcsSystem(Class<T> clazz) {
        this.nodeClazz = clazz;
    }

    @Override
    public void addNode(T node) {
        allNodes.put(node.entityId, node);
    }

    @Override
    public void removeNode(int entityId) {
        allNodes.remove(entityId);
    }

    @Override
    public Class<T> getNodeClass() {
        return nodeClazz;
    }

    @Override
    public void update(float delta) {
        update(delta, allNodes.values());
    }

    protected abstract void update(float delta, IntMap.Values<T> nodes);

}
