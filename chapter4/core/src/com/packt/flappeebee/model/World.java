package com.packt.flappeebee.model;


import com.badlogic.gdx.Gdx;
import com.github.br.ecs.simple.engine.EcsContainer;
import com.github.br.ecs.simple.engine.EcsSettings;
import com.github.br.ecs.simple.engine.debug.console.Command;
import com.github.br.ecs.simple.engine.debug.console.exception.CommandExecutionException;
import com.packt.flappeebee.GamePublisher;
import com.packt.flappeebee.ScreenManager;

import static com.packt.flappeebee.model.LayerEnum.*;


public class World implements ScreenManager, GamePublisher.Subscriber {

    private EcsContainer container;

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
        settings.isConsoleEnabled = true;
        settings.isDebugEnabled = true;

        container = new EcsContainer(settings);
        container.addConsoleCommands(getCommands(container));
        Gdx.input.setInputProcessor(container.getInputProcessor()); //fixme криво, но контроль у клиента

        container.createEntity("background", GameObjectFactory.createBackground());
    }

    private Command[] getCommands(EcsContainer container) {
        return new Command[]{
                createBee(container),
                createPlant(container),
                createCloud(container),
                createCrab(container)
        };
    }

    private Command createCrab(final EcsContainer container) {
        return new Command() {
            @Override
            public void execute(String[] args) throws CommandExecutionException {
                container.createEntity("crab", GameObjectFactory.createCrab());
            }

            @Override
            public String getTitle() {
                return "crab";
            }
        };
    }

    private Command createCloud(final EcsContainer container) {
        return new Command() {
            @Override
            public void execute(String[] args) throws CommandExecutionException {
                for (int i = 0; i < 5; i++) {
                    container.createEntity("cloud", GameObjectFactory.createCloud());
                }
            }

            @Override
            public String getTitle() {
                return "cloud";
            }
        };
    }

    private Command createPlant(final EcsContainer container) {
        return new Command() {
            @Override
            public void execute(String[] args) throws CommandExecutionException {
                int count = Integer.parseInt(args[0]);
                for (int i = 0; i < count; i++) {
                    container.createEntity("plant", GameObjectFactory.createPlant(i + 1));
                }
            }

            @Override
            public String getTitle() {
                return "plant";
            }
        };
    }

    private Command createBee(final EcsContainer container) {
        return new Command() {
            @Override
            public void execute(String[] args) throws CommandExecutionException {
                int count = Integer.parseInt(args[0]);
                for (int i = 0; i < count; i++) {
                    container.createEntity("bee", GameObjectFactory.createFlappee());
                }
            }

            @Override
            public String getTitle() {
                return "bee";
            }
        };
    }


    @Override
    public void update(float delta) {
        if (GamePublisher.self().getCurrentState() == GamePublisher.State.PLAYING) {
            container.update(delta);
        }
    }


}
