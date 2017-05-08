package com.github.br.ecs.simple;

import com.github.br.ecs.simple.component.EcsComponent;
import com.github.br.ecs.simple.component.ScriptComponent;
import com.github.br.ecs.simple.node.EcsNode;
import com.github.br.ecs.simple.system.*;
import com.github.br.ecs.simple.utils.EcsReflectionHelper;

import java.util.HashMap;
import java.util.LinkedHashMap;

/**
 * Контейнер, порождающий игровые сущности. Связывает вместе системы, компоненты и сущности
 */
public class EcsContainer {

    private LinkedHashMap<Class, EcsSystem> systems = new LinkedHashMap<>();
    private HashMap<String, EcsEntity> entities = new HashMap<>();

    public void setDebugMode(boolean active) {
        for(EcsSystem system : systems.values()){
            system.setDebugMode(active);
        }
    }

    public void setDebugMode(boolean active, Class<EcsSystem> system) {
        EcsSystem ecsSystem = getSystem(system);
        ecsSystem.setDebugMode(active);
    }

    public EcsContainer() {
        // инициализация систем. Порядок очень важен!
        //TODO поправить порядок, когда в системе рендеринга появятся слои для отрисовки
        addSystem(ScriptSystem.class);
        addSystem(PhysicsSystem.class);
        addSystem(AnimationSystem.class);
        addSystem(RenderSystem.class);
        addSystem(TransformDebugSystem.class);
    }

    public void update(float delta) {
        for (EcsSystem system : systems.values()) {
            system.update(delta);
        }
    }

    public void createEntity(String name, EcsComponent... components) {
        EcsEntity entity = new EcsEntity(name);
        entities.put(name, entity);

        // заполняем сущность компонентами
        for (EcsComponent component : components) {
            entity.addComponent(component);
        }

        // заполняем системы нодами с компонентами сущности
        for (EcsSystem<EcsNode> system : systems.values()) {
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
                script.init();
            }
        }
    }

    public void deleteEntity(String name) {
        //TODO
    }


    private <T extends EcsSystem> T getSystem(Class<? extends EcsSystem> clazz) {
        return (T) systems.get(clazz);
    }


    private void addSystem(Class<? extends EcsSystem> clazz) {
        try {
            systems.put(clazz, clazz.newInstance());
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }


}
