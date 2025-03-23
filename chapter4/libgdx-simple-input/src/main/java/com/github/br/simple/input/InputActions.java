package com.github.br.simple.input;

import java.util.BitSet;

public class InputActions {

    private final BitSet actions;

    public InputActions(int actionsSize) {
        this.actions = new BitSet(actionsSize);
    }

    public void setAction(int action) {
        this.actions.set(action);
    }

    public boolean getAction(int action) {
        return this.actions.get(action);
    }

    public void clear() {
        this.actions.clear();
    }

}
