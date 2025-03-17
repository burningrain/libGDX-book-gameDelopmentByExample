package com.packt.flappeebee.model;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.github.br.ecs.simple.engine.EcsContainer;
import com.github.br.ecs.simple.engine.EcsSettings;
import com.github.br.ecs.simple.engine.debug.drawobject.DebugDrawObject;
import com.github.br.ecs.simple.system.animation.AnimationSystem;
import com.github.br.ecs.simple.system.physics.PhysicsSystem;
import com.github.br.ecs.simple.system.render.LayerData;
import com.github.br.ecs.simple.system.render.RenderSystem;
import com.github.br.ecs.simple.system.render.ShaderData;
import com.github.br.ecs.simple.system.render.ShaderUpdater;
import com.github.br.ecs.simple.system.script.ScriptSystem;
import com.github.br.gdx.simple.animation.SimpleAnimation;
import com.github.br.gdx.simple.animation.SimpleAnimationSyncLoader;
import com.github.br.gdx.simple.console.Console;
import com.github.br.gdx.simple.console.ConsoleOffOnCallback;
import com.packt.flappeebee.animation.AnimationFactory;

import static com.packt.flappeebee.model.LayerEnum.*;


public class GameWorld extends ScreenAdapter {

    public static final String CRAB_ANIM = "animation/crab/crab.afsm";

    public static final String BLINK_SHADER = "shaders/blink_shader.vert";
    public static final String BACKGROUND_PNG = "background.png";

    private final FileHandleResolver resolver = new InternalFileHandleResolver();
    private final AssetManager assetManager = new AssetManager(resolver);


    private final GameWorldSettings gameWorldSettings;
    private EcsContainer container;
    private Console console;

    public GameWorld(GameWorldSettings gameWorldSettings) {
        this.gameWorldSettings = gameWorldSettings;
        //TODO
        loadAssets();
        initEcsContainer();
        initConsole();
        setInputProcessor();

        container.createEntity("background", GameObjectFactory.createBackground(
                new TextureRegion(assetManager.<Texture>get(BACKGROUND_PNG)))
        );
    }

    private void loadAssets() {
        assetManager.setLoader(SimpleAnimation.class, new SimpleAnimationSyncLoader(resolver));
        assetManager.load(CRAB_ANIM, SimpleAnimation.class);

        assetManager.load(BACKGROUND_PNG, Texture.class);

        assetManager.load(BLINK_SHADER, ShaderProgram.class);
        assetManager.finishLoading();
    }

    private void initConsole() {
        //todo пока так, потом подумать с местом инициализации. Плохо, что завязка на скин в ecs-контейнере
        console = new Console(
                Input.Keys.F9,
                DebugDrawObject.DEFAULT_SKIN,
                true,
                new ConsoleOffOnCallback() {
                    @Override
                    public void call(boolean isActive) {
                        container.setDebugMode(isActive);
                    }
                });
        console.addCommands(CommandsFactory.getCommands(container));
    }

    private void initEcsContainer() {
        ShaderData waveShader = new ShaderData();
        waveShader.title = BLINK_SHADER;
        waveShader.shaderUpdater = new ShaderUpdater() {

            private float time;

            @Override
            public void update(ShaderProgram shp) {
                time += Gdx.graphics.getDeltaTime();
                int uTime = shp.getUniformLocation("u_time");
                shp.setUniformf(uTime, time);
            }

        };

        // инициализация настроек
        EcsSettings settings = new EcsSettings();
        settings.layers = new LayerData[]{
                new LayerData(BACKGROUND.name()),
                new LayerData(PRE_BACKGROUND.name()),
                new LayerData(BACK_EFFECTS.name()),
                new LayerData(MAIN_LAYER.name()),
                new LayerData(FRONT_EFFECTS.name(), waveShader)
        };
        settings.isDebugEnabled = true;

        // загрузка анимаций
        AnimationSystem animationSystem = new AnimationSystem();
        animationSystem.addAnimation(assetManager.<SimpleAnimation>get(CRAB_ANIM));
        //animationSystem.addAnimation(AnimationFactory.createCrab());
        animationSystem.addAnimation(AnimationFactory.createPlant());

        container = new EcsContainer(settings);
        // инициализация систем. Порядок очень важен!
        container.addSystem(ScriptSystem.class);
        container.addSystem(PhysicsSystem.class);
        container.addSystem(animationSystem);
        container.addSystem(
                new RenderSystem(
                        assetManager,
                        createViewport(gameWorldSettings.virtualWidth, gameWorldSettings.virtualHeight),
                        settings.layers
                )
        );
        container.init();
    }

    private void setInputProcessor() {
        InputMultiplexer inputMultiplexer = new InputMultiplexer();
        inputMultiplexer.addProcessor(console.getInputProcessor());
        inputMultiplexer.addProcessor(container.getInputProcessor());
        Gdx.input.setInputProcessor(inputMultiplexer); //fixme криво, но зато контроль у клиента
    }

    @Override
    public void render(float delta) {
        container.render(delta);
        console.render(delta);
    }

    @Override
    public void resize(int width, int height) {
        container.resize(width, height);
        console.resize(width, height);
    }

    protected Viewport createViewport(float worldWidth, float worldHeight) {
        Camera camera = new OrthographicCamera();
        camera.position.set(worldWidth / 2, worldHeight / 2, 0);
        camera.update();
        return new StretchViewport(worldWidth, worldHeight, camera);
    }

}
