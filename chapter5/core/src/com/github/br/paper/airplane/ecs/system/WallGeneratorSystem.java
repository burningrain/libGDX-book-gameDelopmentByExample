package com.github.br.paper.airplane.ecs.system;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.systems.IntervalSystem;
import com.badlogic.gdx.math.MathUtils;
import com.github.br.paper.airplane.GameSettings;
import com.github.br.paper.airplane.gameworld.GameEntityFactory;

public class WallGeneratorSystem extends IntervalSystem {

    private final GameSettings gameSettings;
    private final GameEntityFactory gameEntityFactory;

    public WallGeneratorSystem(GameSettings gameSettings, GameEntityFactory gameEntityFactory) {
        super(2);
        this.gameSettings = gameSettings;
        this.gameEntityFactory = gameEntityFactory;
    }


    @Override
    protected void updateInterval() {
        int virtualScreenWidth = gameSettings.getVirtualScreenWidth();

        int width = MathUtils.random(300);
        int height = MathUtils.random(300);

        int angle = MathUtils.random(50) - 25; // [-25; 25] angles

        int x = virtualScreenWidth + 10;
        int y = MathUtils.random(0, virtualScreenWidth - height);


        Entity wall = gameEntityFactory.createWall(getEngine(), x, y, width, height, angle);
        getEngine().addEntity(wall);
    }

}
