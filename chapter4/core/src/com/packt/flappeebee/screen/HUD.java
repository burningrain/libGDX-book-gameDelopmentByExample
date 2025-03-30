package com.packt.flappeebee.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.github.br.gdx.simple.structure.GameSettings;
import com.github.br.gdx.simple.structure.screen.AbstractGameScreen;
import com.packt.flappeebee.Resources;
import com.packt.flappeebee.screen.level.level1.systems.components.ComponentListener;
import com.packt.flappeebee.screen.level.level1.systems.components.HeroComponent;

public class HUD extends AbstractGameScreen implements ComponentListener<HeroComponent> {

    private Stage stage;
    private Table rootTable;
    private Table table;

    private Label pearlsCountLabel;

    private Label textLabel;
    private Label.LabelStyle textLabelStyle;
    private BitmapFont font;

    private Image heroLifeCountImage;

    private int pearlsCount;
    private int lifeCount;

    @Override
    public void show() {
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
        AssetManager assetManager = getGameManager().assetManager;
        TextureAtlas textureAtlas = assetManager.get(Resources.TextureAtlases.PACK);
        TextureAtlas.AtlasRegion pearlAR = textureAtlas.findRegion(Resources.Images.PEARL);

        heroLifeCountImage = new Image(textureAtlas.findRegion(Resources.Images.LIFE_4));
        Image pearlsCountImage = new Image(pearlAR);

        updateFont(getGameManager().gameSettings.getVirtualScreenWidth());
        textLabelStyle = createLabelStyle(font);
        pearlsCountLabel = new Label(0 + "", textLabelStyle);

        Table table = new Table();
        table.columnDefaults(1).width(50);
        table.add(heroLifeCountImage).left().pad(2).colspan(2);
        table.row();
        table.add(pearlsCountImage).left().pad(4);
        table.add(pearlsCountLabel).left().fillX();

        //table.debug();
        table.pack();
        return table;
    }

    @Override
    public void render(float delta) {
        pearlsCountLabel.setText(pearlsCount);
        TextureRegionDrawable drawable = (TextureRegionDrawable) heroLifeCountImage.getDrawable();
        drawable.setRegion(getCrabLifeTexture(lifeCount));

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
        pearlsCount = component.getPearlsCount();
        lifeCount = component.getLifeCount();
    }

    private TextureRegion getCrabLifeTexture(int lifeCount) {
        AssetManager assetManager = getGameManager().assetManager;
        TextureAtlas textureAtlas = assetManager.get(Resources.TextureAtlases.PACK);
        switch (lifeCount) {
            case 1:
                return textureAtlas.findRegion(Resources.Images.LIFE_1);
            case 2:
                return textureAtlas.findRegion(Resources.Images.LIFE_2);
            case 3:
                return textureAtlas.findRegion(Resources.Images.LIFE_3);
            case 4:
                return textureAtlas.findRegion(Resources.Images.LIFE_4);
            default:
                throw new GdxRuntimeException("lifeCount out of range [1;4]. lifeCount=" + lifeCount);
        }
    }

    private void createOrUpdateFontAndLabels(String text, int virtualHeight) {
        updateFont(virtualHeight);
        updateLabelsStyle();
        createOrUpdateLabels(text);
    }

    private void updateFont(int virtualHeight) {
        int size = Gdx.graphics.getHeight() * 24 / virtualHeight;
        font = ScreenUtils.generateBitmapFont(Gdx.files.internal("fonts/unlearn/unlearn2.ttf"), Color.WHITE, size);
    }

    private Label.LabelStyle createLabelStyle(BitmapFont font) {
        Label.LabelStyle result = new Label.LabelStyle();
        result.font = font;
        result.fontColor = Color.WHITE;

        return result;
    }

    private void updateLabelsStyle() {
        if (textLabelStyle == null) {
            textLabelStyle = createLabelStyle(font);
        }
        textLabelStyle.font = font;
    }

    private void createOrUpdateLabels(String text) {
        if (textLabel == null) {
            textLabel = ScreenUtils.createLabel(text, textLabelStyle);
        }
        textLabel.setStyle(textLabelStyle);
    }

}
