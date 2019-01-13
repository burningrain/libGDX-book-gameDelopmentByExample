package com.github.br.ecs.simple.engine;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.IntArray;
import com.badlogic.gdx.utils.IntMap;

/**
 * Created by user on 28.05.2017.
 */
public class EntityManager {

    private IdGenerator idGenerator = new IdGenerator();
    private IntMap<EcsEntity> entities = new IntMap<EcsEntity>();

    private volatile boolean hasChanges = false;
    private Array<EcsEntity> added = new Array<EcsEntity>(false, 32);
    private IntArray deleted = new IntArray(false, 32);

    public int createEntity(String type, EcsComponent... components) {
        hasChanges = true;

        EcsEntity entity = new EcsEntity(idGenerator.nextId());

        // заполняем сущность компонентами
        for (EcsComponent component : components) {
            entity.addComponent(component);
        }
        added.add(entity);
        return entity.getId();
    }


    public void deleteEntity(int id) {
        hasChanges = true;
        deleted.add(id);
    }

    public boolean hasChanges() {
        return hasChanges;
    }

    public void update(Callback createCallback, Callback deleteCallback) {
        int addedSize = added.size;
        for (int i = 0; i < addedSize; i++) {
            EcsEntity entity = added.get(i);
            entities.put(entity.getId(), entity);
            createCallback.call(entity);
        }

        int deleteSize = deleted.size;
        for (int i = 0; i < deleteSize; i++) {
            int id = deleted.get(i);
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
