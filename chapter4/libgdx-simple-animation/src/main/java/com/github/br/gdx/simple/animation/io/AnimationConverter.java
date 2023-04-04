package com.github.br.gdx.simple.animation.io;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
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
import com.github.br.gdx.simple.animation.io.interpret.PredicateInterpreter;


public class AnimationConverter {

    private final PredicateInterpreter interpreter = new PredicateInterpreter();

    public SimpleAnimation from(AnimationDto animationDto, TextureAtlas atlas) {
        return new SimpleAnimation(
                animationDto.getName(),
                createFsm(animationDto.getFsm(), animationDto.getSubFsm()),
                atlas,
                createAnimatorStaticParts(animationDto.getAnimators(), atlas.getRegions().shrink())
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

    private FSM createFsm(FsmDto fsm, ObjectMap<String, FsmDto> subFsm) {
        FSM.Builder builder = new FSM.Builder();
        for (String state : fsm.getStates()) {
            FsmState fsmState;
            if (subFsm != null && subFsm.containsKey(state)) {
                FsmStateSubFsm.Builder subBuilder = new FsmStateSubFsm.Builder()
                        .setName(state)
                        .setStartState(fsm.getStartState().equals(state))
                        .setEndState(fsm.getEndState().equals(state));
                fsmState = subBuilder.build(createFsm(subFsm.get(state), subFsm));
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
            FsmPredicate fsmPredicate = createFsmPredicate(transition, fsm.getVariables());
            if (FSM.ANY_STATE.equals(transition.getFrom())) {
                builder.addTransitionFromAnyState(transition.getTo(), fsmPredicate);
            } else {
                builder.addTransition(transition.getFrom(), transition.getTo(), fsmPredicate);
            }
        }

        return builder.build();
    }

    private FsmPredicate createFsmPredicate(TransitionDto dto, ObjectMap<String, String> variables) {
        String[] fsmPredicates = dto.getFsmPredicates();
        if (fsmPredicates == null) {
            throw new IllegalArgumentException("The transition[" + dto.getFrom() + "->" + dto.getTo() + "], field 'fsmPredicates' must not be null");
        }

        FsmPredicate[] predicates = new FsmPredicate[fsmPredicates.length];
        int counter = 0;
        for (String fsmPredicate : fsmPredicates) {
            predicates[counter] = interpreter.interpret(variables, fsmPredicate);
            counter++;
        }
        return new CompositeFsmPredicate(predicates);
    }

}
