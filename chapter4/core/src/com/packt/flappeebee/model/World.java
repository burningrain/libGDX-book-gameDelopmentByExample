package com.packt.flappeebee.model;


import com.github.br.ecs.simple.engine.EcsContainer;
import com.packt.flappeebee.GamePublisher;
import com.packt.flappeebee.ScreenManager;

import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


public class World implements ScreenManager, GamePublisher.Subscriber {

    private EcsContainer container = new EcsContainer();

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
        container.setDebugMode(true);

        ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(1);
        scheduledExecutorService.scheduleWithFixedDelay(new Runnable(){
            @Override
            public void run() {
                for(int i = 0; i < 5000; i++){
                    GameObjectFactory.createFlappee(container);
                }
            }
        }, 0, 2L, TimeUnit.SECONDS);

//        for(int i = 0; i < 1; i++){
//            GameObjectFactory.createPlant(container);
//        }
        GameObjectFactory.createCrab(container);
    }


    @Override
    public void update(float delta) {
        if (GamePublisher.self().getCurrentState() == GamePublisher.State.PLAYING) {
            container.update(delta);
        }
    }


}
