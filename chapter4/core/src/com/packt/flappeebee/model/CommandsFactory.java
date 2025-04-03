package com.packt.flappeebee.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.github.br.ecs.simple.engine.EcsContainer;
import com.github.br.gdx.simple.console.Command;
import com.github.br.gdx.simple.console.exception.CommandExecutionException;
import com.github.br.gdx.simple.structure.GameManager;
import com.github.br.gdx.simple.structure.screen.statemachine.GameScreenState;
import com.packt.flappeebee.screen.GameScreens;
import com.packt.flappeebee.screen.level.level.gameloop.GameLoopManager;

/**
 * Created by user on 04.01.2019.
 */
public final class CommandsFactory {

    private CommandsFactory() {
    }

    public static Command[] getCommands(EcsContainer container, GameLoopManager gameLoopManager, GameManager gameManager) {
        return new Command[]{
                createChangeGameCycleStrategy(gameLoopManager),
                createChangeFPS(),
                createChangeLevel(gameManager)
        };
    }

    private static Command createChangeLevel(final GameManager gameManager) {
        return new Command() {
            @Override
            public String execute(String[] args) throws CommandExecutionException {
                int level = Integer.parseInt(args[0]);
                GameScreenState newState;
                switch (level) {
                    case 0:
                        newState = GameScreens.LEVEL_0;
                        break;
                    case 1:
                        newState = GameScreens.LEVEL_1;
                        break;
                    default:
                        throw new GdxRuntimeException("level [" + level + "] is not found");
                }
                gameManager.screenStateManager.changeCurrentState(newState);
                return "";
            }

            @Override
            public String getTitle() {
                return "level";
            }

            @Override
            public String help() {
                return "";
            }
        };
    }

    private static Command createChangeFPS() {
        return new Command() {

            @Override
            public String execute(String[] args) throws CommandExecutionException {
                int fps = Integer.parseInt(args[0]);
                Gdx.graphics.setForegroundFPS(fps);
                return "";
            }

            @Override
            public String getTitle() {
                return "fps";
            }

            @Override
            public String help() {
                return "";
            }
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
