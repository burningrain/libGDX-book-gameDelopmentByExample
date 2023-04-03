package com.github.br.gdx.simple.animation.component;

import com.badlogic.gdx.graphics.g2d.Animation;

public class AnimatorStaticPart {

    public final String name;
    public final boolean looping; //todo параметр можно вычислить через mode

    public final Object[] keyFrames;
    public final float frameDuration;
    public final Animation.PlayMode playMode;


    public AnimatorStaticPart(String name,
                              Object[] keyFrames,
                              float frameDuration,
                              boolean looping,
                              Animation.PlayMode playMode) {
        this.name = name;
        this.looping = looping;

        this.keyFrames = keyFrames;
        this.frameDuration = frameDuration;
        this.playMode = playMode;
    }

}
