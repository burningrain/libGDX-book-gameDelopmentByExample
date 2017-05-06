package com.packt.flappeebee.render;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.packt.flappeebee.GamePublisher;
import com.packt.flappeebee.ScreenManager;
import com.packt.flappeebee.model.World;
import com.packt.flappeebee.render.layers.LayerFolder;
import com.packt.flappeebee.render.layers.impl.GameLayer;

/**
 * Created by user on 28.02.2017.
 */
public class RenderDispatcher implements ScreenManager, GamePublisher.Subscriber {

    private LayerFolder layerFolder;

    public RenderDispatcher(World model) {
        GamePublisher.self().addListener(GamePublisher.State.NEW_GAME, this);
        GamePublisher.self().addListener(GamePublisher.State.GAME_OVER, this);

        layerFolder = new LayerFolder("GameBaseLayers");
        GameLayer gameLayer = new GameLayer();
        layerFolder.addLayer(gameLayer);

    }

    @Override
    public void update(float delta) {
        layerFolder.draw();
    }



    @Override
    public void handleGameState(GamePublisher.State state) {
        //todo
    }
}
