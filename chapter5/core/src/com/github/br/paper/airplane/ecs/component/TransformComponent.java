package com.github.br.paper.airplane.ecs.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector2;

public class TransformComponent implements Component {

    public Vector2 position;
    public Vector2 scale;
    public Vector2 origin;
    public float degreeAngle;

    public float width;
    public float height;
    public float radius;

    //TODO вынести в пул
    public TransformComponent copy() {
        TransformComponent result = new TransformComponent();
        result.position = this.position.cpy();
        result.scale = this.scale.cpy();
        result.origin = this.origin.cpy();
        result.degreeAngle = this.degreeAngle;
        result.width = this.width;
        result.height = this.height;
        result.radius = this.radius;

        return result;
    }

}
