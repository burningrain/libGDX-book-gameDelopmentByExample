package com.packt.flappeebee.screen.level.menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.github.br.gdx.simple.structure.GameManager;
import com.github.br.gdx.simple.structure.screen.AbstractGameScreen;
import com.packt.flappeebee.model.GameWorld;
import com.packt.flappeebee.model.GameWorldSettings;

public class MainMenuScreen extends AbstractGameScreen {

    private GameWorld gameWorld;

    @Override
    public void show() {
        GameManager gameManager = getGameManager();

        boolean npotSupported = Gdx.graphics.supportsExtension("GL_OES_texture_npot")
                || Gdx.graphics.supportsExtension("GL_ARB_texture_non_power_of_two");
        Gdx.app.debug("App", "isNpotSupported: " + npotSupported);

        GameWorldSettings gameWorldSettings = new GameWorldSettings();
        gameWorldSettings.virtualWidth = 640;
        gameWorldSettings.virtualHeight = 480;

        gameWorld = new GameWorld(gameWorldSettings);
    }

    @Override
    public void render(float delta) {
        clearScreen();                      // очищаем экран
        // обработка клавиш теперь размазана по коду
        gameWorld.render(delta);             // обновление игрового мира (состояние и рендеринг)
    }

    public void resize(int width, int height) {
        gameWorld.resize(width, height);
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

    public void dispose() {
    }

    private void clearScreen() {
        Gdx.gl.glClearColor(Color.BLACK.r, Color.BLACK.g, Color.BLACK.b, Color.BLACK.a);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    }

}
