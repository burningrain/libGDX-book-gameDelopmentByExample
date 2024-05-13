package com.github.br.paper.airplane.ecs;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.github.br.paper.airplane.GameConstants;
import com.github.br.paper.airplane.ecs.component.TextureComponent;
import com.github.br.paper.airplane.ecs.component.TransformComponent;

public class RenderSystem extends EntitySystem {

    private final Family family = Family.all(TransformComponent.class, TextureComponent.class).get();

    private final ComponentMapper<TransformComponent> transformMapper = ComponentMapper.getFor(TransformComponent.class);
    private final ComponentMapper<TextureComponent> textureMapper = ComponentMapper.getFor(TextureComponent.class);

    private final SpriteBatch spriteBatch = new SpriteBatch();

    private final float worldWidth;
    private final float worldHeight;
    private Viewport viewport;
    private OrthographicCamera camera;

    private boolean isDrawDebugBox2d;
    private World box2dWorld;
    private Box2DDebugRenderer debugRenderer;
    private OrthographicCamera box2dCam;

    public RenderSystem(World box2dWorld, float worldWidth, float worldHeight) {
        this.worldWidth = worldWidth;
        this.worldHeight = worldHeight;

        camera = new OrthographicCamera();
        camera.position.set(worldWidth / 2, worldHeight / 2, 0);
        camera.update();
        viewport = new FitViewport(worldWidth, worldHeight, camera);

        this.box2dWorld = box2dWorld;
        debugRenderer = new Box2DDebugRenderer();
        box2dCam = new OrthographicCamera(GameConstants.UNIT_WIDTH, GameConstants.UNIT_HEIGHT);
    }

    @Override
    public void update(float deltaTime) {
        clearScreen();
        drawEntities();
        if (isDrawDebugBox2d) {
            drawDebugBox2d();
        }
    }

    private void drawDebugBox2d() {
        box2dCam.position.set(GameConstants.UNIT_WIDTH / 2, GameConstants.UNIT_HEIGHT / 2, 0);
        box2dCam.update();
        debugRenderer.render(box2dWorld, box2dCam.combined);
    }

    private void drawEntities() {
        ImmutableArray<Entity> entities = getEngine().getEntitiesFor(family);
        applyCameraToBatch(spriteBatch, camera);
        spriteBatch.begin();
        for (Entity entity : entities) {
            TransformComponent transformComponent = transformMapper.get(entity);
            TextureComponent textureComponent = textureMapper.get(entity);

            spriteBatch.draw(
                    textureComponent.region,
                    transformComponent.x,
                    transformComponent.y,
                    0,
                    0,
                    textureComponent.region.getRegionWidth(),
                    textureComponent.region.getRegionHeight(),
                    1,
                    1,
                    transformComponent.angle
            );
        }
        spriteBatch.end();
    }

    public void resize(int width, int height) {
        viewport.update(width, height);
    }

    public void hide() {

    }

    public void dispose() {
        spriteBatch.dispose();
    }

    public void applyCameraToBatch(Batch batch, Camera camera) {
        batch.setProjectionMatrix(camera.projection);
        batch.setTransformMatrix(camera.view);
    }

    private void clearScreen() {
        Gdx.gl.glClearColor(Color.BLACK.r, Color.BLACK.g, Color.BLACK.b, Color.BLACK.a);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    }

    public boolean isDrawDebugBox2d() {
        return isDrawDebugBox2d;
    }

    public void setDrawDebugBox2d(boolean drawDebugBox2d) {
        isDrawDebugBox2d = drawDebugBox2d;
    }

}
