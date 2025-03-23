package com.github.br.simple.input.processor;

public class InputData {

    public final InputKey inputKey;
    public final InputMouse inputMouse;
    public final InputTouch inputTouch;

    public boolean isChanged = false;

    public InputData() {
        this.inputKey = new InputKey();
        this.inputMouse = new InputMouse();
        this.inputTouch = new InputTouch();
    }

    public void reset() {
        inputKey.reset();
        inputMouse.reset();
        inputTouch.reset();

        isChanged = false;
    }

}
