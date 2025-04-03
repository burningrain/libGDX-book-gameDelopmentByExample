package com.packt.flappeebee.model;

import com.github.br.ecs.simple.engine.EcsContainer;
import com.github.br.gdx.simple.console.Command;
import com.github.br.gdx.simple.console.exception.CommandExecutionException;
import com.packt.flappeebee.screen.level.level.gameloop.GameLoopManager;

/**
 * Created by user on 04.01.2019.
 */
public final class CommandsFactory {

    private CommandsFactory() {
    }

    public static Command[] getCommands(EcsContainer container, GameLoopManager gameLoopManager) {
        return new Command[]{
                createChangeGameCycleStrategy(gameLoopManager),
        };
    }

    private static Command createChangeGameCycleStrategy(final GameLoopManager gameLoopManager) {
        return new Command() {
            @Override
            public String execute(String[] args) throws CommandExecutionException {
                int variant = Integer.parseInt(args[0]);
                gameLoopManager.changeStrategy(variant);
                return "";
            }

            @Override
            public String getTitle() {
                return "loop";
            }

            @Override
            public String help() {
                return "";
            }
        };
    }

}
