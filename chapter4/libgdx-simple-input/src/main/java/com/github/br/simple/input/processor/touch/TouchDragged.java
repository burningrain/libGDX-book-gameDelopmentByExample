package com.github.br.simple.input.processor.touch;

import com.github.br.simple.input.ActionConst;

public class TouchDragged {

    public int screenX;
    public int screenY;
    public int pointer;

    public boolean isChanged = false;

    public TouchDragged() {
        reset();
    }

    public void reset() {
        screenX = ActionConst.EMPTY_DATA;
        screenY = ActionConst.EMPTY_DATA;
        pointer = ActionConst.EMPTY_DATA;

        isChanged = false;
    }

}
