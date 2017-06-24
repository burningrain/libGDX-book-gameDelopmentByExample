package com.packt.flappeebee.model;


import com.github.br.ecs.simple.engine.EcsContainer;
import com.github.br.ecs.simple.engine.EcsSettings;
import com.packt.flappeebee.GamePublisher;
import com.packt.flappeebee.ScreenManager;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static com.packt.flappeebee.model.LayerEnum.*;


public class World implements ScreenManager, GamePublisher.Subscriber {

    private EcsContainer container;

    public World() {
        GamePublisher.self().addListener(GamePublisher.State.NEW_GAME, this);
    }

    @Override
    public void handleGameState(GamePublisher.State state) {
        startNewGame();
        GamePublisher.self().changeState(GamePublisher.State.PLAYING);
    }

    public void startNewGame() {
        //TODO
        EcsSettings settings = new EcsSettings();
        settings.layers = new String[]{
                BACKGROUND.name(),
                PRE_BACKGROUND.name(),
                BACK_EFFECTS.name(),
                MAIN_LAYER.name(),
                FRONT_EFFECTS.name()
        };
        container = new EcsContainer(settings);
        container.setDebugMode(true);

        ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(1);
        scheduledExecutorService.scheduleWithFixedDelay(new Runnable(){
            @Override
            public void run() {
                for(int i = 0; i < 5; i++){
                    GameObjectFactory.createFlappee(container);
                }
                for(int i =0; i < 3; i++){
                    GameObjectFactory.createCloud(container);
                }
            }
        }, 0, 5L, TimeUnit.SECONDS);

        for(int i = 0; i < 6; i++){
            GameObjectFactory.createPlant(container);
        }
        GameObjectFactory.createCrab(container);
        GameObjectFactory.createBackground(container);
    }


    @Override
    public void update(float delta) {
        if (GamePublisher.self().getCurrentState() == GamePublisher.State.PLAYING) {
            container.update(delta);
        }
    }


}
