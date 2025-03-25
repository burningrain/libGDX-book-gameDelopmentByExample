package com.github.br.simple.input;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.utils.Disposable;
import com.github.br.simple.input.controller.ControllerProxy;
import com.github.br.simple.input.processor.InputProcessorImpl;

public class InputSystem implements Disposable {

    private final ControllerProxy controllerProxy;
    private final InputProcessorImpl inputProcessor;
    private final InputActions inputActions;
    private final ActionMapper actionMapper;
    private boolean isInputTurnOn = true;

    public InputSystem(int actionsSize, ActionMapper actionMapper) {
        this.actionMapper = actionMapper;
        inputActions = new InputActions(actionsSize);
        controllerProxy = ControllerProxy.INSTANCE;
        inputProcessor = new InputProcessorImpl();
    }

    public InputActions getInputActions() {
        return inputActions;
    }

    public InputProcessor getInputProcessor() {
        return inputProcessor;
    }

    public void update(float deltaTime) {
        int action = actionMapper.getAction(deltaTime, inputProcessor.getInputData(), controllerProxy);
        if (action >= 0) {
            inputActions.setAction(action);
        } else if (action == ActionConst.EMPTY_ACTION) {
            inputActions.clear();
        }
        inputProcessor.getInputData().reset();
    }

    public void reset() {
        inputActions.clear();
    }

    public boolean isActionActive(int action) {
        if (!isInputTurnOn) {
            return false;
        }
        return this.inputActions.getAction(action);
    }

    public void turnOn() {
        isInputTurnOn = true;
        reset();
    }

    public void turnOff() {
        isInputTurnOn = false;
        reset();
    }

    public boolean isTurnOn() {
        return isInputTurnOn;
    }

    @Override
    public void dispose() {
        controllerProxy.dispose();
    }
}
