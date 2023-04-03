package com.github.br.gdx.simple.animation;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.ObjectMap;
import com.github.br.gdx.simple.animation.component.AnimatorStaticPart;
import com.github.br.gdx.simple.animation.component.SimpleAnimationComponent;
import com.github.br.gdx.simple.animation.component.SimpleAnimatorUtils;
import com.github.br.gdx.simple.animation.fsm.FsmContext;
import com.github.br.gdx.simple.animation.io.AnimationLoader;

public class SimpleAnimationSystem implements Disposable {

    private final AnimationLoader loader = new AnimationLoader();
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

    public void load(FileHandle animationsDir) {
        Array<SimpleAnimation> items = loader.load(animationsDir);
        if(items == null || items.size == 0) {
            return;
        }
        for (SimpleAnimation simpleAnimation : items) {
            addAnimation(simpleAnimation);
        }
    }

    public void unload(String title) {
        SimpleAnimation simpleAnimation = animations.remove(title);
        if(simpleAnimation == null) {
            throw new IllegalArgumentException("The animation '" + title + "' is not found");
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
