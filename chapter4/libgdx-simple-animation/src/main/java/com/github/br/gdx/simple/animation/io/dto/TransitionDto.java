package com.github.br.gdx.simple.animation.io.dto;

import java.io.Serializable;

public class TransitionDto implements Serializable {

    private String from;
    private String to;
    private String[] fsmPredicates;

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String[] getFsmPredicates() {
        return fsmPredicates;
    }

    public void setFsmPredicates(String[] fsmPredicates) {
        this.fsmPredicates = fsmPredicates;
    }

}
