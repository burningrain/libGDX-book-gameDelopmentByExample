package com.github.br.ecs.simple.engine;

import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.utils.IntMap;

public abstract class EcsSystem extends ScreenAdapter {

    private final Class[] componentsClasses;
    protected IntMap<EcsEntity> entities = new IntMap<EcsEntity>();

    public EcsSystem(Class... componentsClasses) {
        this.componentsClasses = componentsClasses;
    }

    public void addEntity(EcsEntity entity) {
        entities.put(entity.getId(), entity);
    }

    public void removeEntity(int entityId) {
        entities.remove(entityId);
    }

    public boolean isApplySystem(EcsEntity entity) {
        if (entities.containsKey(entity.getId())) {
            return false;
        }

        boolean isApply = true;
        for (Class componentsClass : componentsClasses) {
            if (entity.getComponent(componentsClass) == null) {
                isApply = false;
                break;
            }
        }

        return isApply;
    }

    public boolean isApplySystem(EntityManager.ComponentChangeEvent componentChangeEvent) {
        switch (componentChangeEvent.type) {
            case ADD:
                return isApplySystem(componentChangeEvent.ecsEntity);
            case DELETE:
                boolean isApply = false;
                for (Class componentsClass : componentsClasses) {
                    if (componentChangeEvent.targetComponents.containsKey(componentsClass)) {
                        isApply = true;
                        break;
                    }
                }

                return isApply;
            default:
                throw new IllegalArgumentException();
        }
    }

    public void render(float delta) {
    }

    public void update(float delta) {
        update(delta, entities.values());
    }

    protected abstract void update(float delta, IntMap.Values<EcsEntity> nodes);

}
