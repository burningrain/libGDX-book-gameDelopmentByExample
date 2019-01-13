package com.github.br.ecs.simple.engine.debug;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.utils.Array;
import com.github.br.ecs.simple.engine.IDebugSystem;
import com.github.br.ecs.simple.engine.IEcsSystem;
import com.github.br.ecs.simple.utils.InputProcessorWrapper;

/**
 * Created by user on 08.01.2019.
 */
public class EcsDebug {

    private boolean isDebugActive;
    private InputProcessorWrapper inputProcessorWrapper;

    private DebugService debugService;
    private DebugUI debugUI;

    public EcsDebug(Array<IEcsSystem> systems) {
        debugService = new DebugService(systems);
        debugUI = new DebugUI(debugService);
        inputProcessorWrapper = new InputProcessorWrapper(
                debugUI.getInputProcessor(),
                new InputProcessorWrapper.Predicate() {
                    @Override
                    public boolean apply() {
                        return !isDebugActive;
                    }
                });
    }

    public void addSystem(IDebugSystem system) {
        debugService.addSystem(system);
    }

    public void update(float delta) {
        debugUI.update(delta);
    }

    public boolean isDebugActive() {
        return isDebugActive;
    }

    public void setDebugMode(boolean isActive) {
        this.isDebugActive = isActive;
    }

    public void setDebugMode(Class<? extends IEcsSystem> system, boolean active) {
        debugService.setDebugMode(system, active);
    }

    public InputProcessor getInputProcessor() {
        return inputProcessorWrapper;
    }

}
