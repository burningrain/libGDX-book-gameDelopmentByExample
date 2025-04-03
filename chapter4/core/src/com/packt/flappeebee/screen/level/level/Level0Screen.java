package com.packt.flappeebee.screen.level.level;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
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
import com.github.br.ecs.simple.system.render.LayerData;
import com.github.br.ecs.simple.system.render.RenderSystem;
import com.github.br.ecs.simple.system.render.ShaderData;
import com.github.br.ecs.simple.system.render.ShaderUpdater;
import com.github.br.ecs.simple.system.script.ScriptSystem;
import com.github.br.gdx.simple.animation.SimpleAnimation;
import com.github.br.gdx.simple.animation.SimpleAnimationSyncLoader;
import com.github.br.gdx.simple.console.Console;
import com.github.br.gdx.simple.console.ConsoleOffOnCallback;
import com.github.br.gdx.simple.structure.screen.AbstractGameScreen;
import com.github.br.simple.input.InputSystem;
import com.packt.flappeebee.Resources;
import com.packt.flappeebee.action.ActionMapperImpl;
import com.packt.flappeebee.model.CommandsFactory;
import com.packt.flappeebee.model.GameObjectFactory;
import com.packt.flappeebee.model.GameWorldSettings;
import com.packt.flappeebee.screen.level.level.gameloop.GameLoopManager;
import com.packt.flappeebee.screen.level.level.gameloop.GameLoopStrategy;
import com.packt.flappeebee.screen.level.level.physics.InputComponent;
import com.packt.flappeebee.screen.level.level.physics.PhysicsSystem;

import static com.packt.flappeebee.model.LayerEnum.*;

public class Level0Screen extends AbstractGameScreen {

    // загрузка ресурсов
    private final FileHandleResolver resolver = new InternalFileHandleResolver();
    private final AssetManager assetManager = new AssetManager(resolver);

    // ренеринг
    private RenderSystem renderSystem;

    // пользовательский ввод
    private InputSystem inputSystem;
    private Console console;

    // ECS
    private EcsContainer container;

    // game loops
    private final GameLoopManager gameLoopManager = new GameLoopManager();
    private GameWorldSettings gameWorldSettings;

    @Override
    public void show() {
        super.show();
        loadAssets();

        gameWorldSettings = new GameWorldSettings();
        gameWorldSettings.virtualWidth = 360;
        gameWorldSettings.virtualHeight = 277;

        // инициализация настроек
        ShaderData waveShader = new ShaderData();
        waveShader.title = Resources.Shaders.BLINK_SHADER;
        waveShader.shaderUpdater = new ShaderUpdater() {
            private float time;
            @Override
            public void update(ShaderProgram shp) {
                time += Gdx.graphics.getDeltaTime();
                int uTime = shp.getUniformLocation("u_time");
                shp.setUniformf(uTime, time);
            }
        };

        EcsSettings settings = new EcsSettings();
        settings.layers = new LayerData[]{
                new LayerData(BACKGROUND.name()),
                new LayerData(PRE_BACKGROUND.name()),
                new LayerData(BACK_EFFECTS.name()),
                new LayerData(MAIN_LAYER.name()),
                new LayerData(FRONT_EFFECTS.name(), waveShader)
        };
        settings.isDebugEnabled = true;

        inputSystem = new InputSystem(6, new ActionMapperImpl());
        renderSystem = new RenderSystem(
                assetManager,
                createViewport(gameWorldSettings.virtualWidth, gameWorldSettings.virtualHeight),
                settings.layers
        );
        container = initEcsContainer(settings, renderSystem);
        console = initConsole();
        Gdx.input.setInputProcessor(createInputProcessor());

        container.createEntity("background", GameObjectFactory.createBackground(
                new TextureRegion(assetManager.<Texture>get(Resources.Backgrounds.BACKGROUND_PNG)))
        );
        initGameLoops();

        container.createEntity("crab", GameObjectFactory.createCrab(new InputComponent(inputSystem.getInputActions())));
    }

