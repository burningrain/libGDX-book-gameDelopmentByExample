package com.github.br.gdx.simple.animation.component;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.github.br.gdx.simple.animation.ModifiedAnimation;

public class AnimatorDynamicPart {

    public TextureRegion currentFrame;
    public boolean isStopped = true;
    // A variable for tracking elapsed time for the animation
    public float stateTime = 0;
    public ModifiedAnimation animation;

    public void reset(AnimatorStaticPart animatorStaticPart) {
        stateTime = 0;
        isStopped = true;
        if(this.animation == null) {
            // первоначальная инициализация
            this.animation = new ModifiedAnimation(animatorStaticPart.frameDuration, animatorStaticPart.keyFrames, animatorStaticPart.playMode);
        } else {
            this.animation.reset(animatorStaticPart.frameDuration, animatorStaticPart.keyFrames, animatorStaticPart.playMode);
        }
        this.currentFrame = animation.getKeyFrame(stateTime, animatorStaticPart.looping);
    }

}
