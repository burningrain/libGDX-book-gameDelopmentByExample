package com.github.br.platformer.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.github.br.platformer.*;

import java.util.Iterator;

public class GameScreen extends ScreenAdapter {


    private ShapeRenderer shapeRenderer;
    private Viewport viewport;
    private OrthographicCamera camera;
    private SpriteBatch batch;
    private final PeteGame peteGame;

    private TiledMap tiledMap;
    private OrthogonalTiledMapRenderer orthogonalTiledMapRenderer;

    private Pete pete;

    private Array<Acorn> acorns = new Array<Acorn>();

    public GameScreen(PeteGame peteGame) {
        this.peteGame = peteGame;
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
    }

    @Override
    public void show() {
        camera = new OrthographicCamera();
        camera.position.set(Constants.WORLD_WIDTH / 2, Constants.WORLD_HEIGHT / 2, 0);
        camera.update();

        viewport = new FitViewport(Constants.WORLD_WIDTH, Constants.WORLD_HEIGHT, camera);
        viewport.apply(true);
        shapeRenderer = new ShapeRenderer();
        batch = new SpriteBatch();

        tiledMap = peteGame.getAssetManager().get(Constants.PETE_TMX);
        orthogonalTiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap, batch);
        orthogonalTiledMapRenderer.setView(camera);

        Texture peteTexture = peteGame.getAssetManager().get(Constants.PETE_TEXTURE);
        Sound jumpSound = peteGame.getAssetManager().get(Constants.AUDIO_JUMP, Sound.class);
        pete = new Pete(peteTexture, jumpSound);
        pete.setPosition(0, Constants.WORLD_HEIGHT / 2);
        populateAcorns();

