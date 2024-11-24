package com.github.br.paper.airplane.bullet;

import com.github.br.paper.airplane.GameManager;

public class BulletFactory {

    private final GameManager gameManager;

    private final FireBullet fireBullet;
    private final ElectricalBullet electricalBullet;
    private final VenomBullet venomBullet;

    public BulletFactory(GameManager gameManager) {
        this.gameManager = gameManager;
        this.fireBullet = new FireBullet(gameManager);
        this.electricalBullet = new ElectricalBullet(gameManager);
        this.venomBullet = new VenomBullet(gameManager);
    }

    public BulletStrategy getBulletStrategy(BulletType bulletType) {
        switch (bulletType) {
            case FIRE:
                return fireBullet;
            case ELECTRICITY:
                return electricalBullet;
            case VENOM:
                return venomBullet;

            default:
                throw new IllegalArgumentException();
        }
    }

}
