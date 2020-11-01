package com.github.br.gdx.simple.console;

import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

/**
 * Created by user on 03.01.2019.
 */
public class Console extends ScreenAdapter {

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

    public Console(int activationKeyCode, Skin skin, ConsoleOffOnCallback offOnCallback) {
        this(activationKeyCode, skin, new ScreenViewport(), offOnCallback);
    }

    public Console(int activationKeyCode, Skin skin, boolean isActiveDebugData, ConsoleOffOnCallback offOnCallback) {
        this(activationKeyCode, skin, new ScreenViewport(), isActiveDebugData, offOnCallback);
    }

    public Console(int activationKeyCode, Skin skin, Viewport viewport, ConsoleOffOnCallback offOnCallback) {
        this(activationKeyCode, skin, viewport, offOnCallback, false, new ConsolePlugin[0]);
    }

    public Console(int activationKeyCode, Skin skin, Viewport viewport, boolean isShowDebugData, ConsoleOffOnCallback offOnCallback) {
        this(activationKeyCode, skin, viewport, offOnCallback, isShowDebugData, new ConsolePlugin[0]);
    }

    public Console(int activationKeyCode, Skin skin, Viewport viewport,
                   ConsoleOffOnCallback offOnCallback, boolean isShowDebugData, ConsolePlugin... plugins) {
        this.offOnCallback = offOnCallback;
        this.plugins = plugins;

        consoleService = new ConsoleService();
        consoleUI = new ConsoleUI(consoleService, skin, viewport, isShowDebugData);

        ActivateConsoleInputProcessor activateConsoleInputProcessor = new ActivateConsoleInputProcessor(activationKeyCode);
        InputProcessorWrapper inputProcessorWrapper = createInputProcessorWrapper(consoleUI.getInputProcessor());

        InputProcessor[] processors = new InputProcessor[2 + plugins.length];
        processors[0] = activateConsoleInputProcessor;
        processors[1] = inputProcessorWrapper;
        for (int i = 0; i < plugins.length; i++) {
            processors[2 + i] = createInputProcessorWrapper(plugins[i].getInputProcessor());
        }
        inputProcessor = new InputMultiplexer(
                activateConsoleInputProcessor,
                inputProcessorWrapper
        );
    }

    private InputProcessorWrapper createInputProcessorWrapper(InputProcessor inputProcessor) {
        return new InputProcessorWrapper(inputProcessor, predicate);
    }

    @Override
    public void render(float delta) {
        if (isConsoleActive) {
            consoleUI.render(delta);
            for (ConsolePlugin plugin : plugins) {
                plugin.render(delta);
            }
        }
    }

    @Override
    public void resize(int width, int height) {
        consoleUI.resize(width, height);
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
