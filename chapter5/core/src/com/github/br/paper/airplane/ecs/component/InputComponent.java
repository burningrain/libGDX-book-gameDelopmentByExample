package com.github.br.paper.airplane.ecs.component;

import com.badlogic.ashley.core.Component;

public class InputComponent implements Component {

    private boolean isInputTurnOn = true;

    public boolean isPressedUp;
    public boolean isPressedDown;
    public boolean isClick;

    public boolean isDoubleClick;

    public void turnOn() {
        isInputTurnOn = true;
        reset();
    }

    public void turnOff() {
        isInputTurnOn = false;
        reset();
    }

    public boolean isInputTurnOn() {
        return isInputTurnOn;
    }

    private void reset() {
        isPressedUp = false;
        isPressedDown = false;
        isClick = false;
        isDoubleClick = false;
    }

}
