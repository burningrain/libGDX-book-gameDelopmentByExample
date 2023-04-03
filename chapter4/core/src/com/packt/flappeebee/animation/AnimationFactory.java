package com.packt.flappeebee.animation;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import com.github.br.gdx.simple.animation.SimpleAnimation;
import com.github.br.gdx.simple.animation.component.AnimatorStaticPart;
import com.github.br.gdx.simple.animation.fsm.FSM;
import com.github.br.gdx.simple.animation.fsm.FsmContext;
import com.github.br.gdx.simple.animation.fsm.FsmPredicate;
import com.github.br.gdx.simple.animation.fsm.FsmState;
import com.packt.flappeebee.model.scripts.CrabAnimScript;

public class AnimationFactory {

    private static final TextureAtlas crabAtlas;
    private static final TextureAtlas animAtlas;

    static {
        animAtlas = new TextureAtlas(Gdx.files.internal("packedimages/anim_example.atlas"));
        crabAtlas = new TextureAtlas(Gdx.files.internal("crab/crab.atlas"));
    }

    public static SimpleAnimation createPlant() {
        // статика анимации
        AnimatorStaticPart animatorGrow = new AnimatorStaticPart(
                "grow",
                animAtlas.getRegions().shrink(),
                1f / 30,
                true,
                Animation.PlayMode.LOOP_PINGPONG
        );

        FSM.Builder builder = new FSM.Builder();
        FsmState.Builder stateBuilder = new FsmState.Builder();
        stateBuilder.setName("grow").setStartState(true).setEndState(false);
        builder.addState(stateBuilder.build());
        FSM fsm = builder.build();

        ObjectMap<String, AnimatorStaticPart> map = new ObjectMap<String, AnimatorStaticPart>();
        map.put(animatorGrow.name, animatorGrow);
        SimpleAnimation simpleAnimation = new SimpleAnimation(Animations.PLANT, fsm, animAtlas, map);
        // статика анимации

        return simpleAnimation;
    }

