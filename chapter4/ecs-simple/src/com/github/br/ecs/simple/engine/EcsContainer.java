package com.github.br.ecs.simple.engine;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.github.br.ecs.simple.engine.debug.DebugService;
import com.github.br.ecs.simple.engine.debug.InputProcessorWrapper;
import com.github.br.ecs.simple.engine.debug.console.Command;
import com.github.br.ecs.simple.engine.debug.console.ConsoleService;
import com.github.br.ecs.simple.system.animation.AnimationSystem;
import com.github.br.ecs.simple.system.script.ScriptComponent;
import com.github.br.ecs.simple.system.physics.PhysicsSystem;
import com.github.br.ecs.simple.system.render.RenderSystem;
import com.github.br.ecs.simple.system.script.ScriptSystem;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedList;

import static java.lang.String.format;

/**
 * Контейнер, порождающий игровые сущности. Связывает вместе системы, компоненты и сущности
 */
public class EcsContainer {

    private EcsSettings settings;

    private boolean isDebugMode;
    private ConsoleService consoleService;
    private DebugService debugService;
    private LinkedHashMap<Class, IEcsSystem> systems = new LinkedHashMap<Class, IEcsSystem>();
    private EntityManager entityManager = new EntityManager();

    private ActivateConsoleInputProcessor activateConsoleInputProcessor;
    private InputMultiplexer inputMultiplexer;

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
            ((IDebugSystem) ecsSystem).setDebugMode(active);
        } else {
            System.out.println(format("Система '%s' не поддерживает режим отладки", system)); //todo логгер впилить
        }
    }

    public EcsContainer(EcsSettings settings) {
        this.settings = settings;
        // инициализация систем. Порядок очень важен!
        addSystem(ScriptSystem.class);
        addSystem(PhysicsSystem.class);
        addSystem(AnimationSystem.class);
        addSystem(new RenderSystem(settings.layers));

        ArrayList<InputProcessor> processors = new ArrayList<InputProcessor>();
        if (settings.isConsoleEnabled) {
            consoleService = new ConsoleService();
        }
        if (settings.isDebugEnabled) {
            debugService = new DebugService(consoleService); //todo сделать дебаг независимым от консоли
            addDebugSystem(filterDebugSystems(systems.values()));
            setDebugMode(true);

            activateConsoleInputProcessor = new ActivateConsoleInputProcessor();
            processors.add(activateConsoleInputProcessor);
            processors.add(wrapProcessor(debugService.getInputProcessor(), new InputProcessorWrapper.Predicate() {
                @Override
                public boolean apply() {
                    return !isDebugMode;
                }
            }));
        }
        initInputMultiplexer(processors.toArray(new InputProcessor[0]));
    }

    public InputProcessor getInputProcessor() {
        return inputMultiplexer;
    }

    public void addConsoleCommands(Command[] commands) {
        consoleService.addCommands(commands);
    }

    private void initInputMultiplexer(InputProcessor... processors) {
        this.inputMultiplexer = new InputMultiplexer(processors);
        //Gdx.input.setInputProcessor(inputMultiplexer); todo пока решил оставить это на откуп клиенту
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
            ((IDebugSystem) system).setDebugMode(active);
        }
    }

    private void addDebugSystem(Collection<IDebugSystem> systems) {
        for (IDebugSystem system : systems) {
            debugService.addSystem(system);
        }
    }

    public void update(float delta) {
        EcsSimple.ECS.update(delta);

        if (entityManager.hasChanges()) {
            entityManager.update(createCallback, deleteCallback); // коллбеки для очистки нод в системах
        }
        for (IEcsSystem system : systems.values()) {
            system.update(delta);
        }
        if (isDebugMode) {
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

    private static InputProcessor wrapProcessor(final InputProcessor inputProcessor, InputProcessorWrapper.Predicate predicate) {
        return new InputProcessorWrapper(inputProcessor, predicate);
    }

    private class ActivateConsoleInputProcessor extends InputAdapter {

        @Override
        public boolean keyDown(int keycode) {
            if (Input.Keys.F9 == keycode) {
                isDebugMode = !isDebugMode;
                setDebugMode(isDebugMode);
                return true;
            }
            return false;
        }

    }


    //todo сделать билдер для корректной инициализации контейнера

}
