package com.github.br.gdx.simple.console;

import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.viewport.Viewport;

/**
 * Created by user on 03.01.2019.
 */
public class Console {

    private boolean isConsoleActive;
    private InputProcessor inputProcessor;

    private ConsoleService consoleService;
    private ConsoleUI consoleUI;

    public Console(int activationKeyCode, Skin skin, Viewport viewport) {
        consoleService = new ConsoleService();
        consoleUI = new ConsoleUI(consoleService, skin, viewport);

        ActivateConsoleInputProcessor activateConsoleInputProcessor = new ActivateConsoleInputProcessor(activationKeyCode);
        InputProcessorWrapper inputProcessorWrapper = new InputProcessorWrapper(consoleUI.getInputProcessor(),
                new InputProcessorWrapper.Predicate() {
                    @Override
                    public boolean apply() {
                        return !isConsoleActive;
                    }
                });
        inputProcessor = new InputMultiplexer(new InputProcessor[]{
                activateConsoleInputProcessor,
                inputProcessorWrapper
        });
    }

    public void update(float delta) {
        if(isConsoleActive) {
            consoleUI.update(delta);
        }
    }

    public InputProcessor getInputProcessor() {
        return inputProcessor;
    }

    public void addCommands(Command[] commands) {
        consoleService.addCommands(commands);
    }


    private class ActivateConsoleInputProcessor extends InputAdapter {

        private final int activationKeyCode;

        public ActivateConsoleInputProcessor(int activationKeyCode) {
            this.activationKeyCode = activationKeyCode;
        }

        @Override
        public boolean keyDown(int keycode) {
            if (activationKeyCode == keycode) {
                isConsoleActive = !isConsoleActive;
                return true;
            }
            return false;
        }

    }

}
