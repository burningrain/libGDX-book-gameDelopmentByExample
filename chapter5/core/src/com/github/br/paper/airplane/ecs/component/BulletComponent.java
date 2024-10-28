package com.github.br.paper.airplane.ecs.component;

import com.badlogic.ashley.core.Component;

public class BulletComponent implements Component  {

    public int damage;

    public BulletComponent(int damage) {
        this.damage = damage;
    }
}
