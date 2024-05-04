package com.github.br.libgdx.games.nutty.birds.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.github.br.libgdx.games.nutty.birds.*;

import java.util.function.Consumer;

public class GameScreen implements Screen {

    private final NuttyBirdsGame nuttyGame;

    private static final float WORLD_WIDTH = 960;
    private static final float WORLD_HEIGHT = 544;

    private World world;
    private Box2DDebugRenderer debugRenderer;
    private OrthographicCamera box2dCam;

    private OrthographicCamera camera;
    private Viewport viewport;

    private SpriteBatch batch;
    private TiledMap tiledMap;
    private OrthogonalTiledMapRenderer orthogonalTiledMapRenderer;

    private Array<Body> toRemove = new Array<>();

    // fire
    private final Vector2 anchor = new Vector2(Utils.convertMetresToUnits(6.125f), Utils.convertMetresToUnits(5.75f));
    private final Vector2 firingPosition = anchor.cpy();
    private float distance;
    private float angle;

    private ShapeRenderer shapeRenderer;


    private ObjectMap<Body, Pair> sprites = new ObjectMap<>(); // в книге используется как хранило пар, а не мапа.
    private Sprite slingshot;
    private Sprite squirrel;
    private Sprite staticAcorn;

    public GameScreen(NuttyBirdsGame nuttyGame) {
        this.nuttyGame = nuttyGame;
    }

    @Override
    public void show() {
        world = new World(new Vector2(0, -10F), true);
        shapeRenderer = new ShapeRenderer();
        debugRenderer = new Box2DDebugRenderer();
        box2dCam = new OrthographicCamera(Constants.UNIT_WIDTH, Constants.UNIT_HEIGHT);

        camera = new OrthographicCamera();
        viewport = new FitViewport(WORLD_WIDTH, WORLD_HEIGHT, camera);
        viewport.apply(true);

        tiledMap = nuttyGame.getAssetManager().get(Constants.NUTTYBIRDS_TMX);
        batch = new SpriteBatch();
        orthogonalTiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap, batch);
        orthogonalTiledMapRenderer.setView(camera);

        TiledObjectBodyBuilder.buildFloorBodies(tiledMap, world);
        TiledObjectBodyBuilder.buildBirdBodies(tiledMap, world);
        TiledObjectBodyBuilder.buildBuildingBodies(tiledMap, world);

        world.setContactListener(new NuttyContactListener(this));

        Gdx.input.setInputProcessor(new InputAdapter() {
            @Override
            public boolean touchDragged(int screenX, int screenY, int pointer) {
                calculateAngleAndDistanceForBullet(screenX, screenY);
                return true;
            }

            @Override
            public boolean touchUp(int screenX, int screenY, int pointer, int button) {
                TiledObjectBodyBuilder.createBullet(world, firingPosition, angle, distance, new Consumer<Body>() {
                    @Override
                    public void accept(Body bullet) {
                        Sprite sprite = new Sprite(nuttyGame.getAssetManager().get(Constants.ACORN_PNG, Texture.class));
                        sprite.setOrigin(sprite.getWidth() / 2, sprite.getHeight() / 2);

                        sprites.put(bullet, new Pair(sprite, bullet));
                    }
                });
                firingPosition.set(anchor.cpy());
                return true;
            }
        });

