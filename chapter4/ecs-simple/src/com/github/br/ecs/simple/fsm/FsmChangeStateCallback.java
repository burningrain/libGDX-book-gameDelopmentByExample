package com.github.br.ecs.simple.fsm;

/**
 * Created by user on 09.04.2017.
 */
public interface FsmChangeStateCallback {

    void call(FsmContext context);

}
