package com.packt.flappeebee.screen.level.level1;

import com.artemis.EntityEdit;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.github.br.gdx.simple.animation.SimpleAnimation;
import com.github.br.gdx.simple.structure.screen.AbstractGameScreen;
import com.packt.flappeebee.Resources;
import com.packt.flappeebee.model.GameObjectFactory;
import com.packt.flappeebee.screen.level.level1.systems.AnimationComponent;
import com.packt.flappeebee.screen.level.level1.systems.AnimationSystem;
import com.packt.flappeebee.screen.level.level1.systems.CameraSystem;
import games.rednblack.editor.renderer.SceneConfiguration;
import games.rednblack.editor.renderer.SceneLoader;
import games.rednblack.editor.renderer.components.*;
import games.rednblack.editor.renderer.resources.AsyncResourceManager;
import games.rednblack.editor.renderer.utils.ComponentRetriever;
import games.rednblack.editor.renderer.utils.ItemWrapper;

public class Level1Screen extends AbstractGameScreen {

    private SceneLoader sceneLoader;
    private AsyncResourceManager asyncResourceManager;

    private OrthographicCamera camera;
    private Viewport viewport;

    private com.artemis.World artemisWorld;

    @Override
    public void show() {
        super.show();

        camera = new OrthographicCamera();
        viewport = new ExtendViewport(10, 7, camera);

        AssetManager assetManager = getGameManager().assetManager;
        asyncResourceManager = assetManager.get(Resources.HyperLap2D.PROJECT, AsyncResourceManager.class);

        SceneConfiguration config = new SceneConfiguration();
        //config.setRendererSystem();
        config.setResourceRetriever(asyncResourceManager);

        CameraSystem cameraSystem = new CameraSystem(5, 40, 5, 6);

        AnimationSystem animationSystem = new AnimationSystem();
        animationSystem.addAnimation(assetManager.<SimpleAnimation>get(Resources.Animations.CRAB_ANIM_FSM));

        config.addSystem(cameraSystem);
        config.addSystem(animationSystem);

        sceneLoader = new SceneLoader(config);
        artemisWorld = sceneLoader.getEngine();

        // после new SceneLoader(config) должны быть

        ComponentRetriever.addMapper(AnimationComponent.class);
//
//        int hero = artemisWorld.create();
//        EntityEdit heroEdit = artemisWorld.edit(hero).add(new AnimationComponent(GameObjectFactory.createAnimationComponentCrab()));
//        heroEdit.create(TextureRegionComponent.class);
//        ViewPortComponent viewPortComponent = heroEdit.create(ViewPortComponent.class);
//
//        int pixelToWorld = asyncResourceManager.getProjectVO().pixelToWorld;
//        viewPortComponent.viewPort = viewport;
//        viewPortComponent.pixelsPerWU = pixelToWorld;
//
//        heroEdit.create(MainItemComponent.class);
//        heroEdit.create(NodeComponent.class);
//        heroEdit.create(CompositeTransformComponent.class);
//        TransformComponent transformComponent = heroEdit.create(TransformComponent.class);
//        transformComponent.x = 0;
//        transformComponent.y = 0;
//
//        heroEdit.create(DimensionsComponent.class);
//        heroEdit.create(MainItemComponent.class);
//        LayerMapComponent layerMapComponent = heroEdit.create(LayerMapComponent.class);
//        heroEdit.create(TintComponent.class);
//        heroEdit.create(ZIndexComponent.class);
//
//        cameraSystem.setFocus(hero);

        sceneLoader.loadScene("MainScene", viewport);

        ItemWrapper root = new ItemWrapper(sceneLoader.getRoot(), artemisWorld);
        ItemWrapper player = root.getChild("hero");
        int entity = player.getEntity();
        AnimationComponent animationComponent = ComponentRetriever.create(entity, AnimationComponent.class, artemisWorld);
        animationComponent.simpleAnimationComponent = GameObjectFactory.createAnimationComponentCrab();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        viewport.apply();
        artemisWorld.process();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
        if (width != 0 && height != 0)
            sceneLoader.resize(width, height);
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
