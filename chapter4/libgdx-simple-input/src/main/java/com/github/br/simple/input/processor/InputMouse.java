package com.github.br.simple.input.processor;

import com.github.br.simple.input.processor.mouse.MouseMoved;
import com.github.br.simple.input.processor.mouse.Scrolled;

public class InputMouse {

    public final MouseMoved mouseMoved = new MouseMoved();
    public final Scrolled scrolled = new Scrolled();

    public boolean isChanged = false;

    public InputMouse() {
        reset();
    }

    public void reset() {
        mouseMoved.reset();
        scrolled.reset();

        isChanged = false;
    }

}
