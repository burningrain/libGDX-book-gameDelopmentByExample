package com.packt.flappeebee.model;

import com.github.br.ecs.simple.engine.EcsContainer;
import com.github.br.ecs.simple.system.physics.PhysicsSystem;
import com.github.br.gdx.simple.console.Command;
import com.github.br.gdx.simple.console.exception.CommandExecutionException;

/**
 * Created by user on 04.01.2019.
 */
public final class CommandsFactory {

    private CommandsFactory() {
    }

    public static Command[] getCommands(EcsContainer container, PhysicsSystem physicsSystem) {
        return new Command[]{
                createBee(container),
                createPlant(container),
                createCloud(container),
                createCrab(container),

                createChangeGameCycleStrategy(physicsSystem)
        };
    }

    private static Command createChangeGameCycleStrategy(final PhysicsSystem physicsSystem) {
        return new Command() {
            @Override
            public String execute(String[] args) throws CommandExecutionException {
                int variant = Integer.parseInt(args[0]);
                physicsSystem.changeStrategy(variant);
                return "";
            }

            @Override
            public String getTitle() {
                return "cycle";
            }

            @Override
            public String help() {
                return "";
            }
        };
    }

    private static Command createCrab(final EcsContainer container) {
        return new Command() {
            @Override
            public String execute(String[] args) throws CommandExecutionException {
                container.createEntity("crab", GameObjectFactory.createCrab());
                return "";
            }

            @Override
            public String getTitle() {
                return "crab";
            }

            @Override
            public String help() {
                return null;
            }
        };
    }

    private static Command createCloud(final EcsContainer container) {
        return new Command() {
            @Override
            public String execute(String[] args) throws CommandExecutionException {
                int count = Integer.parseInt(args[0]);
                for (int i = 0; i < count; i++) {
                    container.createEntity("cloud", GameObjectFactory.createCloud());
                }
                return "";
            }

            @Override
            public String getTitle() {
                return "cloud";
            }

            @Override
            public String help() {
                return null;
            }
        };
    }

    private static Command createPlant(final EcsContainer container) {
        return new Command() {
            @Override
            public String execute(String[] args) throws CommandExecutionException {
                int count = Integer.parseInt(args[0]);
                for (int i = 0; i < count; i++) {
                    container.createEntity("plant", GameObjectFactory.createPlant(i + 1));
                }
                return "";
            }

            @Override
            public String getTitle() {
                return "plant";
            }

            @Override
            public String help() {
                return null;
            }
        };
    }

    private static Command createBee(final EcsContainer container) {
        return new Command() {
            @Override
            public String execute(String[] args) throws CommandExecutionException {
                int count = Integer.parseInt(args[0]);
                for (int i = 0; i < count; i++) {
                    container.createEntity("bee", GameObjectFactory.createFlappee());
                }
                return "";
            }

            @Override
            public String getTitle() {
                return "bee";
            }

            @Override
            public String help() {
                return null;
            }
        };
    }

}
