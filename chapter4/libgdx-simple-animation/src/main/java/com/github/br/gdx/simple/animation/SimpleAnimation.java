package com.github.br.gdx.simple.animation;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.ObjectMap;
import com.github.br.gdx.simple.animation.component.AnimatorStaticPart;
import com.github.br.gdx.simple.animation.fsm.FSM;

public class SimpleAnimation implements Disposable {

    public final String name;
    public final FSM fsm;
    public final TextureAtlas textureAtlas;
    public final ObjectMap<String, AnimatorStaticPart> animatorStaticParts; // key = stateName

    public SimpleAnimation(String name, FSM fsm, TextureAtlas textureAtlas, ObjectMap<String, AnimatorStaticPart> animatorStaticParts) {
        this.name = name;
        this.fsm = fsm;
        this.textureAtlas = textureAtlas;
        this.animatorStaticParts = animatorStaticParts;
    }

    public void dispose() {
        textureAtlas.dispose();
        animatorStaticParts.clear();
    }

}
