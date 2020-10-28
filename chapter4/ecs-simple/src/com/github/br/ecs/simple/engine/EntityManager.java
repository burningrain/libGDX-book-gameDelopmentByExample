package com.github.br.ecs.simple.engine;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.IntArray;
import com.badlogic.gdx.utils.IntMap;
import com.badlogic.gdx.utils.ObjectMap;

/**
 * Created by user on 28.05.2017.
 */
public class EntityManager {

    private IdGenerator idGenerator = new IdGenerator();
    private IntMap<EcsEntity> entities = new IntMap<EcsEntity>();

    private volatile boolean hasChanges = false;

    private Array<EcsEntity> addedEntities = new Array<EcsEntity>(false, 32);
    private IntArray deletedEntities = new IntArray(false, 32);

    private Array<ComponentChangeEvent> addedComponents = new Array<ComponentChangeEvent>();
    private Array<ComponentChangeEvent> deletedComponents = new Array<ComponentChangeEvent>();

    public int createEntity(String type, EcsComponent... components) {
        hasChanges = true;

        EcsEntity entity = new EcsEntity(idGenerator.nextId());

        // заполняем сущность компонентами
        for (EcsComponent component : components) {
            entity.addComponent(component);
        }
        addedEntities.add(entity);
        return entity.getId();
    }

    public void deleteEntity(int id) {
        hasChanges = true;
        deletedEntities.add(id);
    }

    public void addComponents(int entityId, EcsComponent... components) {
        hasChanges = true;
        addedComponents.add(new ComponentChangeEvent(ComponentChangeEvent.Type.ADD, entities.get(entityId), components));
    }

    public void deleteComponents(int entityId, EcsComponent... components) {
        hasChanges = true;
        deletedComponents.add(new ComponentChangeEvent(ComponentChangeEvent.Type.DELETE, entities.get(entityId), components));
    }

    public void update(EntityEventCallback createEntityEventCallback, EntityEventCallback deleteEntityEventCallback,
                       ComponentEventCallback addComponentEventCallback, ComponentEventCallback deleteComponentEventCallback) {
        if(!hasChanges) {
            return;
        }

        int addedSize = addedEntities.size;
        for (int i = 0; i < addedSize; i++) {
            EcsEntity entity = addedEntities.get(i);
            entities.put(entity.getId(), entity);
            createEntityEventCallback.call(entity);
        }

        int deleteSize = deletedEntities.size;
        for (int i = 0; i < deleteSize; i++) {
            int id = deletedEntities.get(i);
            EcsEntity entity = entities.get(id);
            entities.remove(id);
            deleteEntityEventCallback.call(entity);
        }

        int addedComponentsSize = addedComponents.size;
        for (int i = 0; i < addedComponentsSize; i++) {
            ComponentChangeEvent componentChangeEvent = addedComponents.get(i);
            EcsEntity ecsEntity = componentChangeEvent.ecsEntity;
            for (EcsComponent component : componentChangeEvent.targetComponents.values()) {
                ecsEntity.addComponent(component);
            }
            addComponentEventCallback.call(componentChangeEvent);
        }

        int deletedComponentsSize = deletedComponents.size;
        for (int i = 0; i < deletedComponentsSize; i++) {
            ComponentChangeEvent componentChangeEvent = deletedComponents.get(i);
            EcsEntity ecsEntity = componentChangeEvent.ecsEntity;
            for (EcsComponent component : componentChangeEvent.targetComponents.values()) {
                ecsEntity.deleteComponent(component.getClass());
            }
            deleteComponentEventCallback.call(componentChangeEvent);
        }

        addedEntities.clear();
        deletedEntities.clear();

        addedComponents.clear();
        deletedComponents.clear();

        hasChanges = false;
    }

    public interface EntityEventCallback {
        void call(EcsEntity entity);
    }

    public interface ComponentEventCallback {
        void call(ComponentChangeEvent componentChangeEvent);
    }

    public static class ComponentChangeEvent {

        public enum Type {
            ADD, DELETE;
        }

        public final Type type;
        public final EcsEntity ecsEntity;
        public final ObjectMap<Class, EcsComponent> targetComponents;

        public ComponentChangeEvent(Type type, EcsEntity ecsEntity, EcsComponent... newComponents) {
            this.type = type;
            this.ecsEntity = ecsEntity;

            ObjectMap<Class, EcsComponent> targetComponents = new ObjectMap<Class, EcsComponent>();
            for (EcsComponent changeComponent : newComponents) {
                targetComponents.put(changeComponent.getClass(), changeComponent);
            }
            this.targetComponents = targetComponents;
        }
    }

}
