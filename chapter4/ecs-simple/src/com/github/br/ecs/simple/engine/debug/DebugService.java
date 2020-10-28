package com.github.br.ecs.simple.engine.debug;


import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.OrderedMap;
import com.github.br.ecs.simple.engine.EcsSystem;
import com.github.br.ecs.simple.engine.DebugSystem;

import static java.lang.String.format;


/**
 * Сервис, выводящий на экран отладочную информацию.
 */
public class DebugService {

    private OrderedMap<Class<? extends EcsSystem>, EcsSystem> systems = new OrderedMap<Class<? extends EcsSystem>, EcsSystem>();
    private Array<DebugSystem> systemsList = new Array<DebugSystem>();

    public DebugService(Array<EcsSystem> systems) {
        for (DebugSystem debugSystem : filterDebugSystems(systems)) {
            addSystem(debugSystem);
        }
    }

    public void addSystem(DebugSystem system) {
        systems.put(system.getClass(), system);
        systemsList.add(system);
        setDebugMode(system.getClass(), true);
    }

    public void setDebugMode(Class<? extends EcsSystem> system, boolean active) {
        EcsSystem ecsSystem = systems.get(system);
        if (ecsSystem == null) throw new IllegalArgumentException(format("Система '%s' не найдена", system));
        if (ecsSystem instanceof DebugSystem) {
            ((DebugSystem) ecsSystem).setDebugMode(active);
        } else {
            System.out.println(format("Система '%s' не поддерживает режим отладки", system)); //todo логгер впилить
        }
    }

    public Array<DebugSystem> getSystems() {
        return systemsList;
    }

    public boolean isSystemActive(Class<? extends EcsSystem> ecsSystem) {
        if (!DebugSystem.class.isAssignableFrom(ecsSystem)) {
            System.out.println(format("Система '%s' не поддерживает режим отладки, не может быть активна", ecsSystem)); //todo логгер впилить
            return false;
        }
        return ((DebugSystem)systems.get(ecsSystem)).isDebugMode();
    }

    private Array<DebugSystem> filterDebugSystems(Array<EcsSystem> systems) {
        Array<DebugSystem> result = new Array<DebugSystem>();
        for (EcsSystem system : systems) {
            if (system instanceof DebugSystem) result.add((DebugSystem) system);
        }
        return result;
    }

}
