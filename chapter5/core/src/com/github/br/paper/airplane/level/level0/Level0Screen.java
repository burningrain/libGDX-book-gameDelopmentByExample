package com.github.br.paper.airplane.level.level0;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.github.br.paper.airplane.GameManager;
import com.github.br.paper.airplane.GameSettings;
import com.github.br.paper.airplane.bullet.BulletType;
import com.github.br.paper.airplane.ecs.component.HeroComponent;
import com.github.br.paper.airplane.ecs.component.Mappers;
import com.github.br.paper.airplane.ecs.system.*;
import com.github.br.paper.airplane.ecs.system.physics.PhysicsSystem;
import com.github.br.paper.airplane.gameworld.GameEntityFactory;
import com.github.br.paper.airplane.gameworld.ResMusic;
import com.github.br.paper.airplane.screen.AbstractGameScreen;
import com.github.br.paper.airplane.screen.HUD;

public class Level0Screen extends AbstractGameScreen {

    private Engine engine;
    private RenderSystem renderSystem;

    private Music music;

    private HUD hud;

    @Override
    public void show() {
        GameManager gameManager = this.getGameManager();
        GameSettings gameSettings = gameManager.gameSettings;
        GameEntityFactory gameEntityFactory = getGameManager().gameEntityFactory;

        Mappers mappers = new Mappers();
        engine = new Engine();
        PhysicsSystem physicsSystem = new PhysicsSystem(gameSettings, gameManager.utils, mappers).setDrawDebugBox2d(true);

        //engine.addSystem(new HeroSystem(mappers));
        engine.addSystem(new InputSystem(mappers, gameManager));
        engine.addSystem(new ScriptSystem(mappers));
        engine.addSystem(new WallGeneratorSystem(gameManager.gameSettings, gameEntityFactory));
        engine.addSystem(new CoinGeneratorSystem(gameManager.gameSettings, gameEntityFactory));
        engine.addSystem(new BulletTypeGeneratorSystem(gameManager.gameSettings, gameEntityFactory));
        engine.addSystem(new InitSystem(mappers));
        engine.addSystem(new DeleteSystem(mappers));
        engine.addSystem(physicsSystem);
        engine.addSystem(renderSystem = new RenderSystem(gameManager.utils, mappers, gameSettings, new Runnable() {
            @Override
            public void run() {
                if (physicsSystem.isDrawDebugBox2d()) {
                    physicsSystem.drawDebugBox2d();
                }
            }
        }));
        engine.addSystem(new DestroySystem(mappers, physicsSystem.getPhysicsUtils()));

        Entity ceil = gameEntityFactory.createCeil(engine);
        engine.addEntity(ceil);

        Entity hero = gameEntityFactory.createHero(engine);
        engine.addEntity(hero);

        music = gameManager.assetManager.get(ResMusic.STONE_INSTRUMENTS, Music.class);
        music.setLooping(true);
        music.play();

        hud = new HUD();
        hud.setGameManager(getGameManager());
        hud.show();

        HeroComponent component = hero.getComponent(HeroComponent.class);
        component.setLifeCount(gameSettings.getGamePlaySettings().getHeroLifeCountMax());
        component.setBulletCount(gameSettings.getGamePlaySettings().getBulletInitCount());
        component.setBulletType(BulletType.VENOM);

        component.addListener(hud);
        component.notifyListeners();
    }

    @Override
    public void render(float delta) {
        clearScreen();
        engine.update(delta);
        hud.render(delta);
    }

    @Override
    public void resize(int width, int height) {
        renderSystem.resize(width, height);
        hud.resize(width, height);
    }

    @Override
    public void pause() {
        hud.pause();
        music.pause();
    }

    @Override
    public void resume() {
        hud.resume();
        music.play();
    }

    @Override
    public void hide() {
        renderSystem.hide();
        hud.hide();
        music.pause();
    }

    @Override
    public void dispose() {
        renderSystem.dispose();
        hud.dispose();
        music.stop();
    }

    private void clearScreen() {
        Gdx.gl.glClearColor(Color.BLACK.r, Color.BLACK.g, Color.BLACK.b, Color.BLACK.a);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    }

}
