package com.github.br.ecs.simple.component;


import com.badlogic.gdx.math.Shape2D;
import com.badlogic.gdx.math.Vector2;
import com.github.br.ecs.simple.physics.Boundary;

public class PhysicsComponent implements EcsComponent {

    public Vector2 movement;        // скорость
    public Vector2 acceleration;    // ускорение
    public Boundary boundary; //Shape2D shape;           // физическая форма

}
