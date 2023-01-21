package com.github.br.gdx.simple.animation.component;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * Created by user on 08.04.2017.
 */
public class SimpleAnimatorUtils {

    public static void update(AnimatorStaticPart animatorStaticPart, AnimatorDynamicPart animatorDynamicPart) {
        if (!animatorDynamicPart.isStopped) {
            animatorDynamicPart.stateTime += Gdx.graphics.getDeltaTime(); // Accumulate elapsed animation time
            animatorDynamicPart.currentFrame =
                    animatorDynamicPart.animation.getKeyFrame(animatorDynamicPart.stateTime, animatorStaticPart.looping);
        }
    }

    public static boolean isAnimationFinished(AnimatorDynamicPart animatorDynamicPart) {
        return animatorDynamicPart.animation.isAnimationFinished(animatorDynamicPart.stateTime);
    }

    public static void play(AnimatorDynamicPart animatorDynamicPart) {
        if (!animatorDynamicPart.isStopped) {
            throw new IllegalStateException("Анимация уже запущена!");
        }
        animatorDynamicPart.isStopped = false;
    }

    public static void pause(AnimatorDynamicPart animatorDynamicPart) {
        animatorDynamicPart.isStopped = true;
    }

    public static void gotoAndPlay(int frame, AnimatorDynamicPart animatorDynamicPart) {
        animatorDynamicPart.currentFrame = animatorDynamicPart.animation.getKeyFrame(frame * animatorDynamicPart.animation.getFrameDuration());
        animatorDynamicPart.isStopped = false;
    }

    public static void gotoAndStop(int frame, AnimatorDynamicPart animatorDynamicPart) {
        animatorDynamicPart.currentFrame = animatorDynamicPart.animation.getKeyFrame(frame * animatorDynamicPart.animation.getFrameDuration());
        animatorDynamicPart.isStopped = true;
    }

    public static void reset(AnimatorStaticPart animatorStaticPart, AnimatorDynamicPart animatorDynamicPart) {
        animatorDynamicPart.reset(animatorStaticPart);
    }

    public static float getStateTime(AnimatorDynamicPart animatorDynamicPart) {
        return animatorDynamicPart.stateTime;
    }

    public static TextureRegion getCurrentFrame(AnimatorDynamicPart animatorDynamicPart) {
        return animatorDynamicPart.currentFrame;
    }

}
