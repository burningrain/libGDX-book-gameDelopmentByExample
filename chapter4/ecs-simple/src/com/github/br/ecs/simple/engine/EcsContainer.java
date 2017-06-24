package com.github.br.ecs.simple.engine;

import com.github.br.ecs.simple.system.animation.AnimationSystem;
import com.github.br.ecs.simple.system.script.ScriptComponent;
import com.github.br.ecs.simple.system.physics.PhysicsSystem;
import com.github.br.ecs.simple.system.render.RenderSystem;
import com.github.br.ecs.simple.system.script.ScriptSystem;
import com.github.br.ecs.simple.system.transform.TransformDebugSystem;

import java.util.LinkedHashMap;

import static java.lang.String.format;

/**
 * Контейнер, порождающий игровые сущности. Связывает вместе системы, компоненты и сущности
 */
public class EcsContainer {

    private LinkedHashMap<Class, IEcsSystem> systems = new LinkedHashMap<>();
    private EntityManager entityManager = new EntityManager();

    private EntityManager.Callback createCallback = new EntityManager.Callback() {
        @Override
        public void call(EcsEntity entity) {
            // заполняем системы нодами с компонентами сущности
            for (IEcsSystem<EcsNode> system : systems.values()) {
                Class nodeClass = system.getNodeClass();
                EcsNode node = EcsReflectionHelper.createAndFillNode(nodeClass, entity);
                if(node != null){
                    system.addNode(node);
                }
            }
            // передаем ссылку на сущность и компоненты скриптам
            ScriptComponent scriptComponent = entity.getComponent(ScriptComponent.class);
            if (scriptComponent != null) {
                for (EcsScript script : scriptComponent.scripts) {
                    script.setEntity(entity);
                    script.setContainer(EcsContainer.this);
                    script.init();
                }
            }
        }
    };

    private EntityManager.Callback deleteCallback = new EntityManager.Callback() {
        @Override
        public void call(EcsEntity entity) {
            for(IEcsSystem system : systems.values()){
                system.removeNode(entity.getId());
            }
        }
    };

    public void setDebugMode(boolean active) {
        for(IEcsSystem system : systems.values()){
            system.setDebugMode(active);
        }
    }

    public void setDebugMode(boolean active, Class<? extends IEcsSystem> system) {
        IEcsSystem ecsSystem = getSystem(system);
        if(ecsSystem == null) throw new IllegalArgumentException(format("Система '%s' не найдена", system));
        ecsSystem.setDebugMode(active);
    }

    public EcsContainer(EcsSettings settings) {
        // инициализация систем. Порядок очень важен!
        addSystem(ScriptSystem.class);
        addSystem(PhysicsSystem.class);
        addSystem(AnimationSystem.class);
        addSystem(new RenderSystem(settings.layers));
        addSystem(TransformDebugSystem.class);
    }

    public void update(float delta) {
        if(entityManager.hasChanges()){
            entityManager.update(createCallback, deleteCallback); // коллбеки для очистки нод в системах
        }
        for (IEcsSystem system : systems.values()) {
            system.update(delta);
        }
    }

    public void createEntity(String type, EcsComponent... components) {
        entityManager.createEntity(type, components);
    }

    public void deleteEntity(EntityId id) {
        entityManager.deleteEntity(id);
    }


    private <T extends EcsSystem> T getSystem(Class<? extends IEcsSystem> clazz) {
        return (T) systems.get(clazz);
    }


    public void addSystem(Class<? extends IEcsSystem> clazz) {
        try {
            systems.put(clazz, clazz.newInstance());
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public void addSystem(IEcsSystem system) {
        systems.put(system.getClass(), system);
    }


}
