package com.github.br.gdx.simple.animation;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;

/**
 * TODO причина существования данного класса-клона в том, что нету возможности "обнулить" com.badlogic.gdx.graphics.g2d.Animation
 * TODO а плодить каждый раз new Animation() и терять 16+ байт при смене анимации нету никакого желания
 */
public class ModifiedAnimation {

    Object[] keyFrames; // TextureRegion[] компилятор трет тип из-за забыотого в либе <>   убран final. поле можно изменять
    private float frameDuration;
    private float animationDuration;
    private int lastFrameNumber;
    private float lastStateTime;

    private Animation.PlayMode playMode = Animation.PlayMode.NORMAL;


    /**
     * Constructor, storing the frame duration, key frames and play type.
     *
     * @param frameDuration the time between frames in seconds.
     * @param keyFrames     the {@link TextureRegion}s representing the frames.
     * @param playMode      the animation playback mode.
     */
    public ModifiedAnimation(float frameDuration, Object[] keyFrames, Animation.PlayMode playMode) {
        this.frameDuration = frameDuration;
        this.animationDuration = keyFrames.length * frameDuration;
        this.keyFrames = keyFrames;

        this.playMode = playMode;
    }

    /**
     * Constructor, storing the frame duration and key frames.
     *
     * @param frameDuration the time between frames in seconds.
     * @param keyFrames     the {@link TextureRegion}s representing the frames.
     */
    public ModifiedAnimation(float frameDuration, TextureRegion... keyFrames) {
        this.frameDuration = frameDuration;
        this.animationDuration = keyFrames.length * frameDuration;
        this.keyFrames = keyFrames;
        this.playMode = Animation.PlayMode.NORMAL;
    }

    /**
     * Returns a {@link TextureRegion} based on the so called state time. This is the amount of seconds an object has spent in the
     * state this Animation instance represents, e.g. running, jumping and so on. The mode specifies whether the animation is
     * looping or not.
     *
     * @param stateTime the time spent in the state represented by this animation.
     * @param looping   whether the animation is looping or not.
     * @return the TextureRegion representing the frame of animation for the given state time.
     */
    public TextureRegion getKeyFrame(float stateTime, boolean looping) {
        // we set the play mode by overriding the previous mode based on looping
        // parameter value
        Animation.PlayMode oldPlayMode = playMode;
        if (looping && (playMode == Animation.PlayMode.NORMAL || playMode == Animation.PlayMode.REVERSED)) {
            if (playMode == Animation.PlayMode.NORMAL)
                playMode = Animation.PlayMode.LOOP;
            else
                playMode = Animation.PlayMode.LOOP_REVERSED;
        } else if (!looping && !(playMode == Animation.PlayMode.NORMAL || playMode == Animation.PlayMode.REVERSED)) {
            if (playMode == Animation.PlayMode.LOOP_REVERSED)
                playMode = Animation.PlayMode.REVERSED;
            else
                playMode = Animation.PlayMode.LOOP;
        }

        TextureRegion frame = getKeyFrame(stateTime);
        playMode = oldPlayMode;
        return frame;
    }

    /**
     * Returns a {@link TextureRegion} based on the so called state time. This is the amount of seconds an object has spent in the
     * state this Animation instance represents, e.g. running, jumping and so on using the mode specified by
     * {@link #setPlayMode(Animation.PlayMode)} method.
     *
     * @param stateTime
     * @return the TextureRegion representing the frame of animation for the given state time.
     */
    public TextureRegion getKeyFrame(float stateTime) {
        int frameNumber = getKeyFrameIndex(stateTime);
        return (TextureRegion)keyFrames[frameNumber];
    }

