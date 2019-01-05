package com.github.br.ecs.simple.engine.debug;


import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.github.br.ecs.simple.engine.IDebugSystem;
import com.github.br.ecs.simple.engine.IEcsSystem;
import com.github.br.ecs.simple.engine.debug.data.DebugData;
import com.github.br.ecs.simple.engine.debug.drawobject.DebugDrawObject;
import com.github.br.ecs.simple.utils.ViewHelper;

import java.util.LinkedHashMap;
import java.util.Map;


/**
 * Сервис, выводящий на экран отладочную информацию.
 */
public class DebugService {

    private LinkedHashMap<Class<? extends IDebugSystem>, IDebugSystem> systems =
            new LinkedHashMap<Class<? extends IDebugSystem>, IDebugSystem>();

    private ShapeRenderer shapeRenderer = new ShapeRenderer();
    private SpriteBatch spriteBatch = new SpriteBatch();

    private Stage stage = new Stage();
    private Table windowTable = new Table(DebugDrawObject.DEFAULT_SKIN);
    private VerticalGroup stack = new VerticalGroup();
    private ScrollPane scrollPane = new ScrollPane(stack, DebugDrawObject.DEFAULT_SKIN);
    private LibGdxPanel libGdxPanel = new LibGdxPanel() {
        @Override
        public void add(Actor actor) {
            stack.addActor(actor);
        }
    };

    public DebugService() {
        windowTable.setFillParent(true);
        windowTable.add(scrollPane).width(200f).fill();
        stage.setDebugAll(true);
        stage.addActor(windowTable);
    }

    public void addSystem(IDebugSystem system) {
        systems.put(system.getClass(), system);
    }

    public void update(float delta) {
        ViewHelper.applyCameraAndViewPort(shapeRenderer);
        ViewHelper.applyCameraAndViewPort(spriteBatch);
        ViewHelper.applyCameraAndViewPort(stage.getBatch());

        stack.clear();
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        spriteBatch.begin();
        drawDebugData(shapeRenderer, spriteBatch, libGdxPanel);
        shapeRenderer.end();
        spriteBatch.end();
        stage.draw();
    }

    private void drawDebugData(final ShapeRenderer shapeRenderer, final SpriteBatch spriteBatch, final LibGdxPanel libGdxPanel) {
        for (Map.Entry<Class<? extends IDebugSystem>, IDebugSystem> entry : systems.entrySet()) {
            Class<? extends IEcsSystem> systemClazz = entry.getKey();
            if(isNeedDrawSystem(systemClazz)) {
                drawSystemName(systemClazz, libGdxPanel);

                DebugDataContainer debugDataContainer = entry.getValue().getDebugData();
                debugDataContainer.forEach(new DebugDataContainer.Callback() {
                    @Override
                    public void call(DebugData debugData) {
                        DebugRendererObjectFactory.getDebugDrawObject(debugData.getClass())
                                .draw(shapeRenderer, spriteBatch, libGdxPanel, debugData);
                    }
                });
            }
        }
    }

    private static void drawSystemName(Class<? extends IEcsSystem> systemClazz, LibGdxPanel libGdxPanel) {
        Label label = new Label(systemClazz.getSimpleName(), DebugDrawObject.DEFAULT_SKIN);
        label.setColor(Color.GOLD);
        libGdxPanel.add(label);
    }

    private boolean isNeedDrawSystem(Class<? extends IEcsSystem> key) {
        return systems.get(key).isDebugMode();
    }

    public InputProcessor getInputProcessor() {
        return stage;
    }
}
