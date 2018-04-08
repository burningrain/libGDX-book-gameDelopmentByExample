package com.github.br.ecs.simple.engine.debug.drawobject;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.github.br.ecs.simple.engine.debug.LibGdxPanel;
import com.github.br.ecs.simple.engine.debug.data.TableData;

/**
 * Created by user on 07.04.2018.
 */
public class TableDrawObject implements DebugDrawObject<TableData> {

    @Override
    public void draw(ShapeRenderer shapeRenderer, SpriteBatch spriteBatch, LibGdxPanel panel, TableData data) {
//        Table table = new Table();
//        table.add(new Label(data.getKey(), DebugDrawObject.DEFAULT_SKIN));
//        table.add(new Label(data.getValue(), DebugDrawObject.DEFAULT_SKIN)).width(100);
//        panel.add(table);

        panel.add(new Label(data.getKey(), DebugDrawObject.DEFAULT_SKIN));
        panel.add(new Label(data.getValue(), DebugDrawObject.DEFAULT_SKIN));
    }

}
