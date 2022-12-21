package com.github.br.gdx.simple.animation;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.ObjectMap;
import com.github.br.gdx.simple.animation.component.AnimatorStaticPart;
import com.github.br.gdx.simple.animation.component.SimpleAnimationComponent;
import com.github.br.gdx.simple.animation.component.SimpleAnimatorUtils;
import com.github.br.gdx.simple.animation.fsm.FsmContext;

public class SimpleAnimationSystem {

    private final ObjectMap<String, SimpleAnimation> animations = new ObjectMap<String, SimpleAnimation>();

    public void update(float delta, SimpleAnimationComponent animationComponent) {
        SimpleAnimation simpleAnimation = animations.get(animationComponent.animationName);
        FsmContext fsmContext = animationComponent.fsmContext;
        String stateBefore = fsmContext.getCurrentState();
        // если не инициализирован компонент, то выставляем ему начальное состояние
        if(stateBefore == null) {
            stateBefore = simpleAnimation.fsm.getStartState();
            fsmContext.setCurrentState(stateBefore);
            SimpleAnimatorUtils.reset(simpleAnimation.animatorStaticParts.get(stateBefore), animationComponent.animatorDynamicPart);
            SimpleAnimatorUtils.play(animationComponent.animatorDynamicPart);
        }

        simpleAnimation.fsm.update(animationComponent.fsmContext);

        String stateAfter = fsmContext.getCurrentState();
        AnimatorStaticPart animatorStaticPart = simpleAnimation.animatorStaticParts.get(stateAfter);
        // если было переключение состояния анимации, то надо сбросить по нулям параметры
        if(!stateAfter.equals(stateBefore)) {
            SimpleAnimatorUtils.reset(animatorStaticPart, animationComponent.animatorDynamicPart);
            SimpleAnimatorUtils.play(animationComponent.animatorDynamicPart);
        }

        SimpleAnimatorUtils.update(animatorStaticPart, animationComponent.animatorDynamicPart);
    }

    public void addAnimation(SimpleAnimation simpleAnimation) {
        this.animations.put(simpleAnimation.name, simpleAnimation);
        //this.animatorService.play(); //todo вопрос изначальной инициализации открыт
    }

    public void load(FileHandle animationsDir) {
        for (FileHandle entry: animationsDir.list()) {
            //TODO
        }
    }

}
