package com.github.br.ecs.simple.node;


import com.github.br.ecs.simple.component.RendererComponent;
import com.github.br.ecs.simple.component.TransformComponent;

public class RendererNode implements EcsNode {

    public TransformComponent transform;
    public RendererComponent renderer;

}
