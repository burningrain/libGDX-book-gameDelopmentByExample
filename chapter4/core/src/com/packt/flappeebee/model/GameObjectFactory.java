package com.packt.flappeebee.model;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.github.br.ecs.simple.engine.EcsComponent;
import com.github.br.ecs.simple.engine.EcsScript;
import com.github.br.ecs.simple.system.animation.AnimationComponent;
import com.github.br.ecs.simple.system.physics.PhysicsComponent;
import com.github.br.ecs.simple.system.render.RendererComponent;
import com.github.br.ecs.simple.system.script.ScriptComponent;
import com.github.br.ecs.simple.system.transform.TransformComponent;
import com.github.br.gdx.simple.animation.component.AnimatorDynamicPart;
import com.github.br.gdx.simple.animation.component.SimpleAnimationComponent;
import com.github.br.gdx.simple.animation.fsm.FsmContext;
import com.packt.flappeebee.animation.Animations;
import com.packt.flappeebee.model.scripts.*;

public final class GameObjectFactory {

    private GameObjectFactory() {
    }

    private static final TextureAtlas packedimages;
    private static final Texture background;

    static {
        packedimages = new TextureAtlas(Gdx.files.internal("packedimages/example.atlas"));
        background = new Texture(Gdx.files.internal("background.png"));
    }

    @Deprecated // "оставил как пример загрузки региона из атласа"
    public static EcsComponent[] createCloud() {
        TransformComponent transformComponent = new TransformComponent();
        transformComponent.position = new Vector2(660f, 460 - MathUtils.random(0, 200));
        transformComponent.rotation = 0f;

        RendererComponent rendererComponent = new RendererComponent();
        rendererComponent.textureRegion = packedimages.findRegion("cloud");
        rendererComponent.layer = LayerEnum.PRE_BACKGROUND.name();

        ScriptComponent scriptComponent = new ScriptComponent();
        scriptComponent.scripts = new Array<EcsScript>(new EcsScript[]{new CloudScript()});

        return new EcsComponent[]{transformComponent, rendererComponent, scriptComponent};
    }

    public static EcsComponent[] createBackground(TextureRegion region) {
        TransformComponent transformComponent = new TransformComponent();
        transformComponent.position = new Vector2(0f, 0f);
        transformComponent.rotation = 0f;

        RendererComponent rendererComponent = new RendererComponent();
        rendererComponent.textureRegion = region;
        rendererComponent.layer = LayerEnum.BACKGROUND.name();

        return new EcsComponent[]{transformComponent, rendererComponent};
    }

    public static EcsComponent[] createFlappee() {
        //todo сделать нормальные билдеры для сущностей

        int x = MathUtils.random(0, 480);
        TransformComponent transformComponent = new TransformComponent();
        transformComponent.position = new Vector2(x, 600);
        transformComponent.rotation = 0f;

        PhysicsComponent physicsComponent = new PhysicsComponent();
        physicsComponent.movement = new Vector2(0f, 0f);
        physicsComponent.acceleration = GameConstants.DIVE_ACCEL;
        physicsComponent.shape = new Circle(new Vector2(32f, 32f), 32f);

        ScriptComponent scriptComponent = new ScriptComponent();
        scriptComponent.scripts = new Array<EcsScript>(new EcsScript[]{new FlappeeScript()});

        RendererComponent rendererComponent = new RendererComponent();
        rendererComponent.textureRegion = packedimages.findRegion("bee");
        rendererComponent.layer = LayerEnum.FRONT_EFFECTS.name();

        return new EcsComponent[]{transformComponent, physicsComponent, scriptComponent, rendererComponent};
    }

    public static EcsComponent[] createPlant(int plantCount) {
        TransformComponent transformComponent = new TransformComponent();
        transformComponent.position = new Vector2(85 * plantCount, 250);
        transformComponent.rotation = 0f;

        PhysicsComponent physicsComponent = new PhysicsComponent();
        physicsComponent.movement = new Vector2(0f, 0f);
        physicsComponent.acceleration = GameConstants.DIVE_ACCEL;
        // заполняем физику
        physicsComponent.shape = new Rectangle(0, 0, 64, 128);

        ScriptComponent scriptComponent = new ScriptComponent();
        if ((plantCount & 1) == 1) {
            scriptComponent.scripts = new Array<EcsScript>(new EcsScript[]{new FlowerScript2()});
        } else {
            scriptComponent.scripts = new Array<EcsScript>(new EcsScript[]{new FlowerScript()});
        }

        RendererComponent rendererComponent = new RendererComponent();
        //rendererComponent.textureRegion = animAtlas.findRegion("anim", 0);
        rendererComponent.layer = (plantCount & 1) == 1 ? LayerEnum.PRE_BACKGROUND.name() : LayerEnum.FRONT_EFFECTS.name();

        // АНИМАЦИЯ
        // динамика анимации
        AnimationComponent animationComponent = new AnimationComponent();
        AnimatorDynamicPart animatorDynamicPart = new AnimatorDynamicPart( /*animatorGrow */);
        animationComponent.simpleAnimationComponent = new SimpleAnimationComponent(Animations.PLANT, new FsmContext(), animatorDynamicPart);
        // динамика анимации

        return new EcsComponent[]{
                transformComponent,
                physicsComponent,
                scriptComponent,
                rendererComponent,
                animationComponent};
    }

    public static EcsComponent[] createCrab() {
        TransformComponent transformComponent = new TransformComponent();
        transformComponent.position = new Vector2(250, 250);
        transformComponent.rotation = 0f;

        PhysicsComponent physicsComponent = new PhysicsComponent();
        physicsComponent.movement = new Vector2(0f, 0f);
        physicsComponent.acceleration = GameConstants.CRAB_DIVE_ACCEL;
        physicsComponent.shape = new Rectangle(0, 0, 75, 64);

        ScriptComponent scriptComponent = new ScriptComponent();
        scriptComponent.scripts = new Array<EcsScript>(new EcsScript[]{new CrabScript(), new CrabAnimScript()});

        RendererComponent rendererComponent = new RendererComponent();
        //rendererComponent.textureRegion = crabAtlas.findRegion("crab", 1);
        rendererComponent.layer = LayerEnum.MAIN_LAYER.name();

        // АНИМАЦИЯ
        // динамика анимации
        FsmContext fsmContext = new FsmContext();
        fsmContext.insert(CrabAnimScript.MOVEMENT, 0f);
        fsmContext.insert(CrabAnimScript.ATTACK, false);
        fsmContext.insert(CrabAnimScript.JUMP, false);
        fsmContext.insert(CrabAnimScript.FLY, false);

        AnimationComponent animationComponent = new AnimationComponent();
        AnimatorDynamicPart animatorDynamicPart = new AnimatorDynamicPart(/*animatorIdle*/);
        animationComponent.simpleAnimationComponent = new SimpleAnimationComponent(Animations.CRAB, fsmContext, animatorDynamicPart);
        // динамика анимации

        // АНИМАЦИЯ

        return new EcsComponent[]{transformComponent, physicsComponent, scriptComponent, rendererComponent, animationComponent};
    }


}
