package com.packt.snake;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;

import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by user on 28.02.2017.
 */
public class InputManager implements ScreenManager {

    private ConcurrentHashMap<Class, Handler> handlers = new ConcurrentHashMap<>();

    @Override
    public void render(float delta) {
        boolean pressed = false;
        switch (Gdx.app.getType()) {
            case Desktop:
                pressed = Gdx.input.isKeyPressed(Input.Keys.ANY_KEY);
                break;
            case Android:
                pressed = Gdx.input.justTouched();
                break;
        }
        if(pressed){
            notifyHandlers();
        }
    }

    public void addHandler(Handler handler){
        handlers.put(handler.getClass(), handler);
    }

    public void removeHandler(Handler handler){
        handlers.remove(handler.getClass());
    }

    private void notifyHandlers(){
        for(Handler handler : handlers.values()){
            handler.handleInput();
        }
    }


    public interface Handler {
        void handleInput();
    }

}
