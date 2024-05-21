package com.github.br.paper.airplane.ecs.system;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.systems.IntervalSystem;
import com.github.br.paper.airplane.GameSettings;
import com.github.br.paper.airplane.gameworld.GameEntityFactory;

import java.util.Random;

public class WallGeneratorSystem extends IntervalSystem {

    private final GameSettings gameSettings;
    private final GameEntityFactory gameEntityFactory;
    private final Random random = new Random();

    public WallGeneratorSystem(GameSettings gameSettings, GameEntityFactory gameEntityFactory) {
        super(2);
        this.gameSettings = gameSettings;
        this.gameEntityFactory = gameEntityFactory;
    }


    @Override
    protected void updateInterval() {
        int virtualScreenWidth = gameSettings.getVirtualScreenWidth();

        int width = random.nextInt(500);
        int height = random.nextInt(500);
        int angle = random.nextInt(360);

        int y = random.nextInt(virtualScreenWidth - height);


        Entity wall = gameEntityFactory.createWall(getEngine(), virtualScreenWidth + 10, y, width, height, angle);
        getEngine().addEntity(wall);
    }

}
