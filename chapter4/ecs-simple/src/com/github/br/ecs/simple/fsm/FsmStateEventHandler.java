package com.github.br.ecs.simple.fsm;

/**
 * Created by user on 15.04.2017.
 */
public interface FsmStateEventHandler {

    void handleEvent(FsmStateEvent event);

}
