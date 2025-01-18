package com.github.br.paper.airplane.ecs.component;

import com.badlogic.ashley.core.Component;
import com.github.br.paper.airplane.bullet.BulletType;

import java.util.ArrayList;

/**
 * маркерный компонент для героя
 */
public class HeroComponent implements Component {

    private byte lifeCount;
    private short bulletCount;
    private BulletType bulletType;

    private boolean isApplyAngularImpulse;

    private final ArrayList<ComponentListener<HeroComponent>> listeners = new ArrayList<>(4);

    public byte getLifeCount() {
        return lifeCount;
    }

    public short getBulletCount() {
        return bulletCount;
    }

    public void setLifeCount(byte lifeCount) {
        this.lifeCount = lifeCount;
        notifyListeners();
    }

    public void setBulletCount(short bulletCount) {
        this.bulletCount = bulletCount;
        notifyListeners();
    }

    public void setBulletType(BulletType bulletType) {
        this.bulletType = bulletType;
        notifyListeners();
    }

    public void addListener(ComponentListener<HeroComponent> listener) {
        listeners.add(listener);
    }

    public void removeListener(ComponentListener<HeroComponent> listener) {
        listeners.remove(listener);
    }

    public void clearListeners() {
        listeners.clear();
    }

    public void notifyListeners() {
        for (ComponentListener<HeroComponent> listener : listeners) {
            listener.update(this);
        }
    }

    public BulletType getBulletType() {
        return bulletType;
    }

    public void setApplyAngularImpulse(boolean applyAngularImpulse) {
        this.isApplyAngularImpulse = applyAngularImpulse;
    }

    public boolean isApplyAngularImpulse() {
        return isApplyAngularImpulse;
    }

}
