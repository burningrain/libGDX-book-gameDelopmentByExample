package com.github.br.ecs.simple.system;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.github.br.ecs.simple.node.TransformNode;
import com.github.br.ecs.simple.utils.ViewHelper;

/**
 * Created by user on 28.03.2017.
 */
public class TransformDebugSystem extends EcsSystem<TransformNode> {

    private ShapeRenderer shapeRenderer = new ShapeRenderer();
    private SpriteBatch batch = new SpriteBatch();

    private GlyphLayout layout = new GlyphLayout();
    private BitmapFont font = new BitmapFont();

    public TransformDebugSystem() {
        super(TransformNode.class);
        shapeRenderer.setColor(Color.GREEN);
        font.setColor(Color.GREEN);
    }

    @Override
    public void update(float delta) {
        if(isDebugMode()){
            shapeRenderer.setProjectionMatrix(ViewHelper.camera.projection);
            shapeRenderer.setTransformMatrix(ViewHelper.camera.view);
            shapeRenderer.begin(ShapeRenderer.ShapeType.Line);

            batch.setProjectionMatrix(ViewHelper.camera.projection);
            batch.setTransformMatrix(ViewHelper.camera.view);
            batch.begin();

            for(TransformNode node : nodes){
                Vector2 position = node.transform.position;
                shapeRenderer.line(position.x - 2, position.y - 2, position.x + 2, position.y + 2);
                shapeRenderer.line(position.x - 2, position.y + 2, position.x + 2, position.y - 2);

                String debugInfo = node.transform.debugInfo;
                if(debugInfo != null){
                    layout.setText(font, debugInfo);
                    font.draw(batch, debugInfo, position.x + 4, position.y - 4);
                }
            }
            batch.end();
            shapeRenderer.end();
        }
    }


}
