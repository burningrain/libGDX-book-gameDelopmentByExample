package com.github.br.ecs.simple.component;


import com.badlogic.gdx.math.Vector2;

public class TransformComponent implements EcsComponent {

    public Vector2 position; // координаты
    public float rotation;   // угол в градусах

    public String debugInfo; // информация для дебага

}