    private void initGameLoops() {
        gameLoopManager.init(
                new GameLoopStrategy() {
                    @Override
                    public void update() {
                        inputSystem.update(1);   // обновляем ввод
                        container.update(1);        // обновляем игровой мир
                        renderSystem.render(1);     // рендерим картинку

                        inputSystem.reset();
                    }
                },
                new GameLoopStrategy() {
                    private long lastTime;
                    @Override
                    public void update() {
                        long currentTime = System.nanoTime();
                        float deltaTime = (currentTime - lastTime) / 1_000_000_000f;

                        inputSystem.update(deltaTime);   // обновляем ввод
                        container.update(deltaTime);     // обновляем игровой мир
                        renderSystem.render(deltaTime);  // рендерим картинку

                        inputSystem.reset();
                        lastTime = currentTime;
                    }
                },
                new GameLoopStrategy() {
                    private static final float TIME_PER_UPDATE = 1 / 60f;
                    private long lastTime = System.nanoTime();
                    private double lag = 0;
                    @Override
                    public void update() {
                        long currentTime = System.nanoTime();
                        float deltaTime = (currentTime - lastTime) / 1_000_000_000f;
                        lastTime = currentTime;
                        lag += deltaTime;

                        inputSystem.update(deltaTime);   // обновляем ввод
                        while(lag > TIME_PER_UPDATE) {
                            container.update(deltaTime); // обновляем игровой мир
                            lag -= TIME_PER_UPDATE;
                        }

                        renderSystem.render(deltaTime);  // рендерим картинку
                        inputSystem.reset();
                    }
                }
        );
    }

    @Override
    public void render(float delta) {
        gameLoopManager.update();
        console.render(delta);
    }


    private void loadAssets() {
        assetManager.setLoader(SimpleAnimation.class, new SimpleAnimationSyncLoader(resolver));
        assetManager.load(Resources.Animations.CRAB_ANIM_FSM, SimpleAnimation.class);
        assetManager.load(Resources.Backgrounds.BACKGROUND_PNG, Texture.class);
        assetManager.load(Resources.Shaders.BLINK_SHADER, ShaderProgram.class);
        assetManager.finishLoading();
    }

    private Console initConsole() {
        //todo Плохо, что здесь завязка на скин в контейнере
        Console console = new Console(
                Input.Keys.F9,
                DebugDrawObject.DEFAULT_SKIN,
                new StretchViewport(
                        gameWorldSettings.virtualWidth,
                        gameWorldSettings.virtualHeight
                ),
                true,
                new ConsoleOffOnCallback() {
                    @Override
                    public void call(boolean isActive) {
                        container.setDebugMode(isActive);
                    }
                });
        console.addCommands(CommandsFactory.getCommands(container, gameLoopManager, getGameManager()));
        return console;
    }

    private EcsContainer initEcsContainer(EcsSettings settings, RenderSystem renderSystem) {
        // загрузка анимаций
        AnimationSystem animationSystem = new AnimationSystem();
        animationSystem.addAnimation(assetManager.<SimpleAnimation>get(Resources.Animations.CRAB_ANIM_FSM));

        EcsContainer container = new EcsContainer(settings);
        // инициализация систем. Порядок важен
        container.addSystem(ScriptSystem.class);
        container.addSystem(PhysicsSystem.class);
        container.addSystem(animationSystem);
        container.addSystem(renderSystem);
        container.init();

        return container;
    }

    private InputMultiplexer createInputProcessor() {
        InputMultiplexer inputMultiplexer = new InputMultiplexer();
        inputMultiplexer.addProcessor(console.getInputProcessor());
        inputMultiplexer.addProcessor(container.getInputProcessor());
        return inputMultiplexer;
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

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void hide() {
    }

    @Override
    public void dispose() {
    }

}
