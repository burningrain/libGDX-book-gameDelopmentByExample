package com.github.br.ecs.simple.engine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by user on 28.05.2017.
 */
public class EntityManager {

    private IdGenerator idGenerator = new IdGenerator();
    private HashMap<EntityId, EcsEntity> entities = new HashMap<EntityId, EcsEntity>();

    private volatile boolean hasChanges = false;
    private ArrayList<EcsEntity> added = new ArrayList<EcsEntity>();
    private ArrayList<EntityId> deleted = new ArrayList<EntityId>();

    public EntityId createEntity(String type, EcsComponent... components) {
        hasChanges = true;

        int id = idGenerator.nextId();
        EntityId entityId = EntityId.of(id, type);
        EcsEntity entity = new EcsEntity(entityId);

        // заполняем сущность компонентами
        for (EcsComponent component : components) {
            entity.addComponent(component);
        }
        added.add(entity);
        return entityId;
    }


    public void deleteEntity(EntityId id) {
        hasChanges = true;
        deleted.add(id);
    }

    public boolean hasChanges() {
        return hasChanges;
    }

    public void update(Callback createCallback, Callback deleteCallback) {
        int addedSize = added.size();
        for (int i = 0; i < addedSize; i++) {
            EcsEntity entity = added.get(i);
            entities.put(entity.getId(), entity);
            createCallback.call(entity);
        }

        int deleteSize = deleted.size();
        for (int i = 0; i < deleteSize; i++) {
            EntityId id = deleted.get(i);
            EcsEntity entity = entities.get(id);
            entities.remove(id);
            deleteCallback.call(entity);
        }

        added.clear();
        deleted.clear();
        hasChanges = false;
    }


    public interface Callback {

        void call(EcsEntity entity);

    }


}
