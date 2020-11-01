package com.packt.flappeebee.model;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
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
import com.github.br.gdx.simple.console.Console;
import com.github.br.gdx.simple.console.ConsoleOffOnCallback;

import static com.packt.flappeebee.model.LayerEnum.*;


public class GameWorld extends ScreenAdapter {

    private GameWorldSettings gameWorldSettings;

    private EcsContainer container;
    private Console console;

    public GameWorld(GameWorldSettings gameWorldSettings) {
        this.gameWorldSettings = gameWorldSettings;
        //TODO
        initEcsContainer();
        initConsole();
        setInputProcessor();

        container.createEntity("background", GameObjectFactory.createBackground());
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
        waveShader.title = "wave";
        waveShader.vertexShader = Gdx.files.internal("shaders/wave_shader.vsh");
        waveShader.fragmentShader = Gdx.files.internal("shaders/wave_shader.fsh");
        waveShader.shaderUpdater = new ShaderUpdater() {

            private float time;

            @Override
            public void update(ShaderProgram shaderProgram) {
                time += Gdx.graphics.getDeltaTime();
                int uTime = shaderProgram.getUniformLocation("u_time");
                shaderProgram.setUniformf(uTime, time);
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

        container = new EcsContainer(settings);

        // инициализация систем. Порядок очень важен!
        container.addSystem(ScriptSystem.class);
        container.addSystem(PhysicsSystem.class);
        container.addSystem(AnimationSystem.class);
        container.addSystem(
                new RenderSystem(
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
