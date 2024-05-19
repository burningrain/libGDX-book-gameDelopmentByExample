package com.github.br.paper.airplane.level.level0;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.github.br.paper.airplane.GameManager;
import com.github.br.paper.airplane.GameSettings;
import com.github.br.paper.airplane.ecs.InputSystem;
import com.github.br.paper.airplane.ecs.PhysicsSystem;
import com.github.br.paper.airplane.ecs.RenderSystem;
import com.github.br.paper.airplane.gameworld.GameEntityFactory;
import com.github.br.paper.airplane.screen.AbstractGameScreen;

public class Level0Screen extends AbstractGameScreen {

    private Engine engine;
    private RenderSystem renderSystem;

    @Override
    public void show() {
        GameManager gameManager = this.getGameManager();
        GameSettings gameSettings = gameManager.gameSettings;

        World world = new World(new Vector2(0, -10F), true);
        engine = new Engine();
        renderSystem = new RenderSystem(world, gameSettings);
        renderSystem.setDrawDebugBox2d(true);

        engine.addSystem(new InputSystem());
        engine.addSystem(new PhysicsSystem(world, gameManager.utils));
        engine.addSystem(renderSystem);

        // FIXME убрать после, пока для теста
        GameEntityFactory gameEntityFactory = getGameManager().gameEntityFactory;
        Entity badLogicLogo = gameEntityFactory.createBadLogicLogo(engine);
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
