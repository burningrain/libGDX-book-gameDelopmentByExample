package com.github.br.ecs.simple;


import com.github.br.ecs.simple.component.EcsComponent;
import java.util.HashMap;

public class EcsEntity {

    private final String name;
    HashMap<Class, EcsComponent> components = new HashMap<>();

    public EcsEntity(String name){
        this.name = name;
    }

    public void addComponent(EcsComponent component) {
        if (components.containsKey(component.getClass())) {
            throw new IllegalArgumentException("Объект уже содержит компонент: " + component.getClass().getSimpleName());
        }
        components.put(component.getClass(), component);
    }

    public <T extends EcsComponent> T getComponent(Class<T> clazz){
        return (T)components.get(clazz);
    }

    public String getName() {
        return name;
    }
}
