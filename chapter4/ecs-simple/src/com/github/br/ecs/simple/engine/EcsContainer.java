package com.github.br.ecs.simple.engine;

import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.utils.Array;
import com.github.br.ecs.simple.engine.debug.EcsDebug;
import com.github.br.ecs.simple.system.script.ScriptComponent;

/**
 * Контейнер, порождающий игровые сущности. Связывает вместе системы, компоненты и сущности
 */
public class EcsContainer {

    private EcsSettings settings;

    private EcsDebug ecsDebug;
    private Array<IEcsSystem> systems = new Array<IEcsSystem>();
    private EntityManager entityManager = new EntityManager();
    private InputMultiplexer inputMultiplexer;

    private EntityManager.Callback createCallback = new EntityManager.Callback() {
        @Override
        public void call(EcsEntity entity) {
            // заполняем системы нодами с компонентами сущности
            for (IEcsSystem<EcsNode> system : systems) {
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
            for (IEcsSystem system : systems) {
                system.removeNode(entity.getId());
            }
        }
    };

    public void setDebugMode(boolean active) {
        ecsDebug.setDebugMode(active);
    }

    public void setDebugMode(Class<? extends IEcsSystem> system, boolean active) {
        ecsDebug.setDebugMode(system, active);
    }

    public EcsContainer(EcsSettings settings) {
        this.settings = settings;
    }

    //fixme мрак, сделано из-за передачи списка в дебаг. Убрать когда сделаю билдер
    public void init() {
        Array<InputProcessor> processors = new Array<InputProcessor>();
        if (settings.isDebugEnabled) {
            ecsDebug = new EcsDebug(systems);
            setDebugMode(true);

            processors.add(ecsDebug.getInputProcessor());
        }

        initInputMultiplexer((InputProcessor[]) processors.toArray(InputProcessor.class));
    }

    public InputProcessor getInputProcessor() {
        return inputMultiplexer;
    }

    private void initInputMultiplexer(InputProcessor... processors) {
        this.inputMultiplexer = new InputMultiplexer(processors);
        //Gdx.input.setInputProcessor(inputMultiplexer); todo пока решил оставить это на откуп клиенту
    }

    public void update(float delta) {
        EcsSimple.ECS.update(delta);
        if (entityManager.hasChanges()) {
            entityManager.update(createCallback, deleteCallback); // коллбеки для очистки нод в системах
        }
        for (IEcsSystem system : systems) {
            system.update(delta);
        }

        if (ecsDebug.isDebugActive()) {
            ecsDebug.update(delta);
        }
    }

    public int createEntity(String type, EcsComponent... components) {
        return entityManager.createEntity(type, components);
    }

    public void deleteEntity(int id) {
        entityManager.deleteEntity(id);
    }

    public void addSystem(Class<? extends IEcsSystem> clazz) {
        try {
            systems.add(clazz.newInstance());
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException ex) {
            ex.printStackTrace();
        }
    }

    public void addSystem(IEcsSystem system) {
        systems.add(system);
    }


    //todo сделать билдер для корректной инициализации контейнера

}
