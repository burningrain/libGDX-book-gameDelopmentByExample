package com.github.br.paper.airplane.screen.menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.github.br.paper.airplane.GameManager;
import com.github.br.paper.airplane.GameSettings;
import com.github.br.paper.airplane.gameworld.Res;
import com.github.br.paper.airplane.gameworld.ResMusic;
import com.github.br.paper.airplane.screen.AbstractGameScreen;
import com.github.br.paper.airplane.screen.GameScreens;
import com.ray3k.stripe.scenecomposer.SceneComposerStageBuilder;

public class MainMenuScreen extends AbstractGameScreen {

    private Skin skin;
    private Stage stage;

    private Music music;

    @Override
    public void show() {
        GameManager gameManager = getGameManager();
        music = gameManager.assetManager.get(ResMusic.STONE_14, Music.class);
        music.setLooping(true);
        music.play();

        GameSettings gameSettings = gameManager.gameSettings;
        Viewport fitViewport = new FitViewport(gameSettings.getVirtualScreenWidth(), gameSettings.getVirtualScreenHeight());
        stage = new Stage(fitViewport);
        skin = gameManager.assetManager.get(Res.SKIN);
        SceneComposerStageBuilder builder = new SceneComposerStageBuilder();
        builder.build(stage, skin, Gdx.files.internal(Res.MAIN_MENU_SCENE));
        Gdx.input.setInputProcessor(stage);

        TextButton textButton = stage.getRoot().findActor("start_button");
        textButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                music.stop();
                gameManager.screenStateManager.changeCurrentState(GameScreens.LEVEL_0);
            }
        });
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.24313726f, 0.20392157f, 0.20392157f, 1.0f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act();
        stage.draw();
    }

    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    public void dispose() {
        stage.dispose();
        skin.dispose();

        music.stop();
    }

    @Override
    public void pause() {
        music.pause();
    }

    @Override
    public void resume() {
        music.play();
    }

    @Override
    public void hide() {
        music.pause();
    }

}
