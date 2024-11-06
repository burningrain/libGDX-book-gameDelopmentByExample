package com.github.br.paper.airplane.level.level0;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.github.br.paper.airplane.GameManager;
import com.github.br.paper.airplane.GameSettings;
import com.github.br.paper.airplane.ecs.component.Mappers;
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

        Mappers mappers = new Mappers();
        engine = new Engine();
        PhysicsSystem physicsSystem = new PhysicsSystem(gameSettings, gameManager.utils, mappers).setDrawDebugBox2d(true);

        engine.addSystem(new HeroSystem(mappers));
        engine.addSystem(new DestroySystem());
        engine.addSystem(new InputSystem(mappers, gameEntityFactory));
        engine.addSystem(new ScriptSystem(mappers));
        engine.addSystem(new WallGeneratorSystem(gameManager.gameSettings, gameEntityFactory));
        engine.addSystem(new CoinGeneratorSystem(gameManager.gameSettings, gameEntityFactory));
        engine.addSystem(new InitSystem(mappers));
        engine.addSystem(new DeleteSystem(mappers));
        engine.addSystem(physicsSystem);
        engine.addSystem(renderSystem = new RenderSystem(mappers, gameSettings, new Runnable() {
            @Override
            public void run() {
                if (physicsSystem.isDrawDebugBox2d()) {
                    physicsSystem.drawDebugBox2d();
                }
            }
        }));

        Entity ceil = gameEntityFactory.createCeil(engine);
        engine.addEntity(ceil);

        Entity badLogicLogo = gameEntityFactory.createHero(engine);
        engine.addEntity(badLogicLogo);

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
