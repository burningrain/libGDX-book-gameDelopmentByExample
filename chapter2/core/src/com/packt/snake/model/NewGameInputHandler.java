package com.packt.snake.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.packt.snake.GamePublisher;
import com.packt.snake.InputManager;

/**
 * Created by user on 11.03.2017.
 */
public class NewGameInputHandler implements InputManager.Handler, GamePublisher.Subscriber {

    private boolean active; // активен только когда игра окончена

    @Override
    public void handleInput() {
        boolean pressed = false;
        switch (Gdx.app.getType()) {
            case Desktop:
                pressed = Gdx.input.isKeyPressed(Input.Keys.SPACE);
                break;
            case Android:
                pressed = Gdx.input.justTouched();
                break;
        }

        if (pressed && active) {
            GamePublisher.self().changeState(GamePublisher.State.NEW_GAME);
        }
    }


    @Override
    public void handleGameState(GamePublisher.State state) {
        active = true;
    }
}
