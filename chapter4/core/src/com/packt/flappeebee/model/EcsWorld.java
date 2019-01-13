package com.packt.flappeebee.model;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.github.br.ecs.simple.engine.EcsContainer;
import com.github.br.ecs.simple.engine.EcsSettings;
import com.github.br.ecs.simple.engine.debug.drawobject.DebugDrawObject;
import com.github.br.ecs.simple.system.animation.AnimationSystem;
import com.github.br.ecs.simple.system.physics.PhysicsSystem;
import com.github.br.ecs.simple.system.render.RenderSystem;
import com.github.br.ecs.simple.system.script.ScriptSystem;
import com.github.br.ecs.simple.utils.ViewHelper;
import com.github.br.gdx.simple.console.Console;
import com.github.br.gdx.simple.console.ConsoleOffOnCallback;

import static com.packt.flappeebee.model.LayerEnum.*;


public class EcsWorld {

    private EcsContainer container;
    private Console console;

    public EcsWorld() {
        //TODO
        initEcsContainer();
        initConsole();
        setInputProcessor();

        container.createEntity("background", GameObjectFactory.createBackground());
    }

    private void initConsole() {
        //todo пока так, потом подумать с местом инициализации. Плохо, что завязка на скин в ecs-контейнере
        console = new Console(Input.Keys.F9, DebugDrawObject.DEFAULT_SKIN, ViewHelper.viewport, new ConsoleOffOnCallback() {
            @Override
            public void call(boolean isActive) {
                container.setDebugMode(isActive);
            }
        });
        console.addCommands(CommandsFactory.getCommands(container));
    }

    private void initEcsContainer() {
        // инициализация настроек
        EcsSettings settings = new EcsSettings();
        settings.layers = new String[]{
                BACKGROUND.name(),
                PRE_BACKGROUND.name(),
                BACK_EFFECTS.name(),
                MAIN_LAYER.name(),
                FRONT_EFFECTS.name()
        };
        settings.isDebugEnabled = true;

        container = new EcsContainer(settings);

        // инициализация систем. Порядок очень важен!
        container.addSystem(ScriptSystem.class);
        container.addSystem(PhysicsSystem.class);
        container.addSystem(AnimationSystem.class);
        container.addSystem(new RenderSystem(settings.layers));
        container.init();
    }

    private void setInputProcessor() {
        InputMultiplexer inputMultiplexer = new InputMultiplexer();
        inputMultiplexer.addProcessor(console.getInputProcessor());
        inputMultiplexer.addProcessor(container.getInputProcessor());
        Gdx.input.setInputProcessor(inputMultiplexer); //fixme криво, но зато контроль у клиента
    }

    public void update(float delta) {
        container.update(delta);
        console.update(delta);
    }

}