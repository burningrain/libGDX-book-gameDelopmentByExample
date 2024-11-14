package com.github.br.paper.airplane.bullet;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.github.br.paper.airplane.GameManager;
import com.github.br.paper.airplane.ecs.component.TransformComponent;

public abstract class BulletStrategy {

    private final GameManager gameManager;

    public BulletStrategy(GameManager gameManager) {
        this.gameManager = gameManager;
    }

    protected final GameManager getGameManager() {
        return gameManager;
    }

    public abstract boolean isBulletsEnough(short bulletCount);

    public abstract short reduceBullets(short bulletCount);

    public abstract Entity[] createBullet(Engine engine, TransformComponent heroTransformComponent);

}
