package com.github.br.paper.airplane.screen;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.github.br.paper.airplane.GameSettings;
import com.github.br.paper.airplane.ecs.component.ComponentListener;
import com.github.br.paper.airplane.ecs.component.HeroComponent;
import com.github.br.paper.airplane.gameworld.Res;

public class HUD extends AbstractGameScreen implements ComponentListener<HeroComponent> {

    private Stage stage;
    private Table rootTable;
    private Table table;

    private Skin skin;

    private Label heroLifeCountLabel;
    private Label bulletsCountLabel;

    @Override
    public void show() {
        skin = getGameManager().assetManager.get(Res.Skins.SKIN);

        GameSettings gameSettings = getGameManager().gameSettings;
        stage = new Stage(new ExtendViewport(gameSettings.getVirtualScreenWidth(), gameSettings.getVirtualScreenHeight()));
        rootTable = createRootTable();

        // Add widgets to the table here.
        table = createTable();
        rootTable.add(table).top().left().padLeft(16).padTop(16).expand();

        //rootTable.setDebug(true); // This is optional, but enables debug lines for tables.
        rootTable.pack();

        stage.addActor(rootTable);
    }

    private Table createRootTable() {
        rootTable = new Table();
        rootTable.setFillParent(true);

        return rootTable;
    }

    private Table createTable() {
        Image heroLifeCountImage = new Image((Texture) getGameManager().assetManager.get(Res.HUD.HERO_LIFE_COUNT_PNG));
        Image bulletsCountImage = new Image((Texture) getGameManager().assetManager.get(Res.HUD.BULLETS_COUNT_PNG));

        heroLifeCountLabel = new Label(0 + "", skin);
        bulletsCountLabel = new Label(0 + "", skin);

        Table table = new Table();
        table.add(heroLifeCountImage).space(12);
        table.add(heroLifeCountLabel);
        table.row();
        table.add(bulletsCountImage).space(12);
        table.add(bulletsCountLabel);

        //table.debug();
        table.pack();
        return table;
    }

    @Override
    public void render(float delta) {
        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
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
        stage.dispose();
    }

    @Override
    public void update(HeroComponent component) {
        heroLifeCountLabel.setText(component.getLifeCount() + "");
        bulletsCountLabel.setText(component.getBulletCount());
    }

}