    public static SimpleAnimation createCrab() {
        // статика анимации
        Array<TextureAtlas.AtlasRegion> atlasRegions = crabAtlas.getRegions();
        Object[] items = atlasRegions.shrink();

        // 0..44
        TextureAtlas.AtlasRegion[] movement = new TextureAtlas.AtlasRegion[44 - 0 + 1];
        System.arraycopy(items, 0, movement, 0, 44 - 0 + 1);
        //Array<TextureAtlas.AtlasRegion> movementArray = new Array<TextureAtlas.AtlasRegion>(movement);

        // 45..75
        TextureAtlas.AtlasRegion[] attack = new TextureAtlas.AtlasRegion[75 - 45 + 1];
        System.arraycopy(items, 45, attack, 0, 75 - 45 + 1);
        //Array<TextureAtlas.AtlasRegion> attackArray = new Array<TextureAtlas.AtlasRegion>(attack);

        // 76..86
        TextureAtlas.AtlasRegion[] jump = new TextureAtlas.AtlasRegion[86 - 76 + 1];
        System.arraycopy(items, 76, jump, 0, 86 - 76 + 1);
        //Array<TextureAtlas.AtlasRegion> jumpArray = new Array<TextureAtlas.AtlasRegion>(jump);

        // 87..104
        TextureAtlas.AtlasRegion[] fly = new TextureAtlas.AtlasRegion[104 - 87 + 1];
        System.arraycopy(items, 87, fly, 0, 104 - 87 + 1);
        //Array<TextureAtlas.AtlasRegion> flyArray = new Array<TextureAtlas.AtlasRegion>(fly);

        AnimatorStaticPart animatorIdle = new AnimatorStaticPart(CrabAnimScript.IDLE, jump, 1f / 10, true, Animation.PlayMode.LOOP);
        AnimatorStaticPart animatorMovement = new AnimatorStaticPart(CrabAnimScript.MOVEMENT, movement, 1f / 60, true, Animation.PlayMode.LOOP);
        AnimatorStaticPart animatorAttack = new AnimatorStaticPart(CrabAnimScript.ATTACK, attack, 1f / 30, false, Animation.PlayMode.NORMAL);
        AnimatorStaticPart animatorJump = new AnimatorStaticPart(CrabAnimScript.JUMP, jump, 1f / 60, false, Animation.PlayMode.NORMAL);
        AnimatorStaticPart animatorFly = new AnimatorStaticPart(CrabAnimScript.FLY, fly, 1f / 30, true, Animation.PlayMode.LOOP);

        FSM.Builder builder = new FSM.Builder();
        FsmState.Builder stateIdleBuilder = new FsmState.Builder();
        stateIdleBuilder.setName(CrabAnimScript.IDLE).setStartState(true);
        FsmState.Builder stateMovementBuilder = new FsmState.Builder();
        stateMovementBuilder.setName(CrabAnimScript.MOVEMENT);
        FsmState.Builder stateAttackBuilder = new FsmState.Builder();
        stateAttackBuilder.setName(CrabAnimScript.ATTACK);
        FsmState.Builder stateJumpBuilder = new FsmState.Builder();
        stateJumpBuilder.setName(CrabAnimScript.JUMP);
        FsmState.Builder stateFlyBuilder = new FsmState.Builder();
        stateFlyBuilder.setName(CrabAnimScript.FLY);

        builder
                .addState(stateIdleBuilder.build())
                .addState(stateMovementBuilder.build())
                .addState(stateAttackBuilder.build())
                .addState(stateJumpBuilder.build())
                .addState(stateFlyBuilder.build())

                .addTransition(CrabAnimScript.IDLE, CrabAnimScript.MOVEMENT, new FsmPredicate() {
                    public boolean predicate(FsmContext context) {
                        return Float.class.cast(context.get(CrabAnimScript.MOVEMENT)) > 0;
                    }
                })
                .addTransition(CrabAnimScript.MOVEMENT, CrabAnimScript.IDLE, new FsmPredicate() {
                    public boolean predicate(FsmContext context) {
                        return Float.class.cast(context.get(CrabAnimScript.MOVEMENT)) <= 0;
                    }
                })
                .addTransition(CrabAnimScript.IDLE, CrabAnimScript.JUMP, new FsmPredicate() {
                    public boolean predicate(FsmContext context) {
                        return Boolean.class.cast(context.get(CrabAnimScript.JUMP)) == true;
                    }
                })
                .addTransition(CrabAnimScript.MOVEMENT, CrabAnimScript.JUMP, new FsmPredicate() {
                    public boolean predicate(FsmContext context) {
                        return Boolean.class.cast(context.get(CrabAnimScript.JUMP)) == true;
                    }
                })
                .addTransition(CrabAnimScript.JUMP, CrabAnimScript.FLY, new FsmPredicate() {
                    public boolean predicate(FsmContext context) {
                        return Boolean.class.cast(context.get(CrabAnimScript.FLY)) == true;
                    }
                })
                .addTransition(CrabAnimScript.IDLE, CrabAnimScript.FLY, new FsmPredicate() {
                    public boolean predicate(FsmContext context) {
                        return Boolean.class.cast(context.get(CrabAnimScript.FLY)) == true;
                    }
                })
                .addTransition(CrabAnimScript.MOVEMENT, CrabAnimScript.FLY, new FsmPredicate() {
                    public boolean predicate(FsmContext context) {
                        return Boolean.class.cast(context.get(CrabAnimScript.FLY)) == true;
                    }
                })
                .addTransition(CrabAnimScript.FLY, CrabAnimScript.IDLE, new FsmPredicate() {
                    public boolean predicate(FsmContext context) {
                        return Boolean.class.cast(context.get(CrabAnimScript.FLY)) == false && Float.class.cast(context.get(CrabAnimScript.MOVEMENT)) <= 0;
                    }
                })
                .addTransition(CrabAnimScript.FLY, CrabAnimScript.MOVEMENT, new FsmPredicate() {
                    public boolean predicate(FsmContext context) {
                        return Boolean.class.cast(context.get(CrabAnimScript.FLY)) == false && Float.class.cast(context.get(CrabAnimScript.MOVEMENT)) > 0;
                    }
                })
                .addTransitionFromAnyState(CrabAnimScript.ATTACK, new FsmPredicate() {
                    public boolean predicate(FsmContext context) {
                        return Boolean.class.cast(context.get(CrabAnimScript.ATTACK)) == true;
                    }
                })
                .addTransition(CrabAnimScript.ATTACK, CrabAnimScript.IDLE, new FsmPredicate() {
                    public boolean predicate(FsmContext context) {
                        return Boolean.class.cast(context.get(CrabAnimScript.ATTACK)) == false && Float.class.cast(context.get(CrabAnimScript.MOVEMENT)) <= 0;
                    }
                })
                .addTransition(CrabAnimScript.ATTACK, CrabAnimScript.MOVEMENT, new FsmPredicate() {
                    public boolean predicate(FsmContext context) {
                        return Boolean.class.cast(context.get(CrabAnimScript.ATTACK)) == false && Float.class.cast(context.get(CrabAnimScript.MOVEMENT)) > 0;
                    }
                })
                .addTransition(CrabAnimScript.ATTACK, CrabAnimScript.FLY, new FsmPredicate() {
                    public boolean predicate(FsmContext context) {
                        return Boolean.class.cast(context.get(CrabAnimScript.ATTACK)) == false && Boolean.class.cast(context.get(CrabAnimScript.FLY)) == true;
                    }
                });
        FSM fsm = builder.build();

        ObjectMap<String, AnimatorStaticPart> animators = new ObjectMap<String, AnimatorStaticPart>();
        animators.put(animatorIdle.name, animatorIdle);
        animators.put(animatorMovement.name, animatorMovement);
        animators.put(animatorAttack.name, animatorAttack);
        animators.put(animatorJump.name, animatorJump);
        animators.put(animatorFly.name, animatorFly);

        SimpleAnimation simpleAnimation = new SimpleAnimation(Animations.CRAB, fsm, crabAtlas, animators);
        // статика анимации
        return simpleAnimation;
    }


}
