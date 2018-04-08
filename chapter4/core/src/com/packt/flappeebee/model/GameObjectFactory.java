package com.packt.flappeebee.model;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.github.br.ecs.simple.engine.EcsContainer;
import com.github.br.ecs.simple.engine.EcsScript;
import com.github.br.ecs.simple.system.animation.AnimationComponent;
import com.github.br.ecs.simple.system.animation.AnimationController;
import com.github.br.ecs.simple.system.animation.Animator;
import com.github.br.ecs.simple.fsm.FSM;
import com.github.br.ecs.simple.fsm.FsmContext;
import com.github.br.ecs.simple.fsm.FsmPredicate;
import com.github.br.ecs.simple.fsm.FsmState;
import com.github.br.ecs.simple.system.physics.Boundary;
import com.github.br.ecs.simple.system.physics.PhysicsComponent;
import com.github.br.ecs.simple.system.render.RendererComponent;
import com.github.br.ecs.simple.system.script.ScriptComponent;
import com.github.br.ecs.simple.system.transform.TransformComponent;
import com.packt.flappeebee.model.scripts.*;

import java.util.Arrays;
import java.util.List;

public final class GameObjectFactory {

    private GameObjectFactory() {
    }

    private static TextureAtlas packedimages;
    private static TextureAtlas animAtlas;
    private static TextureAtlas crabAtlas;

    private static Texture background;

    static {
        packedimages = new TextureAtlas(Gdx.files.internal("packedimages/example.atlas"));
        animAtlas = new TextureAtlas(Gdx.files.internal("packedimages/anim_example.atlas"));
        crabAtlas = new TextureAtlas(Gdx.files.internal("crab/crab.atlas"));

        background = new Texture(Gdx.files.internal("background.png"));
    }

    public static void createCloud(EcsContainer container) {
        TransformComponent transformComponent = new TransformComponent();
        transformComponent.position = new Vector2(660f, 460 - MathUtils.random(0, 200));
        transformComponent.rotation = 0f;

        RendererComponent rendererComponent = new RendererComponent();
        rendererComponent.textureRegion = packedimages.findRegion("cloud");
        rendererComponent.layer = LayerEnum.PRE_BACKGROUND.name();

        ScriptComponent scriptComponent = new ScriptComponent();
        scriptComponent.scripts = Arrays.<EcsScript>asList(new CloudScript());

        container.createEntity("cloud", transformComponent, rendererComponent, scriptComponent);
    }

    public static void createBackground(EcsContainer container){
        TransformComponent transformComponent = new TransformComponent();
        transformComponent.position = new Vector2(0f, 0f);
        transformComponent.rotation = 0f;

        RendererComponent rendererComponent = new RendererComponent();
        rendererComponent.textureRegion = new TextureRegion(background);
        rendererComponent.layer = LayerEnum.BACKGROUND.name();

        container.createEntity("background", transformComponent, rendererComponent);
    }

    public static void createFlappee(EcsContainer container) {
        //todo сделать нормальные билдеры для сущностей

        int x = MathUtils.random(0, 480);
        TransformComponent transformComponent = new TransformComponent();
        transformComponent.position = new Vector2(x, 600);
        transformComponent.rotation = 0f;

        PhysicsComponent physicsComponent = new PhysicsComponent();
        physicsComponent.movement = new Vector2(0f, 0f);
        physicsComponent.acceleration = GameConstants.DIVE_ACCEL;
        physicsComponent.boundary = new Boundary(new Circle(new Vector2(32f, 32f), 32f));

        ScriptComponent scriptComponent = new ScriptComponent();
        scriptComponent.scripts = Arrays.<EcsScript>asList(new FlappeeScript());

        RendererComponent rendererComponent = new RendererComponent();
        rendererComponent.textureRegion = packedimages.findRegion("bee");
        rendererComponent.layer = LayerEnum.FRONT_EFFECTS.name();

        container.createEntity("bee", transformComponent, physicsComponent, scriptComponent, rendererComponent);
    }

    private static int plantCount = 1;

