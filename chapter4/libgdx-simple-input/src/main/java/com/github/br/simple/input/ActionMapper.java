package com.github.br.simple.input;

import com.badlogic.gdx.controllers.Controller;
import com.github.br.simple.input.processor.InputData;

public interface ActionMapper {

    int getAction(float deltaTime, InputData inputData, Controller controller);

}
