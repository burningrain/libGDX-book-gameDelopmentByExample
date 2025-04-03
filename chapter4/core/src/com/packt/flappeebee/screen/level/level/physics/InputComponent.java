package com.packt.flappeebee.screen.level.level.physics;

import com.github.br.ecs.simple.engine.EcsComponent;
import com.github.br.simple.input.InputActions;

public class InputComponent implements EcsComponent {
    public InputActions inputActions;
    public InputComponent(InputActions inputActions) {
        this.inputActions = inputActions;
    }
}
