package com.github.br.gdx.simple.animation.io.dto;

import com.badlogic.gdx.utils.ObjectMap;

import java.io.Serializable;
import java.util.Map;

public class AnimationDto implements Serializable {

    private AnimatorDto[] animators;
    private FsmDto fsm;
    private ObjectMap<String, FsmDto> subFsm;

    public AnimatorDto[] getAnimators() {
        return animators;
    }

    public void setAnimators(AnimatorDto[] animators) {
        this.animators = animators;
    }

    public FsmDto getFsm() {
        return fsm;
    }

    public void setFsm(FsmDto fsm) {
        this.fsm = fsm;
    }

    public ObjectMap<String, FsmDto> getSubFsm() {
        return subFsm;
    }

    public void setSubFsm(ObjectMap<String, FsmDto> subFsm) {
        this.subFsm = subFsm;
    }

}
