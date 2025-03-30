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
import com.packt.flappeebee.screen.CrabAnimationState;
import com.packt.flappeebee.screen.level.Tags;
import com.packt.flappeebee.screen.level.level1.systems.components.AnimationComponent;
import com.packt.flappeebee.screen.level.level1.systems.components.HeroComponent;
import com.packt.flappeebee.screen.level.level1.systems.components.InputComponent;
import com.packt.flappeebee.screen.level.level1.systems.components.PearlComponent;
import games.rednblack.editor.renderer.components.DimensionsComponent;
import games.rednblack.editor.renderer.components.MainItemComponent;
import games.rednblack.editor.renderer.components.TransformComponent;
import games.rednblack.editor.renderer.components.physics.PhysicsBodyComponent;
import games.rednblack.editor.renderer.physics.PhysicsContact;
import games.rednblack.editor.renderer.scripts.BasicScript;
import games.rednblack.editor.renderer.utils.ItemWrapper;

import java.util.HashSet;
import java.util.Set;

public class PlayerScript extends BasicScript implements PhysicsContact {

    protected ComponentMapper<PhysicsBodyComponent> physicsMapper;
    protected ComponentMapper<TransformComponent> transformMapper;
    protected ComponentMapper<MainItemComponent> mainItemMapper;
    protected ComponentMapper<DimensionsComponent> dimensionsMapper;

    protected ComponentMapper<InputComponent> inputMapper;
    protected ComponentMapper<AnimationComponent> animationMapper;

    protected ComponentMapper<PearlComponent> pearlMapper;
    protected ComponentMapper<HeroComponent> heroMapper;

    protected com.artemis.World engine;

    private PhysicsBodyComponent physicsBodyComponent;

    private InputComponent inputComponent;
    private AnimationComponent animationComponent;

    private final Vector2 impulse = new Vector2(0, 0);
    private final Vector2 speed = new Vector2(0, 0);

    private boolean attack = false;
    private boolean animJump = false;

    // jump
    private int jumpTimeout = 0;
    private final Set<Fixture> fixturesUnderfoot = new HashSet<>();


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
        }

        if (inputActions.getAction(HeroActions.MOVE_RIGHT)) {
            movePlayer(HeroActions.MOVE_RIGHT);
        }
        animationContext.update(CrabAnimationState.MOVEMENT, Math.abs(linearVelocity.x));

        if (inputActions.getAction(HeroActions.JUMP) && !animJump && isCanJumpNow()) {
            animJump = true;
            jumpTimeout = 15; // 15 frames
            movePlayer(HeroActions.JUMP);
            animationContext.update(CrabAnimationState.JUMP, true);
        }

        if (linearVelocity.y < -0.3) {
            animationContext.update(CrabAnimationState.FLY, true);
        } else if (linearVelocity.y >= 0 && linearVelocity.y <= 0.2f) {
            animationContext.update(CrabAnimationState.FLY, false);
        }

        if (inputActions.getAction(HeroActions.ATTACK) && !attack) {
            attack = true;
            animationContext.update(CrabAnimationState.ATTACK, true);
            //TODO
        }


        if (CrabAnimationState.ATTACK.equals(animationContext.getCurrentState())) {
            if (SimpleAnimatorUtils.isAnimationFinished(animationComponent.simpleAnimationComponent.animatorDynamicPart)) {
                attack = false;
                animationContext.update(CrabAnimationState.ATTACK, false);
            }
        }
        if (CrabAnimationState.JUMP.equals(animationContext.getCurrentState())) {
            if (SimpleAnimatorUtils.isAnimationFinished(animationComponent.simpleAnimationComponent.animatorDynamicPart)) {
                animJump = false;
                animationContext.update(CrabAnimationState.JUMP, false);
                animationContext.update(CrabAnimationState.FLY, true);
            }
        }

        if (jumpTimeout > 0) {
            jumpTimeout--;
        }
    }

    public void movePlayer(int direction) {
        Body body = physicsBodyComponent.body;

        speed.set(body.getLinearVelocity());

        switch (direction) {
            case HeroActions.MOVE_LEFT:
                impulse.set(-2f, speed.y);
                break;
            case HeroActions.MOVE_RIGHT:
                impulse.set(2f, speed.y);
                break;
            case HeroActions.JUMP:
                impulse.set(speed.x, 1.5f);
                break;
        }

        body.applyLinearImpulse(impulse.sub(speed), body.getWorldCenter(), true);
    }

    @Override
    public void dispose() {

    }

    @Override
    public void beginContact(int contactEntity, Fixture contactFixture, Fixture ownFixture, Contact contact) {
        HeroComponent heroComponent = heroMapper.get(entity);
        MainItemComponent mainItemComponent = mainItemMapper.get(contactEntity);

        if (mainItemComponent.tags.contains(Tags.PLATFORM)) {
            fixturesUnderfoot.add(contactFixture);
        }

        PearlComponent pearlComponent = pearlMapper.get(contactEntity);
        if (pearlComponent != null && pearlComponent.value > 0) {
            ItemWrapper itemWrapper = new ItemWrapper(contactEntity, engine);
            ItemWrapper child = itemWrapper.getChild(Tags.PEARL_ID);
            if (child != null) {
                heroComponent.setPearlsCount(heroComponent.getPearlsCount() + pearlComponent.value);
                pearlComponent.value--;
                engine.delete(child.getEntity());
            }
        }
    }

    @Override
    public void endContact(int contactEntity, Fixture contactFixture, Fixture ownFixture, Contact contact) {
        MainItemComponent mainItemComponent = mainItemMapper.get(contactEntity);

        if (mainItemComponent.tags.contains(Tags.PLATFORM)) {
            fixturesUnderfoot.remove(contactFixture);
        }
    }

    @Override
    public void preSolve(int contactEntity, Fixture contactFixture, Fixture ownFixture, Contact contact) {
    }

    @Override
    public void postSolve(int contactEntity, Fixture contactFixture, Fixture ownFixture, Contact contact) {
    }


    private boolean isCanJumpNow() {
        if (jumpTimeout > 0) {
            return false;
        }

        return !fixturesUnderfoot.isEmpty();
    }

}
