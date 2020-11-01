package com.github.br.ecs.simple.engine.debug;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.github.br.ecs.simple.engine.DebugSystem;
import com.github.br.ecs.simple.engine.debug.drawobject.DebugDrawObject;
import com.github.br.ecs.simple.utils.ViewHelper;
import com.github.br.ecs.simple.utils.cache.PeriodCacheValueInt;
import com.github.br.ecs.simple.utils.cache.PeriodCacheValueLong;

/**
 * Created by user on 08.01.2019.
 */
public class DebugUI extends ScreenAdapter {

    private DebugService debugService;
    private Array<SystemUiData> uiCache;

    private ShapeRenderer shapeRenderer = new ShapeRenderer();
    private SpriteBatch spriteBatch = new SpriteBatch();

    private Stage stage;
    private Table windowTable = new Table(DebugDrawObject.DEFAULT_SKIN);
    private VerticalGroup stack = new VerticalGroup();
    private ScrollPane scrollPane = new ScrollPane(stack, DebugDrawObject.DEFAULT_SKIN);

    public DebugUI(DebugService debugService) {
        this.debugService = debugService;

        uiCache = new Array<SystemUiData>();
        for (DebugSystem debugSystem : debugService.getSystems()) {
            SystemUiData systemUiData = new SystemUiData(debugSystem);
            uiCache.add(systemUiData);
            stack.addActor(systemUiData.getTable());
        }

        initUI();
    }

    private void initUI() {
        stack.left();

        windowTable.add(scrollPane);
        windowTable.top().left();

        windowTable.setFillParent(true);

        stage = new Stage(new ScreenViewport());
        stage.addActor(windowTable);
        stage.setDebugAll(true);
    }

    @Override
    public void render(float delta) {
        stage.getViewport().apply(true);
        ViewHelper.applyCameraAndViewPort(shapeRenderer, stage.getViewport());
        ViewHelper.applyCameraAndViewPort(spriteBatch, stage.getViewport());
        ViewHelper.applyCameraAndViewPort(stage.getBatch(), stage.getViewport());

        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        spriteBatch.begin();
        updateData(delta);
        shapeRenderer.end();
        spriteBatch.end();
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().apply(true);
        stage.getViewport().update(width, height);
    }

    private void updateData(float delta) {
        Array<DebugSystem> systems = debugService.getSystems();
        for (int i = 0; i < systems.size; i++) {
            SystemUiData systemUiData = uiCache.get(i);
            systemUiData.updateData(systems.get(i), delta);
        }
    }

    public InputProcessor getInputProcessor() {
        return stage;
    }


    public static class SystemUiData {

        private Class systemClazz;
        private PeriodCacheValueLong executionTime = new PeriodCacheValueLong(0.4f, new PeriodCacheValueLong.AvgForPeriod());
        private PeriodCacheValueInt nodesAmount = new PeriodCacheValueInt(0.4f, new PeriodCacheValueInt.AvgForPeriod());

        private Label sysNameLabel;
        private Label execTimeLabel;
        private Label nodesAmountLabel;
        private Table table;

        public SystemUiData(DebugSystem system) {
            this.systemClazz = system.getClass();
            this.executionTime.setValue((int) system.getExecutionTime());
            this.nodesAmount.setValue(system.getNodesAmount());

            sysNameLabel = new Label(system.getClass().getSimpleName(), DebugDrawObject.DEFAULT_SKIN);
            sysNameLabel.setColor(Color.GOLD);

            execTimeLabel = new Label(String.valueOf(system.getExecutionTime()), DebugDrawObject.DEFAULT_SKIN);
            nodesAmountLabel = new Label(String.valueOf(system.getNodesAmount()),DebugDrawObject.DEFAULT_SKIN);

            table = new Table(DebugDrawObject.DEFAULT_SKIN);
            table.add(sysNameLabel);
            table.add(nodesAmountLabel);
            table.add(execTimeLabel);
            table.left();
        }

        public void updateData(DebugSystem system, float delta) {
            nodesAmount.update(system.getNodesAmount(), delta);
            executionTime.update(system.getExecutionTime(), delta);

            nodesAmountLabel.setText("e: " + String.valueOf(nodesAmount.getValue()));
            execTimeLabel.setText("t: " + String.valueOf(executionTime.getValue() >>> 10)); // делим на 1024, почти мкс
        }

        public Table getTable() {
            return table;
        }
    }

}
