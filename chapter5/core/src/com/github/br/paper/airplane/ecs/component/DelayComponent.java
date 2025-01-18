package com.github.br.paper.airplane.ecs.component;

import com.badlogic.ashley.core.Component;

public class DelayComponent implements Component {

    public short name; // TODO для будущего пуллинга
    public float timeSec;
    public Runnable executeOnFinish;

    public float currentTime;

    public DelayComponent(short name, float timeSec, Runnable executeOnFinish) {
        this.name = name;
        this.timeSec = timeSec;
        this.executeOnFinish = executeOnFinish;
        reset();
    }

    public void reset() {
        this.currentTime = this.timeSec;
    }

}
