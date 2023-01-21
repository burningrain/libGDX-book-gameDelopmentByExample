package com.github.br.gdx.simple.animation.io.dto;

import com.badlogic.gdx.graphics.g2d.Animation;

import java.io.Serializable;

public class AnimatorDto implements Serializable {

    private String name;
    private int frameFrequency;
    private Animation.PlayMode mode;
    private int from;
    private int to;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getFrameFrequency() {
        return frameFrequency;
    }

    public void setFrameFrequency(int frameFrequency) {
        this.frameFrequency = frameFrequency;
    }

    public Animation.PlayMode getMode() {
        return mode;
    }

    public void setMode(Animation.PlayMode mode) {
        this.mode = mode;
    }

    public int getFrom() {
        return from;
    }

    public void setFrom(int from) {
        this.from = from;
    }

    public int getTo() {
        return to;
    }

    public void setTo(int to) {
        this.to = to;
    }

}
