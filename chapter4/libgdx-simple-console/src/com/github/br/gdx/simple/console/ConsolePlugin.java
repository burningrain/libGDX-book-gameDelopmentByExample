package com.github.br.gdx.simple.console;

import com.badlogic.gdx.InputProcessor;

/**
 * Created by user on 07.01.2019.
 */
public interface ConsolePlugin {

    void render(float delta);

    InputProcessor getInputProcessor();

}
