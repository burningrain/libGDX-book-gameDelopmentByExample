package com.github.br.gdx.simple.animation.io.dto;

import java.io.Serializable;

public class FsmDto implements Serializable {

    private String[] states;
    private String startState;
    private String endState;
    private TransitionDto[] transitions;

    public String[] getStates() {
        return states;
    }

    public void setStates(String[] states) {
        this.states = states;
    }

    public String getStartState() {
        return startState;
    }

    public void setStartState(String startState) {
        this.startState = startState;
    }

    public String getEndState() {
        return endState;
    }

    public void setEndState(String endState) {
        this.endState = endState;
    }

    public TransitionDto[] getTransitions() {
        return transitions;
    }

    public void setTransitions(TransitionDto[] transitions) {
        this.transitions = transitions;
    }

}
