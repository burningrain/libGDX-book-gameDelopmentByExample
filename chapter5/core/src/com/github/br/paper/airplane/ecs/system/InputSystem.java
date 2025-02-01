package com.github.br.paper.airplane.ecs.system;

import com.badlogic.gdx.InputAdapter;
import com.github.br.paper.airplane.ecs.component.InputComponent;

public class InputSystem {

    private final InputComponent inputComponent = new InputComponent();

    private long lastTime;

    public InputComponent getInputComponent() {
        return inputComponent;
    }

    public final InputAdapter inputAdapter = new InputAdapter() {

        @Override
        public boolean touchDown(int screenX, int screenY, int pointer, int button) {
            return pressDown();
        }

        @Override
        public boolean touchUp(int screenX, int screenY, int pointer, int button) {
            return pressUp();
        }

        @Override
        public boolean keyDown(int keycode) {
            return pressDown();
        }

        @Override
        public boolean keyUp(int keycode) {
            return pressUp();
        }

    };

    private boolean pressUp() {
        if (!inputComponent.isInputTurnOn()) {
            return true;
        }

        inputComponent.isPressedUp = true;
        inputComponent.isPressedDown = false;
        return true;
    }

    private boolean pressDown() {
        if (!inputComponent.isInputTurnOn()) {
            return true;
        }

        inputComponent.isPressedDown = true;
        inputComponent.isClick = true;
        long now = System.currentTimeMillis();
        inputComponent.isDoubleClick = ((float) (now - lastTime)) < 200;
        lastTime = now;

        return true;
    }

    public void postInput() {
        inputComponent.isPressedUp = false;

        inputComponent.isClick = false;
        inputComponent.isDoubleClick = false;
    }

}
