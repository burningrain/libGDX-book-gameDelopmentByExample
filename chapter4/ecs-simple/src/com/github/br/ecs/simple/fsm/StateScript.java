package com.github.br.ecs.simple.fsm;

/**
 * Created by user on 09.04.2017.
 */
public abstract class StateScript {

    private FSM fsm;

    void setFsm(FSM fsm) {
        this.fsm = fsm;
    }

    public FsmContext getContext() {
        return fsm.context;
    }

    public String getCurrentState(){
        return fsm.currentState.getName();
    }

    public abstract void initialize();
    public abstract void update();
    public abstract void dispose();

    public void fireEvent(FsmStateEvent event){
        event.script = this;
        fsm.fireEvent(event);
    }
}
