package com.github.br.simple.input;

public class MouseInput {

    public boolean isPressedUp;
    public boolean isPressedDown;
    public boolean isClick;
    public boolean isDoubleClick;

    private void reset() {
        isPressedUp = false;
        isPressedDown = false;
        isClick = false;
        isDoubleClick = false;
    }

}
