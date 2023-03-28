package com.github.br.gdx.simple.animation.io;

import com.badlogic.gdx.utils.ObjectMap;
import com.github.br.gdx.simple.animation.AnimationExtensions;
import com.github.br.gdx.simple.animation.SimpleAnimation;
import com.github.br.gdx.simple.animation.component.AnimatorStaticPart;
import com.github.br.gdx.simple.animation.fsm.FSM;
import com.github.br.gdx.simple.animation.fsm.FsmPredicate;
import com.github.br.gdx.simple.animation.fsm.FsmState;
import com.github.br.gdx.simple.animation.fsm.FsmStateSubFsm;
import com.github.br.gdx.simple.animation.io.dto.AnimationDto;
import com.github.br.gdx.simple.animation.io.dto.AnimatorDto;
import com.github.br.gdx.simple.animation.io.dto.FsmDto;
import com.github.br.gdx.simple.animation.io.dto.TransitionDto;


public class AnimationConverter {

    private final AnimationClassloader animationClassloader = new AnimationClassloader();

    public SimpleAnimation from(AnimationDto animationDto, Object[] keyFrames, ObjectMap<String, byte[]> javaClasses) {
        loadTransitionClasses(animationDto.getName(), javaClasses);
        return new SimpleAnimation(
                animationDto.getName(),
                createFsm(animationDto.getName(), animationDto.getFsm(), animationDto.getSubFsm()),
                createAnimatorStaticParts(animationDto.getAnimators(), keyFrames)
        );
    }

    private ObjectMap<String, AnimatorStaticPart> createAnimatorStaticParts(AnimatorDto[] animators, Object[] keyFrames) {
        ObjectMap<String, AnimatorStaticPart> result = new ObjectMap<String, AnimatorStaticPart>();
        for (AnimatorDto animator : animators) {
            int length = animator.getTo() - animator.getFrom();
            Object[] keyFramesArray = new Object[length];
            System.arraycopy(keyFrames, animator.getFrom(), keyFramesArray, 0, length);
            result.put(animator.getName(), createAnimatorStaticPart(animator, keyFramesArray));
        }

        return result;
    }

    private AnimatorStaticPart createAnimatorStaticPart(AnimatorDto animator, Object[] keyFrames) {
        return new AnimatorStaticPart(
                animator.getName(),
                keyFrames,
                1.0f / animator.getFrameFrequency(),
                animator.isLooping(),
                animator.getMode()
        );
    }

    private FSM createFsm(String name, FsmDto fsm, ObjectMap<String, FsmDto> subFsm) {
        FSM.Builder builder = new FSM.Builder();
        for (String state : fsm.getStates()) {
            FsmState fsmState;
            if (subFsm != null && subFsm.containsKey(state)) {
                FsmStateSubFsm.Builder subBuilder = new FsmStateSubFsm.Builder()
                        .setName(state)
                        .setStartState(fsm.getStartState().equals(state))
                        .setEndState(fsm.getEndState().equals(state));
                fsmState = subBuilder.build(createFsm(name, subFsm.get(state), subFsm));
            } else {
                fsmState = new FsmState.Builder()
                        .setName(state)
                        .setStartState(fsm.getStartState().equals(state))
                        .setEndState(fsm.getEndState().equals(state))
                        .build();
            }
            builder.addState(fsmState);
        }

        for (TransitionDto transition : fsm.getTransitions()) {
            FsmPredicate fsmPredicate = createFsmPredicate(name, transition);
            if (FSM.ANY_STATE.equals(transition.getFrom())) {
                builder.addTransitionFromAnyState(transition.getTo(), fsmPredicate);
            } else {
                builder.addTransition(transition.getFrom(), transition.getTo(), fsmPredicate);
            }
        }

        return builder.build();
    }

    private void loadTransitionClasses(String packageName, ObjectMap<String, byte[]> javaClasses) {
        for (ObjectMap.Entry<String, byte[]> entry : javaClasses) {
            String fullClassName = getFullClassName(packageName, entry.key);
            animationClassloader.setLoadedClassName(fullClassName);
            animationClassloader.setLoadedClass(entry.value);
            try {
                animationClassloader.loadClass(fullClassName);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private FsmPredicate createFsmPredicate(String packageName, TransitionDto transitionDto) {
        String classname = Utils.createClassName(transitionDto);
        String fullClassName = AnimationExtensions.ANIMATION_PACKAGE + packageName + "." + classname;
        try {
            Class<?> aClass = Class.forName(fullClassName);
            FsmPredicate predicate = (FsmPredicate) aClass.newInstance();
            return predicate;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private String getFullClassName(String packageName, String className) {
        String packageTitle = AnimationExtensions.ANIMATION_PACKAGE + packageName + ".";
        int classLength = ".class".length();
        return packageTitle + className.substring(0, className.length() - classLength);
    }

}
