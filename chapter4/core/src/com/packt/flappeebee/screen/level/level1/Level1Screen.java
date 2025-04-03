package com.packt.flappeebee.screen.level.level1;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.github.br.gdx.simple.animation.SimpleAnimation;
import com.github.br.gdx.simple.structure.screen.AbstractGameScreen;
import com.github.br.simple.input.InputSystem;
import com.packt.flappeebee.Resources;
import com.packt.flappeebee.action.ActionMapperImpl;
import com.packt.flappeebee.model.GameObjectFactory;
import com.packt.flappeebee.screen.HUD;
import com.packt.flappeebee.screen.level.Tags;
import com.packt.flappeebee.screen.level.level1.script.HeroScript;
import com.packt.flappeebee.screen.level.level1.systems.components.AnimationComponent;
import com.packt.flappeebee.screen.level.level1.systems.AnimationSystem;
import com.packt.flappeebee.screen.level.level1.systems.CameraSystem;
import com.packt.flappeebee.screen.level.level1.systems.components.HeroComponent;
import com.packt.flappeebee.screen.level.level1.systems.components.InputComponent;
import com.packt.flappeebee.screen.level.level1.systems.components.PearlComponent;
import games.rednblack.editor.renderer.SceneConfiguration;
import games.rednblack.editor.renderer.SceneLoader;
import games.rednblack.editor.renderer.resources.AsyncResourceManager;
import games.rednblack.editor.renderer.utils.ComponentRetriever;
import games.rednblack.editor.renderer.utils.ItemWrapper;

public class Level1Screen extends AbstractGameScreen {

    private SceneLoader sceneLoader;
    private AsyncResourceManager asyncResourceManager;
    private HUD hud;

    private OrthographicCamera camera;
    private Viewport viewport;

    private com.artemis.World artemisWorld;

    private InputSystem inputSystem;

    @Override
    public void show() {
        super.show();

        hud = new HUD();
        hud.setGameManager(getGameManager());
        hud.show();

        camera = new OrthographicCamera();
        viewport = new ExtendViewport(10, 7, camera);

        AssetManager assetManager = getGameManager().assetManager;
        asyncResourceManager = assetManager.get(Resources.HyperLap2D.PROJECT, AsyncResourceManager.class);

        SceneConfiguration config = new SceneConfiguration();
        //config.setRendererSystem();
        config.setResourceRetriever(asyncResourceManager);

        CameraSystem cameraSystem = new CameraSystem(6.5f, 40, 4, 6);

        AnimationSystem animationSystem = new AnimationSystem();
        animationSystem.addAnimation(assetManager.<SimpleAnimation>get(Resources.Animations.CRAB_ANIM_FSM));

        config.addSystem(cameraSystem);
        config.addSystem(animationSystem);

        sceneLoader = new SceneLoader(config);
        artemisWorld = sceneLoader.getEngine();

        // после new SceneLoader(config) должны быть добавления новых компонент во фреймворк
        ComponentRetriever.addMapper(AnimationComponent.class);
        ComponentRetriever.addMapper(InputComponent.class);
        ComponentRetriever.addMapper(HeroComponent.class);

        sceneLoader.loadScene("MainScene", viewport);
        ItemWrapper root = new ItemWrapper(sceneLoader.getRoot(), artemisWorld);

        // hero
        ItemWrapper hero = root.getChild(Tags.HERO_ID);
        int heroEntity = hero.getEntity();
        AnimationComponent animationComponent = ComponentRetriever.create(heroEntity, AnimationComponent.class, artemisWorld);
        animationComponent.simpleAnimationComponent = GameObjectFactory.createAnimationComponentCrab();

        HeroScript heroScript = new HeroScript();
        hero.addScript(heroScript);

        // camera
        cameraSystem.setFocus(heroEntity);

        // InputSystem
        inputSystem = new InputSystem(5, new ActionMapperImpl());
        InputComponent inputComponent = ComponentRetriever.create(heroEntity, InputComponent.class, artemisWorld);
        inputComponent.inputActions = inputSystem.getInputActions();
        Gdx.input.setInputProcessor(inputSystem.getInputProcessor());

        // collectables
        sceneLoader.addComponentByTagName(Tags.SEA_SHELL, PearlComponent.class);

        // hero component
        HeroComponent heroComponent = ComponentRetriever.create(heroEntity, HeroComponent.class, artemisWorld);
        heroComponent.setLifeCount(4);
        heroComponent.addListener(hud);
        heroComponent.notifyListeners();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(Color.BLACK.r, Color.BLACK.g, Color.BLACK.b, Color.BLACK.a);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        viewport.apply();

        inputSystem.update(delta);
        artemisWorld.process();
        inputSystem.reset();

        hud.render(delta);
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
        if (width != 0 && height != 0) {
            sceneLoader.resize(width, height);
            hud.resize(width, height);
        }

    }

    @Override
    public void pause() {
        if (hud == null) {
            return;
        }
        hud.pause();
    }

    @Override
    public void resume() {
        if (hud == null) {
            return;
        }
        hud.resume();
    }

    @Override
    public void hide() {
        if (hud == null) {
            return;
        }
        hud.hide();
    }

    @Override
    public void dispose() {
        if (hud == null) {
            return;
        }
        hud.dispose();
    }

}
