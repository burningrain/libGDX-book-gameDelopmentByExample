package com.github.br.ecs.simple.system;


import com.github.br.ecs.simple.node.EcsNode;

import java.util.ArrayList;

public abstract class EcsSystem<T extends EcsNode> {

    private final Class<T> nodeClazz;
    ArrayList<T> nodes = new ArrayList<>();

    private boolean debugMode;

    public EcsSystem(Class<T> clazz){
        this.nodeClazz = clazz;
    }

    public void addNode(T node){
        nodes.add(node);
    }

    public void removeNode(T node){
        nodes.remove(node);
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

    public abstract void update(float delta);


}
