package com.github.br.ecs.simple.utils;

import com.badlogic.gdx.InputProcessor;

/**
 * Created by user on 02.01.2019.
 */
public class InputProcessorWrapper implements InputProcessor {

    private InputProcessor inputProcessor;
    private Predicate predicate;

    public InputProcessorWrapper(InputProcessor inputProcessor, Predicate predicate) {
        this.inputProcessor = inputProcessor;
        this.predicate = predicate;
    }

    @Override
    public boolean keyDown(int keycode) {
        if(predicate.apply()) {
            return false;
        }
        return inputProcessor.keyDown(keycode);
    }

    @Override
    public boolean keyUp(int keycode) {
        if(predicate.apply()) {
            return false;
        }
        return inputProcessor.keyUp(keycode);
    }

    @Override
    public boolean keyTyped(char character) {
        if(predicate.apply()) {
            return false;
        }
        return inputProcessor.keyTyped(character);
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        if(predicate.apply()) {
            return false;
        }
        return inputProcessor.touchDown(screenX, screenY, pointer, button);
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        if(predicate.apply()) {
            return false;
        }
        return inputProcessor.touchUp(screenX, screenY, pointer, button);
    }

    @Override
    public boolean touchCancelled(int screenX, int screenY, int pointer, int button) {
        if(predicate.apply()) {
            return false;
        }
        return inputProcessor.touchCancelled(screenX, screenY, pointer, button);
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        if(predicate.apply()) {
            return false;
        }
        return inputProcessor.touchDragged(screenX, screenY, pointer);
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        if(predicate.apply()) {
            return false;
        }
        return inputProcessor.mouseMoved(screenX, screenY);
    }

    @Override
    public boolean scrolled (float amountX, float amountY) {
        if(predicate.apply()) {
            return false;
        }
        return inputProcessor.scrolled(amountX, amountY);
    }

    public interface Predicate {
        boolean apply();
    }

}
