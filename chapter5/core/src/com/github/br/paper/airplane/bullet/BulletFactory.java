package com.github.br.paper.airplane.bullet;

import com.github.br.paper.airplane.GameManager;

public class BulletFactory {

    private final GameManager gameManager;

    private final FireBullet fireBullet;
    private final ElectricalBullet electricalBullet;

    public BulletFactory(GameManager gameManager) {
        this.gameManager = gameManager;
        this.fireBullet = new FireBullet(gameManager);
        this.electricalBullet = new ElectricalBullet(gameManager);
    }

    public BulletStrategy getBulletStrategy(BulletType bulletType) {
        switch (bulletType) {
            case FIRE:
                return fireBullet;
            case ELECTRICITY:
                return electricalBullet;

            default:
                throw new IllegalArgumentException();
        }
    }

}
