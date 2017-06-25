package com.github.br.ecs.simple.engine;

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
    private LinkedList<EcsEntity> added = new LinkedList<EcsEntity>();
    private LinkedList<EntityId> deleted = new LinkedList<EntityId>();

    public void createEntity(String type, EcsComponent... components) {
        hasChanges = true;

        int id = idGenerator.nextId();
        EntityId entityId = EntityId.of(id, type);
        EcsEntity entity = new EcsEntity(entityId);

        // заполняем сущность компонентами
        for (EcsComponent component : components) {
            entity.addComponent(component);
        }
        added.add(entity);
    }


    public void deleteEntity(EntityId id) {
        hasChanges = true;
        deleted.add(id);
    }

    public boolean hasChanges(){
        return hasChanges;
    }

    public void update(Callback createCallback, Callback deleteCallback){
        EcsEntity ecsEntity;
        while((ecsEntity = added.poll()) != null){
            entities.put(ecsEntity.getId(), ecsEntity);
            createCallback.call(ecsEntity);
        }
        EntityId entityId;
        while((entityId = deleted.poll()) != null){
            EcsEntity entity = entities.get(entityId);
            entities.remove(entityId);
            deleteCallback.call(entity);
        }
        hasChanges = false;
    }


    public interface Callback {

        void call(EcsEntity entity);

    }


}
