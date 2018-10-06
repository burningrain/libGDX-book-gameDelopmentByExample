package com.github.br.ecs.simple.engine.debug.drawobject;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.github.br.ecs.simple.engine.debug.LibGdxPanel;
import com.github.br.ecs.simple.engine.debug.data.TableData;
import com.github.br.ecs.simple.utils.ViewHelper;

/**
 * Created by user on 07.04.2018.
 */
public class TableDrawObject implements DebugDrawObject<TableData> {

    @Override
    public void draw(ShapeRenderer shapeRenderer, SpriteBatch spriteBatch, LibGdxPanel panel, final TableData data) {
        final Table table = new Table(DebugDrawObject.DEFAULT_SKIN);
        data.forEach(new TableData.Callback() {
            @Override
            public void call(String key, String value) {
                table.add(key);
                table.add(value);
                table.row();
            }
        });
        panel.add(table);
    }

}
