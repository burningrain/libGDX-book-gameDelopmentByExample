package com.github.br.ecs.simple.animation;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

import java.util.ArrayList;

/**
 * Created by user on 08.04.2017.
 */
public class Animator {

    private String name;
    private Animation animation;
    private TextureRegion currentFrame;
    // A variable for tracking elapsed time for the animation
    private float stateTime = 0;
    private boolean isStopped = true;

    private float frameDuration;
    private Animation.PlayMode playMode;
    private boolean looping = false;

    private ArrayList<FinishedCallback> finishedCallbacks;

    //TODO добавить коллбеки на окончание анимации

    public Animator(String name, Array<? extends TextureRegion> keyFrames, float frameDuration){
        this(name, keyFrames, frameDuration, Animation.PlayMode.NORMAL, false);
    }

    public Animator(String name,
                    Array<? extends TextureRegion> keyFrames,
                    float frameDuration,
                    Animation.PlayMode playMode,
                    boolean looping){
        this.name = name;
        this.animation = new Animation(frameDuration, keyFrames, playMode);
        this.frameDuration = frameDuration;
        this.playMode = playMode;
        this.looping = looping;

        this.currentFrame = animation.getKeyFrame(stateTime, looping);
    }

    public void update() {
        if(!isStopped){
            stateTime += Gdx.graphics.getDeltaTime(); // Accumulate elapsed animation time
            this.currentFrame = animation.getKeyFrame(stateTime, looping);
            if(animation.isAnimationFinished(stateTime)){
                notifyFilishedListeners();
            }
        }
    }

    public boolean isAnimationFinished(){
        return animation.isAnimationFinished(stateTime);
    }

    public void play(){
        if(!isStopped){
            throw new IllegalStateException("Анимация уже запущена!");
        }
        isStopped = false;
    }

    public void pause(){
        isStopped = true;
    }

    public void gotoAndPlay(int frame){
        currentFrame = animation.getKeyFrame(frame*frameDuration);
        isStopped = false;
    }

    public void gotoAndStop(int frame){
        currentFrame = animation.getKeyFrame(frame*frameDuration);
        isStopped = true;
    }

    public void stop(){
        isStopped = true;
        stateTime = 0;
        animation.setPlayMode(playMode);
    }

    public boolean isLooping() {
        return looping;
    }

    public void setLooping(boolean looping) {
        this.looping = looping;
    }

    public TextureRegion getCurrentFrame() {
        return currentFrame;
    }

    public float getStateTime() {
        return stateTime;
    }

    public Animation.PlayMode getPlayMode() {
        return playMode;
    }

    public void setPlayMode(Animation.PlayMode playMode) {
        this.playMode = playMode;
        animation.setPlayMode(this.playMode);
    }

    public String getName() {
        return name;
    }

    public interface FinishedCallback {
        void call(Animator animator);
    }

    public void addFinishedCallback(FinishedCallback callback){
        if(finishedCallbacks == null){
            finishedCallbacks = new ArrayList<>();
        }
        finishedCallbacks.add(callback);
    }

    public void removeCallback(FinishedCallback callback){
        finishedCallbacks.remove(callback);
    }

    private void notifyFilishedListeners(){
        if(finishedCallbacks != null){
            for(FinishedCallback callback : finishedCallbacks){
                callback.call(this);
            }
        }
    }

}
