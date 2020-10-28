package com.github.br.ecs.simple.engine;


/**
 * Ну как-то же надо управлять сущностью.
 * Наследуемся от скрипта и колдуем над компонентами. Все как в юнити.
 */
public abstract class EcsScript {

    private EcsEntity entity;
    private EcsContainer container;

    void setEntity(EcsEntity entity) {
        this.entity = entity;
    }

    void setContainer(EcsContainer container) {
        this.container = container;
    }

    public abstract void init(); // либо прокидывать entity через super, либо сетить после создания скрипта.

    public abstract void dispose();

    public abstract void update(float delta);

    public void createEntity(String type, EcsComponent... components) {
        container.createEntity(type, components);
    }

    public void deleteEntity(int entityId) {
        container.deleteEntity(entityId);
    }

    public void addComponents(int entityId, EcsComponent... components) {
        container.addComponents(entityId, components);
    }

    public void deleteComponents(int entityId, EcsComponent... components) {
        container.deleteComponents(entityId, components);
    }

    public int getEntityId() {
        return entity.getId();
    }

    /**
     * метод будет кидать npe при использовании в конструкторе или полях
     * @param clazz
     * @param <T>
     * @return
     */
    public <T extends EcsComponent> T getComponent(Class<T> clazz) {
        return entity.getComponent(clazz);
    }

}
