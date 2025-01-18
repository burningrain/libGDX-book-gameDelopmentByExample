package com.github.br.paper.airplane.ecs.component;

import com.badlogic.ashley.core.Component;

public class InputComponent implements Component {

    private boolean isInputTurnOn = true;

    public boolean isPressedDown;
    public boolean isFire;

    public void turnOn() {
        isInputTurnOn = true;
        isPressedDown = false;
        this.isFire = false;
    }

    public void turnOff() {
        isInputTurnOn = false;
        isPressedDown = false;
        this.isFire = false;
    }

    public boolean isInputTurnOn() {
        return isInputTurnOn;
    }

}
