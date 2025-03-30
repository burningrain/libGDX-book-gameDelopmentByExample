package com.packt.flappeebee.screen.level.level1.systems.components;

import com.artemis.Component;

import java.util.ArrayList;

public class HeroComponent extends Component {

    private int lifeCount;
    private int pearlsCount;

    private final ArrayList<ComponentListener<HeroComponent>> listeners = new ArrayList<>(4);

    public int getLifeCount() {
        return lifeCount;
    }

    public void setLifeCount(int lifeCount) {
        this.lifeCount = lifeCount;
        notifyListeners();
    }

    public int getPearlsCount() {
        return pearlsCount;
    }

    public void setPearlsCount(int pearlsCount) {
        this.pearlsCount = pearlsCount;
        notifyListeners();
    }

    // observer
    public void addListener(ComponentListener<HeroComponent> listener) {
        listeners.add(listener);
    }

    public void removeListener(ComponentListener<HeroComponent> listener) {
        listeners.remove(listener);
    }

    public void notifyListeners() {
        for (ComponentListener<HeroComponent> listener : listeners) {
            listener.update(this);
        }
    }

}