    /**
     * Returns the current frame number.
     *
     * @param stateTime
     * @return current frame number
     */
    public int getKeyFrameIndex(float stateTime) {
        if (keyFrames.length == 1) return 0;

        int frameNumber = (int) (stateTime / frameDuration);
        switch (playMode) {
            case NORMAL:
                frameNumber = Math.min(keyFrames.length - 1, frameNumber);
                break;
            case LOOP:
                frameNumber = frameNumber % keyFrames.length;
                break;
            case LOOP_PINGPONG:
                frameNumber = frameNumber % ((keyFrames.length * 2) - 2);
                if (frameNumber >= keyFrames.length)
                    frameNumber = keyFrames.length - 2 - (frameNumber - keyFrames.length);
                break;
            case LOOP_RANDOM:
                int lastFrameNumber = (int) ((lastStateTime) / frameDuration);
                if (lastFrameNumber != frameNumber) {
                    frameNumber = MathUtils.random(keyFrames.length - 1);
                } else {
                    frameNumber = this.lastFrameNumber;
                }
                break;
            case REVERSED:
                frameNumber = Math.max(keyFrames.length - frameNumber - 1, 0);
                break;
            case LOOP_REVERSED:
                frameNumber = frameNumber % keyFrames.length;
                frameNumber = keyFrames.length - frameNumber - 1;
                break;
        }

        lastFrameNumber = frameNumber;
        lastStateTime = stateTime;

        return frameNumber;
    }

    /**
     * Returns the keyFrames[] array where all the TextureRegions of the animation are stored.
     *
     * @return keyFrames[] field
     */
    public Object[] getKeyFrames() {
        return keyFrames;
    }

    /**
     * Returns the animation play mode.
     */
    public Animation.PlayMode getPlayMode() {
        return playMode;
    }

    /**
     * Sets the animation play mode.
     *
     * @param playMode The animation {@link Animation.PlayMode} to use.
     */
    public void setPlayMode(Animation.PlayMode playMode) {
        this.playMode = playMode;
    }

    /**
     * Whether the animation would be finished if played without looping (PlayMode#NORMAL), given the state time.
     *
     * @param stateTime
     * @return whether the animation is finished.
     */
    public boolean isAnimationFinished(float stateTime) {
        int frameNumber = (int) (stateTime / frameDuration);
        return keyFrames.length - 1 < frameNumber;
    }

    /**
     * Sets duration a frame will be displayed.
     *
     * @param frameDuration in seconds
     */
    public void setFrameDuration(float frameDuration) {
        this.frameDuration = frameDuration;
        this.animationDuration = keyFrames.length * frameDuration;
    }

    /**
     * @return the duration of a frame in seconds
     */
    public float getFrameDuration() {
        return frameDuration;
    }

    /**
     * @return the duration of the entire animation, number of frames times frame duration, in seconds
     */
    public float getAnimationDuration() {
        return animationDuration;
    }

    // добавлен метод сброса параметров
    public void reset(float frameDuration, Object[] keyFrames, Animation.PlayMode playMode) {
        // this.animationDuration = keyFrames.size * frameDuration; меняется в setFrameDuration
        this.keyFrames = keyFrames;
        this.setFrameDuration(frameDuration);
        this.setPlayMode(playMode);
        lastFrameNumber = 0;
        lastStateTime = 0;
    }

    public static void main(String[] args) {
        TextureRegion[] array = new TextureRegion[]{
                new TextureRegion(),
                new TextureRegion(),
                new TextureRegion(),
                new TextureRegion(),
                new TextureRegion(),
                new TextureRegion(),
                new TextureRegion(),
                new TextureRegion(),
                new TextureRegion(),
                new TextureRegion(),
                new TextureRegion(),
                new TextureRegion()
        };

        TextureRegion[] array2 = new TextureRegion[] {
                new TextureRegion()
        };

        ModifiedAnimation modifiedAnimation = new ModifiedAnimation(1 / 60f, array);
        System.out.println(modifiedAnimation.getAnimationDuration());

        modifiedAnimation.reset(1 / 60f, array2, Animation.PlayMode.LOOP);
        System.out.println(modifiedAnimation.getAnimationDuration());
    }


}