        Array<Body> bodies = new Array<>();
        world.getBodies(bodies);
        for (Body body : bodies) {
            String userData = (String) body.getUserData();
            Sprite sprite = SpriteGenerator.generateSpriteForBody(nuttyGame.getAssetManager(), userData);
            if (sprite != null) {
                sprites.put(body, new Pair(sprite, body));
            }
        }
        slingshot = new Sprite(nuttyGame.getAssetManager().get(Constants.SLINGSHOT_PNG, Texture.class));
        slingshot.setPosition(170, 64);
        squirrel = new Sprite(nuttyGame.getAssetManager().get(Constants.SQUIRREL_PNG, Texture.class));
        squirrel.setPosition(32, 64);
        staticAcorn = new Sprite(nuttyGame.getAssetManager().get(Constants.ACORN_PNG, Texture.class));
    }

    private void update(float delta) {
        clearDeadBodies();
        world.step(1 / 60f, 6, 2);
    }

    @Override
    public void render(float delta) {
        update(delta);
        clearScreen();
        draw();
        //drawDebug();
    }

    private void clearScreen() {
        Gdx.gl.glClearColor(Color.BLACK.r, Color.BLACK.g, Color.BLACK.b, Color.BLACK.a);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    }

    private void draw() {
        batch.setProjectionMatrix(camera.projection);
        batch.setTransformMatrix(camera.view);
        orthogonalTiledMapRenderer.render();

        updateSpritePositions();
        batch.begin();
        for (Pair pair : sprites.values()) {
            pair.sprite.draw(batch);
        }
        squirrel.draw(batch);
        staticAcorn.draw(batch);
        slingshot.draw(batch);
        batch.end();
    }

    private void updateSpritePositions() {
        for (Body body : sprites.keys()) {
            Sprite sprite = sprites.get(body).sprite;
            sprite.setPosition(
                    Utils.convertMetresToUnits(body.getPosition().x) - sprite.getWidth() / 2f,
                    Utils.convertMetresToUnits(body.getPosition().y) - sprite.getHeight() / 2f
            );
            sprite.setRotation(MathUtils.radiansToDegrees * body.getAngle());
        }
        staticAcorn.setPosition(
                firingPosition.x - staticAcorn.getWidth() / 2f,
                firingPosition.y - staticAcorn.getHeight() / 2f
        );
    }

    private void drawDebug() {
        box2dCam.position.set(Constants.UNIT_WIDTH / 2, Constants.UNIT_HEIGHT / 2, 0);
        box2dCam.update();
        debugRenderer.render(world, box2dCam.combined);

        shapeRenderer.setProjectionMatrix(camera.projection);
        shapeRenderer.setTransformMatrix(camera.view);

        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.rect(anchor.x - 5, anchor.y - 5, 10, 10);
        shapeRenderer.rect(firingPosition.x - 5, firingPosition.y - 5, 10, 10);
        shapeRenderer.line(anchor.x, anchor.y, firingPosition.x, firingPosition.y);
        shapeRenderer.end();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
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

    public void remove(Body body) {
        toRemove.add(body);
    }

    private void clearDeadBodies() {
        for (Body body : toRemove) {
            world.destroyBody(body);
            sprites.remove(body);
        }
        toRemove.clear();
    }

    private float angleBetweenTwoPoints(Vector2 anchor, Vector2 firingPosition) {
        float angle = MathUtils.atan2(anchor.y - firingPosition.y, anchor.x - firingPosition.x);
        angle %= 2 * MathUtils.PI;
        if (angle < 0) {
            angle += 2 * MathUtils.PI2;
        }

        return angle;
    }

    private float distanceBetweenTwoPoints(Vector2 anchor, Vector2 firingPosition) {
        return (float) Math.sqrt((
                (anchor.x - firingPosition.x) * (anchor.x - firingPosition.x)) +
                ((anchor.y - firingPosition.y) * (anchor.y - firingPosition.y))
        );
    }

    private void calculateAngleAndDistanceForBullet(int screenX, int screenY) {
        firingPosition.set(screenX, screenY);
        viewport.unproject(firingPosition);
        distance = distanceBetweenTwoPoints(anchor, firingPosition);
        angle = angleBetweenTwoPoints(anchor, firingPosition);
        if (distance > Constants.MAX_DISTANCE) {
            distance = Constants.MAX_DISTANCE;
        }
        if (angle > Constants.LOWER_ANGLE) {
            if (angle > Constants.UPPER_ANGLE) {
                angle = 0;
            } else {
                angle = Constants.LOWER_ANGLE;
            }
        }
        firingPosition.set(anchor.x + (distance * -MathUtils.cos(angle)), anchor.y + (distance * -MathUtils.sin(angle)));
    }

}
