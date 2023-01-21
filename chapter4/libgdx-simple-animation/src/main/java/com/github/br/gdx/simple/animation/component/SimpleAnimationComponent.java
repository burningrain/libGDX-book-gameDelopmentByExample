package com.github.br.gdx.simple.animation.component;

import com.github.br.gdx.simple.animation.fsm.FsmContext;

public class SimpleAnimationComponent {

    public String animationName;
    public FsmContext fsmContext;
    public AnimatorDynamicPart animatorDynamicPart;

    public SimpleAnimationComponent(String animationName, FsmContext fsmContext, AnimatorDynamicPart animatorDynamicPart) {
        this.animationName = animationName;
        this.fsmContext = fsmContext;
        this.animatorDynamicPart = animatorDynamicPart;
    }

}
