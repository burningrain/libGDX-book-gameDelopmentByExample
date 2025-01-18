package com.github.br.paper.airplane;

import com.badlogic.gdx.math.Vector2;

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

    private final byte venomBulletDamage = 3;
    private final byte venomBulletCost = 3;
    private final float venomBulletVelocity = 8;

    private final byte wallLife = 3;

    private final Vector2 upForce = new Vector2(0, 4);;

    // angular
    private final float angularImpulse = 0.02f;
    private final float defaultTorque = 0.02f;
    private final float endAngle = 95f;


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

    public byte getVenomBulletCost() {
        return venomBulletCost;
    }

    public float getVenomBulletVelocity() {
        return venomBulletVelocity;
    }

    public byte getVenomBulletDamage() {
        return venomBulletDamage;
    }


    public float getAngularImpulse() {
        return angularImpulse;
    }

    public float getDefaultTorque() {
        return defaultTorque;
    }

    public Vector2 getUpForce() {
        return upForce;
    }

    public float getEndAngle() {
        return endAngle;
    }

}
