package com.github.br.ecs.simple.engine;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.github.br.ecs.simple.engine.debug.DebugService;
import com.github.br.ecs.simple.engine.debug.console.ConsoleService;
import com.github.br.ecs.simple.system.animation.AnimationSystem;
import com.github.br.ecs.simple.system.script.ScriptComponent;
import com.github.br.ecs.simple.system.physics.PhysicsSystem;
import com.github.br.ecs.simple.system.render.RenderSystem;
import com.github.br.ecs.simple.system.script.ScriptSystem;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedList;

import static java.lang.String.format;

/**
 * Контейнер, порождающий игровые сущности. Связывает вместе системы, компоненты и сущности
 */
public class EcsContainer {

    private EcsSettings settings;

    private ConsoleService consoleService;
    private DebugService debugService;
    private LinkedHashMap<Class, IEcsSystem> systems = new LinkedHashMap<Class, IEcsSystem>();
    private EntityManager entityManager = new EntityManager();

    private EntityManager.Callback createCallback = new EntityManager.Callback() {
        @Override
        public void call(EcsEntity entity) {
            // заполняем системы нодами с компонентами сущности
            for (IEcsSystem<EcsNode> system : systems.values()) {
                Class nodeClass = system.getNodeClass();
                EcsNode node = EcsReflectionHelper.createAndFillNode(nodeClass, entity);
                if (node != null) {
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
            for (IEcsSystem system : systems.values()) {
                system.removeNode(entity.getId());
            }
        }
    };

    public void setDebugMode(boolean active, Class<? extends IEcsSystem> system) {
        IEcsSystem ecsSystem = getSystem(system);
        if (ecsSystem == null) throw new IllegalArgumentException(format("Система '%s' не найдена", system));
        if (ecsSystem instanceof DebugSystem) {
            ((IDebugSystem)ecsSystem).setDebugMode(active);
        }
    }

    public EcsContainer(EcsSettings settings) {
        this.settings = settings;
        // инициализация систем. Порядок очень важен!
        addSystem(ScriptSystem.class);
        addSystem(PhysicsSystem.class);
        addSystem(AnimationSystem.class);
        addSystem(new RenderSystem(settings.layers));

        if (settings.debug) {
            consoleService = new ConsoleService(this.settings.commands);
            debugService = new DebugService(consoleService);
            addDebugSystem(filterDebugSystems(systems.values()));
            setDebugMode(true);
        }

    }

    private static Collection<IDebugSystem> filterDebugSystems(Collection<IEcsSystem> systems) {
        LinkedList<IDebugSystem> result = new LinkedList<IDebugSystem>();
        for (IEcsSystem system : systems) {
            if (system instanceof IDebugSystem) result.add((IDebugSystem) system);
        }
        return result;
    }

    private void setDebugMode(boolean active) {
        for (IEcsSystem system : filterDebugSystems(systems.values())) {
            ((IDebugSystem)system).setDebugMode(active);
        }
    }

    private void addDebugSystem(Collection<IDebugSystem> systems) {
        for (IDebugSystem system : systems) {
            debugService.addSystem(system);
        }
    }

    public void update(float delta) {
        EcsSimple.ECS.update(delta);

        //fixme дребезг контактов
        if (Gdx.input.isKeyPressed(Input.Keys.F9)) {
            setDebugMode(settings.debug = !settings.debug);
        }

        if (entityManager.hasChanges()) {
            entityManager.update(createCallback, deleteCallback); // коллбеки для очистки нод в системах
        }
        for (IEcsSystem system : systems.values()) {
            system.update(delta);
        }
        if (settings.debug) {
            debugService.update(delta);
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
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException ex) {
            ex.printStackTrace();
        }
    }

    public void addSystem(IEcsSystem system) {
        systems.put(system.getClass(), system);
    }


}