    public static void createPlant(EcsContainer container) {
        TransformComponent transformComponent = new TransformComponent();
        transformComponent.position = new Vector2(85 * plantCount, 250);
        transformComponent.rotation = 0f;

        PhysicsComponent physicsComponent = new PhysicsComponent();
        physicsComponent.movement = new Vector2(0f, 0f);
        physicsComponent.acceleration = GameConstants.DIVE_ACCEL;
        // заполняем физику
        physicsComponent.boundary = new Boundary(new Rectangle(0, 0, 64, 128));

        ScriptComponent scriptComponent = new ScriptComponent();
        if ((plantCount & 1) == 1) {
            scriptComponent.scripts = Arrays.<EcsScript>asList(new FlowerScript2());
        } else {
            scriptComponent.scripts = Arrays.<EcsScript>asList(new FlowerScript());
        }

        RendererComponent rendererComponent = new RendererComponent();
        rendererComponent.textureRegion = animAtlas.findRegion("anim", 0);
        rendererComponent.layer = (plantCount & 1) == 1? LayerEnum.PRE_BACKGROUND.name() : LayerEnum.FRONT_EFFECTS.name();

        // АНИМАЦИЯ
        AnimationComponent animationComponent = new AnimationComponent();
        Animator animatorGrow = new Animator("grow", animAtlas.getRegions(), 1f / 30);
        animatorGrow.setPlayMode(Animation.PlayMode.LOOP_PINGPONG);
        animatorGrow.setLooping(true);

        FsmContext fsmContext = new FsmContext();
        FSM.Builder builder = new FSM.Builder();
        FsmState.Builder stateBuilder = new FsmState.Builder();
        stateBuilder.setName("grow").setStartState(true).setEndState(false);
        builder.addState(stateBuilder.build());
        builder.addContext(fsmContext);
        FSM fsm = builder.build();

        AnimationController animationController = new AnimationController(new Animator[]{animatorGrow}, fsm);
        animationComponent.controller = animationController;

        container.createEntity("plant" + plantCount,
                                transformComponent,
                                physicsComponent,
                                scriptComponent,
                                rendererComponent,
                                animationComponent);
        plantCount++;
    }

    public static void createCrab(EcsContainer container) {
        TransformComponent transformComponent = new TransformComponent();
        transformComponent.position = new Vector2(250, 250);
        transformComponent.rotation = 0f;

        PhysicsComponent physicsComponent = new PhysicsComponent();
        physicsComponent.movement = new Vector2(0f, 0f);
        physicsComponent.acceleration = GameConstants.CRAB_DIVE_ACCEL;
        physicsComponent.boundary = new Boundary(new Rectangle(0, 0, 75, 64));

        ScriptComponent scriptComponent = new ScriptComponent();
        scriptComponent.scripts = Arrays.asList(new CrabScript(), new CrabAnimScript());

        RendererComponent rendererComponent = new RendererComponent();
        rendererComponent.textureRegion = crabAtlas.findRegion("crab", 1);
        rendererComponent.layer = LayerEnum.MAIN_LAYER.name();

        // АНИМАЦИЯ
        AnimationComponent animationComponent = new AnimationComponent();
        Array<TextureAtlas.AtlasRegion> atlasRegions = crabAtlas.getRegions();
        List<TextureAtlas.AtlasRegion> atlasList = Arrays.asList(atlasRegions.items);

        Array<TextureAtlas.AtlasRegion> movementArray = new Array<TextureAtlas.AtlasRegion>(atlasList.subList(0, 44).toArray(new TextureAtlas.AtlasRegion[0]));
        Array<TextureAtlas.AtlasRegion> attackArray = new Array<TextureAtlas.AtlasRegion>(atlasList.subList(45, 75).toArray(new TextureAtlas.AtlasRegion[0]));
        Array<TextureAtlas.AtlasRegion> jumpArray = new Array<TextureAtlas.AtlasRegion>(atlasList.subList(76, 86).toArray(new TextureAtlas.AtlasRegion[0]));
        Array<TextureAtlas.AtlasRegion> flyArray = new Array<TextureAtlas.AtlasRegion>(atlasList.subList(87, 104).toArray(new TextureAtlas.AtlasRegion[0]));

        Animator animatorIdle = new Animator(CrabAnimScript.IDLE, jumpArray, 1f / 10);
        animatorIdle.setPlayMode(Animation.PlayMode.LOOP);
        animatorIdle.setLooping(true);
        Animator animatorMovement = new Animator(CrabAnimScript.MOVEMENT, movementArray, 1f / 60);
        animatorMovement.setPlayMode(Animation.PlayMode.LOOP);
        animatorMovement.setLooping(true);
        Animator animatorAttack = new Animator(CrabAnimScript.ATTACK, attackArray, 1f / 30);
        Animator animatorJump = new Animator(CrabAnimScript.JUMP, jumpArray, 1f / 60);
        Animator animatorFly = new Animator(CrabAnimScript.FLY, flyArray, 1f / 30);
        animatorFly.setPlayMode(Animation.PlayMode.LOOP);
        animatorFly.setLooping(true);

        FsmContext fsmContext = new FsmContext();
        fsmContext.insert(CrabAnimScript.MOVEMENT, 0f);
        fsmContext.insert(CrabAnimScript.ATTACK, false);
        fsmContext.insert(CrabAnimScript.JUMP, false);
        fsmContext.insert(CrabAnimScript.FLY, false);

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

        builder.addContext(fsmContext)
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

        AnimationController animationController = new AnimationController(
                new Animator[]{animatorIdle, animatorMovement, animatorAttack, animatorJump, animatorFly}, builder.build());
        animationComponent.controller = animationController;
        // АНИМАЦИЯ

        container.createEntity("crab", transformComponent, physicsComponent, scriptComponent, rendererComponent, animationComponent);
    }


}
