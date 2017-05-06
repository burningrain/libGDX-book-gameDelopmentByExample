package com.github.br.ecs.simple;

import com.github.br.ecs.simple.component.EcsComponent;

/**
 * Ну как-то же надо управлять сущностью.
 * Наследуемся от скрипта и колдуем над компонентами. Все как в юнити.
 */
public abstract class EcsScript {

    private EcsEntity entity;

    void setEntity(EcsEntity entity) {
        this.entity = entity;
    }

    public abstract void init(); // либо прокидывать entity через super, либо сетить после создания скрипта.
    public abstract void update(float delta);


    // метод будет кидать npe при использовании в конструкторе или полях
    public <T extends EcsComponent> T getComponent(Class<T> clazz){
        return entity.getComponent(clazz);
    }

}
