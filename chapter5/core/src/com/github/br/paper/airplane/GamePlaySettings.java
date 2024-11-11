package com.github.br.paper.airplane;

public class GamePlaySettings {

    private final byte heroLifeCountMax = 4;

    private final byte bulletInitCount = 10;
    private final byte bulletAddingByCoinCount = 5;
    private final byte bulletDamage = 1;

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

    public byte getBulletDamage() {
        return bulletDamage;
    }

    public byte getWallLife() {
        return wallLife;
    }

}
