package com.github.br.ecs.simple.engine;


import java.util.HashMap;

public class EcsEntity {

    private final int id;
    HashMap<Class, EcsComponent> components = new HashMap<Class, EcsComponent>();

    EcsEntity(int id) {
        this.id = id;
    }

    public void addComponent(EcsComponent component) {
        if (components.containsKey(component.getClass())) {
            throw new IllegalArgumentException("Объект уже содержит компонент: " + component.getClass().getSimpleName());
        }
        components.put(component.getClass(), component);
    }

    public <T extends EcsComponent> T getComponent(Class<T> clazz) {
        return (T) components.get(clazz);
    }

    public int getId() {
        return id;
    }
}
