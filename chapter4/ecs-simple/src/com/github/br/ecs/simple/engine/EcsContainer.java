package com.github.br.ecs.simple.engine;

import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.utils.Array;
import com.github.br.ecs.simple.engine.debug.EcsDebug;
import com.github.br.ecs.simple.system.script.ScriptComponent;

/**
 * Контейнер, порождающий игровые сущности. Связывает вместе системы, компоненты и сущности
 */
public class EcsContainer implements Screen {

    private final EcsSettings settings;

    private EcsDebug ecsDebug;
    private final Array<EcsSystem> systems = new Array<EcsSystem>();
    private final EntityManager entityManager = new EntityManager();
    private InputMultiplexer inputMultiplexer;

    private EntityManager.EntityEventCallback createEntityEventCallback = new EntityManager.EntityEventCallback() {
        @Override
        public void call(EcsEntity entity) {
            // заполняем системы нодами с компонентами сущности
            for (EcsSystem system : systems) {
                if (system.isApplySystem(entity)) {
                    system.addEntity(entity);
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

    private final EntityManager.EntityEventCallback deleteEntityEventCallback = new EntityManager.EntityEventCallback() {
        @Override
        public void call(EcsEntity entity) {
            // fixme перебор всех систем, неоптимально.
            for (EcsSystem system : systems) {
                system.removeEntity(entity.getId());
            }
        }
    };

    private final EntityManager.ComponentEventCallback addedComponentEventCallback = new EntityManager.ComponentEventCallback() {
        @Override
        public void call(EntityManager.ComponentChangeEvent componentChangeEvent) {
            for (EcsSystem system : systems) {
                if(system.isApplySystem(componentChangeEvent)) {
                    system.addEntity(componentChangeEvent.ecsEntity);
                }
            }
        }
    };

    private final EntityManager.ComponentEventCallback deletedComponentEventCallback = new EntityManager.ComponentEventCallback() {
        @Override
        public void call(EntityManager.ComponentChangeEvent componentChangeEvent) {
            for (EcsSystem system : systems) {
                if(system.isApplySystem(componentChangeEvent)) {
                    system.removeEntity(componentChangeEvent.ecsEntity.getId());
                }
            }
        }
    };

    public void setDebugMode(boolean active) {
        ecsDebug.setSystemDebugMode(active);
    }

    public void setSystemDebugMode(Class<? extends EcsSystem> system, boolean active) {
        ecsDebug.setSystemDebugMode(system, active);
    }

    public EcsContainer(EcsSettings settings) {
        this.settings = settings;
    }

    //fixme мрак, сделано из-за передачи списка в дебаг. Убрать когда сделаю билдер
    public void init() {
        Array<InputProcessor> processors = new Array<InputProcessor>();
        if (settings.isDebugEnabled) {
            ecsDebug = new EcsDebug(systems);
            processors.add(ecsDebug.getInputProcessor());
            setDebugMode(false); // для красоты, чтобы с консолькой вместе появлялся режим
        }

        initInputMultiplexer((InputProcessor[]) processors.toArray(InputProcessor.class));
    }

    public InputProcessor getInputProcessor() {
        return inputMultiplexer;
    }

    private void initInputMultiplexer(InputProcessor... processors) {
        this.inputMultiplexer = new InputMultiplexer(processors);
    }

    public int createEntity(String type, EcsComponent... components) {
        return entityManager.createEntity(type, components);
    }

    public void deleteEntity(int id) {
        entityManager.deleteEntity(id);
    }

    public void addComponents(int entityId, EcsComponent... components) {
        entityManager.addComponents(entityId, components);
    }

    public void deleteComponents(int entityId, EcsComponent... components) {
        entityManager.deleteComponents(entityId, components);
    }

    public void addSystem(Class<? extends EcsSystem> clazz) {
        try {
            systems.add(clazz.newInstance());
        } catch (Exception e) {
            throw  new RuntimeException(e);
        }
    }

    public void addSystem(EcsSystem system) {
        systems.add(system);
    }

    public void update(float delta) {
        EcsSimple.ECS.update(delta);
        entityManager.update(createEntityEventCallback, deleteEntityEventCallback, addedComponentEventCallback, deletedComponentEventCallback); // коллбеки для очистки нод в системах
        for (EcsSystem system : systems) {
            system.update(delta);
        }

        if (ecsDebug.isDebugActive()) {
            ecsDebug.update(delta);
        }
    }

    @Override
    public void show() {
        for (EcsSystem system : systems) {
            system.show();
        }
    }

    @Override
    public void render(float delta) {
    }

    @Override
    public void resize(int width, int height) {
        for (EcsSystem system : systems) {
            system.resize(width, height);
        }
        if (ecsDebug != null) {
            ecsDebug.resize(width, height);
        }
    }

    @Override
    public void pause() {
        for (EcsSystem system : systems) {
            system.pause();
        }
    }

    @Override
    public void resume() {
        for (EcsSystem system : systems) {
            system.resume();
        }
    }

    @Override
    public void hide() {
        for (EcsSystem system : systems) {
            system.hide();
        }
    }

    @Override
    public void dispose() {
        for (EcsSystem system : systems) {
            system.dispose();
        }
    }

    //todo сделать билдер для корректной инициализации контейнера

}
