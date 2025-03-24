package com.packt.flappeebee.screen.level.level1.script;

import com.artemis.ComponentMapper;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.github.br.gdx.simple.animation.component.SimpleAnimatorUtils;
import com.github.br.gdx.simple.animation.fsm.FsmContext;
import com.github.br.simple.input.InputActions;
import com.packt.flappeebee.action.HeroActions;
import com.packt.flappeebee.screen.level.level1.systems.components.AnimationComponent;
import com.packt.flappeebee.screen.level.level1.systems.components.InputComponent;
import games.rednblack.editor.renderer.components.DimensionsComponent;
import games.rednblack.editor.renderer.components.MainItemComponent;
import games.rednblack.editor.renderer.components.TransformComponent;
import games.rednblack.editor.renderer.components.physics.PhysicsBodyComponent;
import games.rednblack.editor.renderer.physics.PhysicsContact;
import games.rednblack.editor.renderer.scripts.BasicScript;

public class PlayerScript extends BasicScript implements PhysicsContact {

    public static final String IDLE = "IDLE";
    public static final String JUMP = "JUMP";
    public static final String MOVEMENT = "MOVEMENT";
    public static final String ATTACK = "ATTACK";
    public static final String FLY = "FLY";

    protected ComponentMapper<PhysicsBodyComponent> physicsMapper;
    protected ComponentMapper<TransformComponent> transformMapper;
    protected ComponentMapper<MainItemComponent> mainItemMapper;
    protected ComponentMapper<DimensionsComponent> dimensionsMapper;

    protected ComponentMapper<InputComponent> inputMapper;
    protected ComponentMapper<AnimationComponent> animationMapper;

    private PhysicsBodyComponent physicsBodyComponent;

    private InputComponent inputComponent;
    private AnimationComponent animationComponent;

    private final Vector2 impulse = new Vector2(0, 0);
    private final Vector2 speed = new Vector2(0, 0);


    @Override
    public void init(int item) {
        super.init(item);

        physicsBodyComponent = physicsMapper.get(item);
        inputComponent = inputMapper.get(item);
        animationComponent = animationMapper.get(item);
    }

    @Override
    public void act(float delta) {
        Vector2 linearVelocity = physicsBodyComponent.body.getLinearVelocity();
        FsmContext animationContext = animationComponent.simpleAnimationComponent.fsmContext;
        InputActions inputActions = inputComponent.inputActions;
        if (inputActions.getAction(HeroActions.MOVE_LEFT)) {
            movePlayer(HeroActions.MOVE_LEFT);
            animationContext.update(MOVEMENT, Math.abs(linearVelocity.x));
        }

        if (inputActions.getAction(HeroActions.MOVE_RIGHT)) {
            movePlayer(HeroActions.MOVE_RIGHT);
            animationContext.update(MOVEMENT, Math.abs(linearVelocity.x));
        }

        if (inputActions.getAction(HeroActions.JUMP)) {
            movePlayer(HeroActions.JUMP);
            animationContext.update(JUMP, true);
        }

        if (linearVelocity.y < -0.3) {
            animationContext.update(FLY, true);
        } else if (linearVelocity.y == 0) {
            animationContext.update(FLY, false);
        }

        if (inputActions.getAction(HeroActions.ATTACK)) {
            animationContext.update(ATTACK, true);
            //TODO
        }


        if (ATTACK.equals(animationContext.getCurrentState())) {
            if (SimpleAnimatorUtils.isAnimationFinished(animationComponent.simpleAnimationComponent.animatorDynamicPart)) {
                //attack = false;
                animationContext.update(ATTACK, false);
            }
        }
        if (JUMP.equals(animationContext.getCurrentState())) {
            if (SimpleAnimatorUtils.isAnimationFinished(animationComponent.simpleAnimationComponent.animatorDynamicPart)) {
                //jump = false;
                animationContext.update(JUMP, false);
                animationContext.update(FLY, true);
            }
        }
    }

    public void movePlayer(int direction) {
        Body body = physicsBodyComponent.body;

        speed.set(body.getLinearVelocity());

        switch (direction) {
            case HeroActions.MOVE_LEFT:
                impulse.set(-2, speed.y);
                break;
            case HeroActions.MOVE_RIGHT:
                impulse.set(2, speed.y);
                break;
            case HeroActions.JUMP:
                TransformComponent transformComponent = transformMapper.get(entity);
                impulse.set(speed.x, transformComponent.y < 3 ? 2 : speed.y);
                break;
        }

        body.applyLinearImpulse(impulse.sub(speed), body.getWorldCenter(), true);
    }

    @Override
    public void dispose() {

    }

    @Override
    public void beginContact(int contactEntity, Fixture contactFixture, Fixture ownFixture, Contact contact) {
        MainItemComponent mainItemComponent = mainItemMapper.get(contactEntity);

//        PlayerComponent playerComponent = playerMapper.get(animEntity);
//        if (mainItemComponent.tags.contains("platform")) {
//            playerComponent.touchedPlatforms++;
//        }


    }

    @Override
    public void endContact(int contactEntity, Fixture contactFixture, Fixture ownFixture, Contact contact) {
        MainItemComponent mainItemComponent = mainItemMapper.get(contactEntity);

//        PlayerComponent playerComponent = playerMapper.get(animEntity);
//        if (mainItemComponent.tags.contains("platform")) {
//            playerComponent.touchedPlatforms--;
//        }
    }

    @Override
    public void preSolve(int contactEntity, Fixture contactFixture, Fixture ownFixture, Contact contact) {
        TransformComponent transformComponent = transformMapper.get(this.entity);

        TransformComponent colliderTransform = transformMapper.get(contactEntity);
        DimensionsComponent colliderDimension = dimensionsMapper.get(contactEntity);

        if (transformComponent.y < colliderTransform.y + colliderDimension.height) {
            contact.setFriction(0);
        } else {
            contact.setFriction(1);
        }
    }

    @Override
    public void postSolve(int contactEntity, Fixture contactFixture, Fixture ownFixture, Contact contact) {

    }
}
