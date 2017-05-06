package com.github.br.ecs.simple.node;

import com.github.br.ecs.simple.component.PhysicsComponent;
import com.github.br.ecs.simple.component.TransformComponent;

public class PhysicsNode implements EcsNode {

    public TransformComponent transform;
    public PhysicsComponent physics;

}
