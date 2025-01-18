package com.github.br.paper.airplane.ecs.system;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.github.br.paper.airplane.ecs.component.InputComponent;

public class InputSystem extends EntitySystem {

    private final InputComponent inputComponent = new InputComponent();

    private long lastTime;

    public InputComponent getInputComponent() {
        return inputComponent;
    }

    private final InputAdapter inputAdapter = new InputAdapter() {

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

        inputComponent.isPressedDown = false;
        return true;
    }

    private boolean pressDown() {
        if (!inputComponent.isInputTurnOn()) {
            return true;
        }

        inputComponent.isPressedDown = true;
        long now = System.currentTimeMillis();
        inputComponent.isFire = ((float) (now - lastTime)) < 200;
        lastTime = now;

        return true;
    }

    @Override
    public void addedToEngine(Engine engine) {
        Gdx.input.setInputProcessor(inputAdapter);
    }

    @Override
    public void removedFromEngine(Engine engine) {
        Gdx.input.setInputProcessor(null);
    }

    @Override
    public void update(float deltaTime) {

    }

}
