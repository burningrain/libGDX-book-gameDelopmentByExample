package com.github.br.ecs.simple.component;


import com.badlogic.gdx.math.Vector2;

public class TransformComponent implements EcsComponent {

    public Vector2 position; // координаты
    public Vector2 scale = new Vector2(1f, 1f); // масштаб по осям
    public float rotation;   // угол в градусах

    public String debugInfo; // информация для дебага TODO убрать отсюда в отдельный компонент и систему

}
