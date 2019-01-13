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
    private ConsoleOffOnCallback offOnCallback;
    private ConsolePlugin[] plugins;

    private InputProcessorWrapper.Predicate predicate = new InputProcessorWrapper.Predicate() {
        @Override
        public boolean apply() {
            return !isConsoleActive;
        }
    };

    private ConsoleService consoleService;
    private ConsoleUI consoleUI;

    public Console(int activationKeyCode, Skin skin, Viewport viewport) {
        this(activationKeyCode, skin, viewport, null);
    }

    public Console(int activationKeyCode, Skin skin, Viewport viewport, ConsoleOffOnCallback offOnCallback) {
        this(activationKeyCode, skin, viewport, offOnCallback, new ConsolePlugin[0]);
    }

    public Console(int activationKeyCode, Skin skin, Viewport viewport,
                   ConsoleOffOnCallback offOnCallback, ConsolePlugin... plugins) {
        this.offOnCallback = offOnCallback;
        this.plugins = plugins;

        consoleService = new ConsoleService();
        consoleUI = new ConsoleUI(consoleService, skin, viewport);

        ActivateConsoleInputProcessor activateConsoleInputProcessor = new ActivateConsoleInputProcessor(activationKeyCode);
        InputProcessorWrapper inputProcessorWrapper = createInputProcessorWrapper(consoleUI.getInputProcessor());

        InputProcessor[] processors = new InputProcessor[2 + plugins.length];
        processors[0] = activateConsoleInputProcessor;
        processors[1] = inputProcessorWrapper;
        for (int i = 0; i < plugins.length; i++) {
            processors[2 + i] = createInputProcessorWrapper(plugins[i].getInputProcessor());
        }
        inputProcessor = new InputMultiplexer(new InputProcessor[]{
                activateConsoleInputProcessor,
                inputProcessorWrapper
        });
    }

    private InputProcessorWrapper createInputProcessorWrapper(InputProcessor inputProcessor) {
        return new InputProcessorWrapper(inputProcessor, predicate);
    }

    public void update(float delta) {
        if (isConsoleActive) {
            consoleUI.update(delta);
            for (ConsolePlugin plugin : plugins) {
                plugin.update(delta);
            }
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
                if (offOnCallback != null) {
                    offOnCallback.call(isConsoleActive);
                }
                return true;
            }
            return false;
        }

    }

}
