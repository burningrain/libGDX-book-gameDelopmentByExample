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
import com.github.br.gdx.simple.console.Command;
import com.github.br.gdx.simple.console.Console;
import com.packt.flappeebee.GamePublisher;
import com.packt.flappeebee.ScreenManager;

import static com.packt.flappeebee.model.LayerEnum.*;


public class World implements ScreenManager, GamePublisher.Subscriber {

    private EcsContainer container;
    //todo пока так, потом подумать с местом инициализации. Плохо, что завязка на скин в ecs-контейнере
    private Console console = new Console(Input.Keys.F9, DebugDrawObject.DEFAULT_SKIN, ViewHelper.viewport);

    public World() {
        GamePublisher.self().addListener(GamePublisher.State.NEW_GAME, this);
    }

    @Override
    public void handleGameState(GamePublisher.State state) {
        startNewGame();
        GamePublisher.self().changeState(GamePublisher.State.PLAYING);
    }

    public void startNewGame() {
        //TODO
        EcsSettings settings = new EcsSettings();
        settings.layers = new String[]{
                BACKGROUND.name(),
                PRE_BACKGROUND.name(),
                BACK_EFFECTS.name(),
                MAIN_LAYER.name(),
                FRONT_EFFECTS.name()
        };
        settings.isDebugEnabled = true;

        initEcsContainer(settings);
        initConsole(CommandsFactory.getCommands(container));
        setInputProcessor();

        container.createEntity("background", GameObjectFactory.createBackground());
    }

    private void initConsole(Command[] commands) {
        console.addCommands(commands);
    }

    private void initEcsContainer(EcsSettings settings) {
        container = new EcsContainer(settings);
        // инициализация систем. Порядок очень важен!
        container.addSystem(ScriptSystem.class);
        container.addSystem(PhysicsSystem.class);
        container.addSystem(AnimationSystem.class);
        container.addSystem(new RenderSystem(settings.layers));

    }

    private void setInputProcessor() {
        InputMultiplexer inputMultiplexer = new InputMultiplexer();
        inputMultiplexer.addProcessor(console.getInputProcessor());
        inputMultiplexer.addProcessor(container.getInputProcessor());
        Gdx.input.setInputProcessor(inputMultiplexer); //fixme криво, но зато контроль у клиента
    }


    @Override
    public void update(float delta) {
        if (GamePublisher.self().getCurrentState() == GamePublisher.State.PLAYING) {
            container.update(delta);
        }
        console.update(delta);
    }

}
