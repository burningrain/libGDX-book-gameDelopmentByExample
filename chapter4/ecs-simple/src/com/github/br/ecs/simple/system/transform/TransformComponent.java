package com.github.br.ecs.simple.system.transform;


import com.badlogic.gdx.math.Vector2;
import com.github.br.ecs.simple.engine.EcsComponent;

public class TransformComponent implements EcsComponent {

    public Vector2 position; // координаты
    public Vector2 scale = new Vector2(1f, 1f); // масштаб по осям
    public float rotation;   // угол в градусах

}
