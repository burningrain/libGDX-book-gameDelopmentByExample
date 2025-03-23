package com.github.br.simple.input.processor.touch;

import com.github.br.simple.input.ActionConst;

public class TouchUp {

    public int screenX;
    public int screenY;
    public int pointer;
    public int button;

    public boolean isChanged = false;

    public TouchUp() {
        reset();
    }

    public void reset() {
        screenX = ActionConst.EMPTY_DATA;
        screenY = ActionConst.EMPTY_DATA;
        pointer = ActionConst.EMPTY_DATA;
        button = ActionConst.EMPTY_DATA;

        isChanged = false;
    }
}
