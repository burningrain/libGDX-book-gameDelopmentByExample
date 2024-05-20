package com.github.br.paper.airplane.ecs.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector2;

public class TransformComponent implements Component {

    public Vector2 position;
    public Vector2 scale;
    public Vector2 origin;
    public float angle;

    public float width;
    public float height;
    public float radius;

}
