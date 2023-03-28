package com.github.br.gdx.simple.animation.io.dto;

import java.io.Serializable;

public class TransitionDto implements Serializable {

    private String from;
    private String to;
    private String fsmPredicate;

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

    public String getFsmPredicate() {
        return fsmPredicate;
    }

    public void setFsmPredicate(String fsmPredicate) {
        this.fsmPredicate = fsmPredicate;
    }

}
