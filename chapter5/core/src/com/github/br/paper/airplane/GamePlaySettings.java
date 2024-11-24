package com.github.br.paper.airplane;

public class GamePlaySettings {

    private final byte heroLifeCountMax = 4;

    private final byte bulletInitCount = 10;
    private final byte bulletAddingByCoinCount = 5;

    private final byte fireBulletDamage = 1;
    private final byte fireBulletCost = 1;
    private final float fireBulletVelocity = 3;

    private final byte electricalBulletDamage = 2;
    private final byte electricalBulletCost = 2;
    private final float electricalBulletVelocity = 5;

    private final byte wallLife = 3;


    public byte getHeroLifeCountMax() {
        return heroLifeCountMax;
    }

    public byte getBulletInitCount() {
        return bulletInitCount;
    }

    public byte getBulletAddingByCoinCount() {
        return bulletAddingByCoinCount;
    }

    public byte getFireBulletDamage() {
        return fireBulletDamage;
    }

    public byte getFireBulletCost() {
        return fireBulletCost;
    }

    public byte getElectricalBulletDamage() {
        return electricalBulletDamage;
    }

    public byte getElectricalBulletCost() {
        return electricalBulletCost;
    }

    public float getElectricalBulletVelocity() {
        return electricalBulletVelocity;
    }

    public byte getWallLife() {
        return wallLife;
    }

    public float getFireBulletVelocity() {
        return fireBulletVelocity;
    }

}
