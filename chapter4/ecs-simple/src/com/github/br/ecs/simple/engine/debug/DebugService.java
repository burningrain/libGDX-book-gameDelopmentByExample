package com.github.br.ecs.simple.engine.debug;


import com.badlogic.gdx.utils.Array;
import com.github.br.ecs.simple.engine.IDebugSystem;
import com.github.br.ecs.simple.engine.IEcsSystem;

import java.util.LinkedHashMap;

import static java.lang.String.format;


/**
 * Сервис, выводящий на экран отладочную информацию.
 */
public class DebugService {


    private LinkedHashMap<Class<? extends IDebugSystem>, IDebugSystem> systems = new LinkedHashMap<Class<? extends IDebugSystem>, IDebugSystem>();
    private Array<IDebugSystem> systemsList = new Array<IDebugSystem>();

    public DebugService(Array<IEcsSystem> systems) {
        for (IDebugSystem iDebugSystem : filterDebugSystems(systems)) {
            addSystem(iDebugSystem);
        }
    }

    public void addSystem(IDebugSystem system) {
        systems.put(system.getClass(), system);
        systemsList.add(system);
        setDebugMode(system.getClass(), true);
    }

    public void setDebugMode(Class<? extends IEcsSystem> system, boolean active) {
        IEcsSystem ecsSystem = systems.get(system);
        if (ecsSystem == null) throw new IllegalArgumentException(format("Система '%s' не найдена", system));
        if (ecsSystem instanceof IDebugSystem) {
            ((IDebugSystem) ecsSystem).setDebugMode(active);
        } else {
            System.out.println(format("Система '%s' не поддерживает режим отладки", system)); //todo логгер впилить
        }
    }

    public Array<IDebugSystem> getSystems() {
        return systemsList;
    }

    public boolean isSystemActive(Class<? extends IEcsSystem> key) {
        return systems.get(key).isDebugMode();
    }

    private static Array<IDebugSystem> filterDebugSystems(Array<IEcsSystem> systems) {
        Array<IDebugSystem> result = new Array<IDebugSystem>();
        for (IEcsSystem system : systems) {
            if (system instanceof IDebugSystem) result.add((IDebugSystem) system);
        }
        return result;
    }

}
