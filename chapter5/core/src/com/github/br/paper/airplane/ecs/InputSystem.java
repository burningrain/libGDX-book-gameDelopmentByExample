package com.github.br.paper.airplane.ecs;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;

public class InputSystem extends EntitySystem {

    private final InputAdapter inputAdapter = new InputAdapter() {

        public boolean touchDown(int screenX, int screenY, int pointer, int button) {
            return true;
        }

        public boolean keyDown(int keycode) {
            return true;
        }

    };

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