        peteGame.getAssetManager().get(Constants.AUDIO_THEME, Music.class).setLooping(true);
        peteGame.getAssetManager().get(Constants.AUDIO_THEME, Music.class).play();
    }

    @Override
    public void render(float delta) {
        update(delta);
        clearScreen();
        draw();
        drawDebug();
    }

    private void update(float delta) {
        updateCameraX();
        pete.update(delta);
        stopPeteLeavingTheScreen();
        handlePeteCollision();
        handlePeteCollisionWithAcorn();
    }

    private void clearScreen() {
        Gdx.gl.glClearColor(Color.BLACK.r, Color.BLACK.g, Color.BLACK.b, Color.BLACK.a);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    }

    private void draw() {
        batch.setProjectionMatrix(camera.projection);
        batch.setTransformMatrix(camera.view);
        orthogonalTiledMapRenderer.render();

        batch.begin();
        drawAcornList(batch);
        pete.draw(batch);
        batch.end();

    }

    private void drawAcornList(SpriteBatch batch) {
        for (Acorn acorn : acorns) {
            acorn.draw(batch);
        }
    }

    private void drawDebug() {
        shapeRenderer.setProjectionMatrix(camera.projection);
        shapeRenderer.setTransformMatrix(camera.view);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        //pete.drawDebug(shapeRenderer);
        shapeRenderer.end();
    }

    private void stopPeteLeavingTheScreen() {
        if (pete.getY() < 0) {
            pete.setPosition(pete.getX(), 0);
            pete.landed();
        }
        if (pete.getX() < 0) {
            pete.setPosition(0, pete.getY());
        }
        TiledMapTileLayer tiledMapTileLayer = (TiledMapTileLayer) tiledMap.getLayers().get(Constants.LAYER_PLATFORMS);
        float levelWidth = tiledMapTileLayer.getWidth() * tiledMapTileLayer.getTileWidth();
        if (pete.getX() + Pete.WIDTH > levelWidth) {
            pete.setPosition(levelWidth - Pete.WIDTH, pete.getY());
        }
    }

    private Array<CollisionCell> whichCellsDoesPeteCover() {
        float x = pete.getX();
        float y = pete.getY();
        Array<CollisionCell> cellsCovered = new Array<CollisionCell>(4);
        float cellX = x / Constants.CELL_SIZE;
        float cellY = y / Constants.CELL_SIZE;
        int bottomLeftCellX = MathUtils.floor(cellX);
        int bottomLeftCellY = MathUtils.floor(cellY);
        TiledMapTileLayer tiledMapTileLayer = (TiledMapTileLayer) tiledMap.getLayers().get(Constants.LAYER_PLATFORMS);
        cellsCovered.add(
                new CollisionCell(
                        tiledMapTileLayer.getCell(bottomLeftCellX, bottomLeftCellY),
                        bottomLeftCellX,
                        bottomLeftCellY
                )
        );
        if (cellX % 1 != 0 && cellY % 1 != 0) {
            int topRightCellX = bottomLeftCellX + 1;
            int topRightCellY = bottomLeftCellY + 1;
            cellsCovered.add(
                    new CollisionCell(tiledMapTileLayer.getCell(topRightCellX, topRightCellY),
                            topRightCellX,
                            topRightCellY
                    )
            );
        }
        if (cellX % 1 != 0) {
            int bottomRightCellX = bottomLeftCellX + 1;
            int bottomRightCellY = bottomLeftCellY;
            cellsCovered.add(
                    new CollisionCell(tiledMapTileLayer.getCell(bottomRightCellX, bottomRightCellY),
                            bottomRightCellX,
                            bottomRightCellY)
            );
        }
        if (cellY % 1 != 0) {
            int topLeftCellX = bottomLeftCellX;
            int topLeftCellY = bottomLeftCellY + 1;
            cellsCovered.add(
                    new CollisionCell(tiledMapTileLayer.getCell(topLeftCellX, topLeftCellY),
                            topLeftCellX,
                            topLeftCellY)
            );
        }
        return cellsCovered;
    }

    private Array<CollisionCell> filterOutNonTiledCells(Array<CollisionCell> cells) {
        for (Iterator<CollisionCell> iterator = cells.iterator(); iterator.hasNext(); ) {
            CollisionCell collisionCell = iterator.next();
            if (collisionCell.isEmpty()) {
                iterator.remove();
            }
        }

        return cells;
    }

    private void handlePeteCollision() {
        Array<CollisionCell> peteCells = whichCellsDoesPeteCover();
        peteCells = filterOutNonTiledCells(peteCells);
        for (CollisionCell cell : peteCells) {
            float cellLevelX = cell.cellX * Constants.CELL_SIZE;
            float cellLevelY = cell.cellY * Constants.CELL_SIZE;
            Rectangle intersection = new Rectangle();
            Intersector.intersectRectangles(
                    pete.getCollisionRectangle(),
                    new Rectangle(cellLevelX, cellLevelY, Constants.CELL_SIZE, Constants.CELL_SIZE),
                    intersection
            );
            if (intersection.getHeight() < intersection.getWidth()) {
                pete.setPosition(pete.getX(), intersection.getY() + intersection.getHeight());
                pete.landed();
            } else if (intersection.getWidth() < intersection.getHeight()) {
                if (intersection.getX() == pete.getX()) {
                    pete.setPosition(intersection.getX() + intersection.getWidth(), pete.getY());
                }
                if (intersection.getX() > pete.getX()) {
                    pete.setPosition(intersection.getX() - Pete.WIDTH, pete.getY());
                }
            }
        }
    }

    private void populateAcorns() {
        MapLayer mapLayer = tiledMap.getLayers().get(Constants.LAYER_COLLECTABLES);
        for (MapObject mapObject : mapLayer.getObjects()) {
            acorns.add(
                    new Acorn(
                            peteGame.getAssetManager().get(Constants.ACORN_TEXTURE, Texture.class),
                            mapObject.getProperties().get("x", Float.class),
                            mapObject.getProperties().get("y", Float.class)
                    )
            );
        }
    }

    private void handlePeteCollisionWithAcorn() {
        for (Iterator<Acorn> iter = acorns.iterator(); iter.hasNext(); ) {
            Acorn acorn = iter.next();
            if (pete.getCollisionRectangle().overlaps(acorn.getCollisionRectangle())) {
                peteGame.getAssetManager().get(Constants.AUDIO_COLLECT, Sound.class).play();
                iter.remove();
            }
        }
    }

    private void updateCameraX() {
        TiledMapTileLayer tiledMapTileLayer = (TiledMapTileLayer) tiledMap.getLayers().get(Constants.LAYER_PLATFORMS);
        float levelWidth = tiledMapTileLayer.getWidth() * tiledMapTileLayer.getTileWidth();
        if ((pete.getX() > Constants.WORLD_WIDTH / 2f) && (pete.getX() < (levelWidth - Constants.WORLD_WIDTH / 2f))) {
            camera.position.set(pete.getX(), camera.position.y, camera.position.z);
            camera.update();
            orthogonalTiledMapRenderer.setView(camera);
        }

    }

}
