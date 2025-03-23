package com.github.br.simple.input.processor;

import com.badlogic.gdx.InputProcessor;
import com.github.br.simple.input.processor.mouse.MouseMoved;
import com.github.br.simple.input.processor.mouse.Scrolled;
import com.github.br.simple.input.processor.touch.TouchCancelled;
import com.github.br.simple.input.processor.touch.TouchDown;
import com.github.br.simple.input.processor.touch.TouchDragged;
import com.github.br.simple.input.processor.touch.TouchUp;

public class InputProcessorImpl implements InputProcessor {

    private final InputData inputData = new InputData();

    public InputData getInputData() {
        return inputData;
    }

    @Override
    public boolean keyDown(int keycode) {
        inputData.inputKey.keyDown = keycode;
        inputData.inputKey.isChanged = true;
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        inputData.inputKey.keyUp = keycode;
        inputData.inputKey.isChanged = true;
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        inputData.inputKey.keyTyped = character;
        inputData.inputKey.isChanged = true;
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        TouchDown touchDown = inputData.inputTouch.touchDown;
        touchDown.screenX = screenX;
        touchDown.screenY = screenY;
        touchDown.pointer = pointer;
        touchDown.button = button;

        touchDown.isChanged = true;
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        TouchUp touchUp = inputData.inputTouch.touchUp;
        touchUp.screenX = screenX;
        touchUp.screenY = screenY;
        touchUp.pointer = pointer;
        touchUp.button = button;

        touchUp.isChanged = true;
        return false;
    }

    @Override
    public boolean touchCancelled(int screenX, int screenY, int pointer, int button) {
        TouchCancelled touchCancelled = inputData.inputTouch.touchCancelled;
        touchCancelled.screenX = screenX;
        touchCancelled.screenY = screenY;
        touchCancelled.pointer = pointer;
        touchCancelled.button = button;

        touchCancelled.isChanged = true;
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        TouchDragged touchDragged = inputData.inputTouch.touchDragged;
        touchDragged.screenX = screenX;
        touchDragged.screenY = screenY;
        touchDragged.pointer = pointer;

        touchDragged.isChanged = true;
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        MouseMoved mouseMoved = inputData.inputMouse.mouseMoved;
        mouseMoved.screenX = screenX;
        mouseMoved.screenY = screenY;

        mouseMoved.isChanged = true;
        return false;
    }

    @Override
    public boolean scrolled(float amountX, float amountY) {
        Scrolled scrolled = inputData.inputMouse.scrolled;
        scrolled.amountX = amountX;
        scrolled.amountY = amountY;

        scrolled.isChanged = true;
        return false;
    }

}
