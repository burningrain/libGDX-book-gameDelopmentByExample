package com.github.br.paper.airplane.ecs.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector2;

public class SimpleTweenComponent implements Component {

    public Vector2 velocity;
    public Vector2 acceleration;

    public SimpleTweenComponent(Vector2 velocity, Vector2 acceleration) {
        this.velocity = velocity;
        this.acceleration = acceleration;
    }

}
