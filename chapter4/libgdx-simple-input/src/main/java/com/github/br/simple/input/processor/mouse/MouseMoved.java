package com.github.br.simple.input.processor.mouse;

import com.github.br.simple.input.ActionConst;

public class MouseMoved {

    public int screenX;
    public int screenY;

    public boolean isChanged = false;

    public MouseMoved() {
        reset();
    }

    public void reset() {
        screenX = ActionConst.EMPTY_DATA;
        screenY = ActionConst.EMPTY_DATA;

        isChanged = false;
    }
}
