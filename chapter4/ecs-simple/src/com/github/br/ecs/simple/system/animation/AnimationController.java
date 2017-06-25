package com.github.br.ecs.simple.system.animation;

import com.github.br.ecs.simple.fsm.*;

import java.util.HashMap;

/**
 * Created by user on 09.04.2017.
 */
public class AnimationController {

    private HashMap<String, Animator> animators = new HashMap<String, Animator>();
    private FSM fsm;

    private Animator currentAnimator;

    public AnimationController(Animator[] animators, FSM fsm){
        for(Animator animator : animators){
            this.animators.put(animator.getName(), animator);
        }
        this.fsm = fsm;
        this.currentAnimator = this.animators.get(fsm.getCurrentState());
        this.currentAnimator.play();

        this.fsm.addChangeStateCallback(new FsmChangeStateCallback() {
            @Override
            public void call(FsmContext context) {
                AnimationController.this.currentAnimator.stop();
                AnimationController.this.currentAnimator =
                        AnimationController.this.animators.get(context.getCurrentState());
                AnimationController.this.currentAnimator.play();
            }
        });
    }

    void update(){
        this.fsm.udpate();
        this.currentAnimator.update();
    }

    public FsmContext getAnimationContext() {
        return this.fsm.getContext();
    }

    public Animator getCurrentAnimator(){
        return currentAnimator;
    }

}
