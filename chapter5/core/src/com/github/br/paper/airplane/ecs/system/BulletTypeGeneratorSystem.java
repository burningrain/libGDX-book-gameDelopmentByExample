package com.github.br.paper.airplane.ecs.system;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.systems.IntervalSystem;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.github.br.paper.airplane.GameSettings;
import com.github.br.paper.airplane.bullet.BulletType;
import com.github.br.paper.airplane.gameworld.GameEntityFactory;

public class BulletTypeGeneratorSystem extends IntervalSystem {

    private final GameSettings gameSettings;
    private final GameEntityFactory gameEntityFactory;

    public BulletTypeGeneratorSystem(GameSettings gameSettings, GameEntityFactory gameEntityFactory) {
        super(1f);
        this.gameSettings = gameSettings;
        this.gameEntityFactory = gameEntityFactory;
    }

    @Override
    protected void updateInterval() {
        int virtualScreenWidth = gameSettings.getVirtualScreenWidth();

        int x = virtualScreenWidth + 10;
        int y = MathUtils.random(0, virtualScreenWidth - 50);
        int velocity = MathUtils.random(-10, -1);

        Entity item = gameEntityFactory.createItemBulletType(getEngine(), x, y, new Vector2(velocity, 0f), createBulletType());
        getEngine().addEntity(item);
    }

    private BulletType createBulletType() {
        switch (MathUtils.random(BulletType.values().length - 1)) {
            case 0: return BulletType.FIRE;
            case 1: return BulletType.ELECTRICITY;
            case 2: return BulletType.VENOM;
            default:
                throw new IllegalArgumentException();
        }

    }

}
