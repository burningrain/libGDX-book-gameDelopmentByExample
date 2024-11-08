package com.github.br.paper.airplane.ecs.component;

import com.badlogic.ashley.core.Component;

public class HealthComponent implements Component {

    public int health;
    public int damage;

    public HealthComponent(int health, int damage) {
        this.health = health;
        this.damage = damage;
    }

}
