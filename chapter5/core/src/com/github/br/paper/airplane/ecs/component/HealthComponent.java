package com.github.br.paper.airplane.ecs.component;

import com.badlogic.ashley.core.Component;

public class HealthComponent implements Component {

    public int health;

    public HealthComponent(int health) {
        this.health = health;
    }

}
