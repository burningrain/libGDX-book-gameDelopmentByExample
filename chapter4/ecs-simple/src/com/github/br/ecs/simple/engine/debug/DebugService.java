package com.github.br.ecs.simple.engine.debug;


import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.github.br.ecs.simple.engine.IEcsSystem;
import com.github.br.ecs.simple.engine.debug.data.DebugData;
import com.github.br.ecs.simple.engine.debug.drawobject.DebugDrawObject;
import com.github.br.ecs.simple.utils.ViewHelper;

import java.util.LinkedHashMap;
import java.util.Map;


/**
 * Сервис, выводящий на экран дебажную информацию.
 */
public class DebugService {

    private LinkedHashMap<Class<? extends IEcsSystem>, IEcsSystem> systems =
            new LinkedHashMap<Class<? extends IEcsSystem>, IEcsSystem>();

    private ShapeRenderer shapeRenderer = new ShapeRenderer();
    private SpriteBatch spriteBatch = new SpriteBatch();

    private Stage stage = new Stage();
    private Table table = new Table(); //todo добавить скролл

    private LibGdxPanel libGdxPanel = new LibGdxPanel() {
        @Override
        public void add(Actor actor) {
            table.add(actor);
        }
    };

    public DebugService() {
        table.setFillParent(true);
        table.top().left();
        table.setDebug(true);
        stage.addActor(table);
    }

    public void addSystem(IEcsSystem system) {
        systems.put(system.getClass(), system);
    }

    public void update(float delta) {
        ViewHelper.applyCameraAndViewPort(shapeRenderer);
        ViewHelper.applyCameraAndViewPort(spriteBatch);

        table.clear();
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        spriteBatch.begin();
        drawDebugData(shapeRenderer, spriteBatch, libGdxPanel);
        shapeRenderer.end();
        spriteBatch.end();
        stage.draw();
    }

    private void drawDebugData(final ShapeRenderer shapeRenderer, final SpriteBatch spriteBatch, final LibGdxPanel libGdxPanel) {
        for (Map.Entry<Class<? extends IEcsSystem>, IEcsSystem> entry : systems.entrySet()) {
            if(isNeedDrawSystem(entry.getKey())) {
                DebugDataContainer debugDataContainer = entry.getValue().getDebugData();
                debugDataContainer.forEach(new DebugDataContainer.Callback() {
                    @Override
                    public void call(DebugData debugData) {
                        DebugRendererObjectFactory.getDebugDrawObject(debugData.getClass())
                                .draw(shapeRenderer, spriteBatch, libGdxPanel, debugData);
                        table.row(); //todo с таблицами кривота, разобраться
                    }
                });
            }
        }
    }

    private boolean isNeedDrawSystem(Class<? extends IEcsSystem> key) {
        return systems.get(key).isDebugMode();
    }

}
