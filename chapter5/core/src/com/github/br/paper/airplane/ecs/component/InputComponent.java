package com.github.br.paper.airplane.ecs.component;

import com.badlogic.ashley.core.Component;

public class InputComponent implements Component {

    private boolean isInputTurnOn = true;

    public boolean isPressedUp;
    public boolean isPressedDown;
    public boolean isFire;

    public void turnOn() {
        isInputTurnOn = true;
        isPressedUp = false;
        isPressedDown = false;
        this.isFire = false;
    }

    public void turnOff() {
        isPressedUp = false;
        isInputTurnOn = false;
        isPressedDown = false;
        this.isFire = false;
    }

    public boolean isInputTurnOn() {
        return isInputTurnOn;
    }

}
