package com.github.br.gdx.simple.animation;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.ObjectMap;
import com.github.br.gdx.simple.animation.component.AnimatorStaticPart;
import com.github.br.gdx.simple.animation.component.SimpleAnimationComponent;
import com.github.br.gdx.simple.animation.component.SimpleAnimatorUtils;
import com.github.br.gdx.simple.animation.fsm.FsmContext;

public class SimpleAnimationSystem implements Disposable {

    private final ObjectMap<String, SimpleAnimation> animations = new ObjectMap<String, SimpleAnimation>();

    public void update(float delta, SimpleAnimationComponent animationComponent) {
        SimpleAnimation simpleAnimation = animations.get(animationComponent.animationName);
        FsmContext fsmContext = animationComponent.fsmContext;
        String stateBefore = fsmContext.getCurrentState();
        // если не инициализирован компонент, то выставляем ему начальное состояние
        if (stateBefore == null) {
            stateBefore = simpleAnimation.fsm.getStartState();
            fsmContext.setCurrentState(stateBefore);
            SimpleAnimatorUtils.reset(simpleAnimation.animatorStaticParts.get(stateBefore), animationComponent.animatorDynamicPart);
            SimpleAnimatorUtils.play(animationComponent.animatorDynamicPart);
        }

        simpleAnimation.fsm.update(animationComponent.fsmContext);

        String stateAfter = fsmContext.getCurrentState();
        AnimatorStaticPart animatorStaticPart = simpleAnimation.animatorStaticParts.get(stateAfter);
        // если было переключение состояния анимации, то надо сбросить по нулям параметры
        if (!stateAfter.equals(stateBefore)) {
            SimpleAnimatorUtils.reset(animatorStaticPart, animationComponent.animatorDynamicPart);
            SimpleAnimatorUtils.play(animationComponent.animatorDynamicPart);
        }

        SimpleAnimatorUtils.update(animatorStaticPart, animationComponent.animatorDynamicPart);
    }

    public void addAnimation(SimpleAnimation simpleAnimation) {
        this.animations.put(simpleAnimation.name, simpleAnimation);
    }

    public void unload(String title) {
        SimpleAnimation simpleAnimation = animations.remove(title);
        if(simpleAnimation == null) {
            Gdx.app.debug("WARN " + SimpleAnimationSystem.class.getSimpleName(), "The animation '" + title + "' is not found");
            return;
        }

        simpleAnimation.dispose();
    }

    @Override
    public void dispose() {
        ObjectMap.Entries<String, SimpleAnimation> iterator = animations.iterator();
        for (ObjectMap.Entry<String, SimpleAnimation> entry : iterator) {
            entry.value.dispose();
        }
        animations.clear();
    }

}
