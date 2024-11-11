package com.github.br.paper.airplane.ecs.component;

import com.badlogic.ashley.core.Component;

public class HealthComponent implements Component {

    public short health;
    public short damage;

    public HealthComponent(short health, short damage) {
        this.health = health;
        this.damage = damage;
    }

}
