package com.github.br.gdx.simple.animation;

import com.badlogic.gdx.utils.ObjectMap;
import com.github.br.gdx.simple.animation.component.AnimatorStaticPart;
import com.github.br.gdx.simple.animation.fsm.FSM;

public class SimpleAnimation {

    public final String name;
    public final FSM fsm;
    public final ObjectMap<String, AnimatorStaticPart> animatorStaticParts; // key = stateName

    public SimpleAnimation(String name, FSM fsm, ObjectMap<String, AnimatorStaticPart> animatorStaticParts) {
        this.name = name;
        this.fsm = fsm;
        this.animatorStaticParts = animatorStaticParts;
    }

}
