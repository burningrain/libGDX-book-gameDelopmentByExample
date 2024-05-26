package com.github.br.paper.airplane.level.level0;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.github.br.paper.airplane.GameManager;
import com.github.br.paper.airplane.GameSettings;
import com.github.br.paper.airplane.ecs.system.*;
import com.github.br.paper.airplane.gameworld.GameEntityFactory;
import com.github.br.paper.airplane.screen.AbstractGameScreen;

public class Level0Screen extends AbstractGameScreen {

    private Engine engine;
    private RenderSystem renderSystem;

    @Override
    public void show() {
        GameManager gameManager = this.getGameManager();
        GameSettings gameSettings = gameManager.gameSettings;
        GameEntityFactory gameEntityFactory = getGameManager().gameEntityFactory;

        engine = new Engine();
        PhysicsSystem physicsSystem = new PhysicsSystem(gameSettings, gameManager.utils).setDrawDebugBox2d(true);
        engine.addSystem(new DeleteSystem());
        engine.addSystem(new InputSystem());
        engine.addSystem(new ScriptSystem());
        engine.addSystem(new WallGeneratorSystem(gameManager.gameSettings, gameEntityFactory));
        engine.addSystem(new WallSystem());
        engine.addSystem(physicsSystem);
        engine.addSystem(renderSystem = new RenderSystem(gameSettings, new Runnable() {
            @Override
            public void run() {
                if (physicsSystem.isDrawDebugBox2d()) {
                    physicsSystem.drawDebugBox2d();
                }
            }
        }));

        Entity ceil = gameEntityFactory.createCeil(engine);
        engine.addEntity(ceil);

        Entity badLogicLogo = gameEntityFactory.createBadLogicLogo(engine);
        engine.addEntity(badLogicLogo);

        // FIXME убрать после, пока для теста
        Entity wall = gameEntityFactory.createWall(
                engine,
                gameSettings.getVirtualScreenWidth() - 200,
                0,
                64,
                256,
                0
        );
        engine.addEntity(wall);
    }

    @Override
    public void render(float delta) {
        engine.update(delta);
    }

    @Override
    public void resize(int width, int height) {
        renderSystem.resize(width, height);
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void hide() {
        renderSystem.hide();
    }

    @Override
    public void dispose() {
        renderSystem.dispose();
    }

}
