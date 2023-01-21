package com.github.br.gdx.simple.animation.fsm;

/**
 * Created by user on 09.04.2017.
 */
public abstract class StateScript {

    private FSM fsm;

    void setFsm(FSM fsm) {
        this.fsm = fsm;
    }

    public abstract void initialize();

    public abstract void update(FsmContext context);

    public abstract void dispose();

    public void fireEvent(FsmStateEvent event) {
        event.script = this;
        fsm.fireEvent(event);
    }

}
