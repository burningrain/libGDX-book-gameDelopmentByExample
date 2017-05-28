package com.github.br.ecs.simple.system.render;


import com.github.br.ecs.simple.engine.EcsNode;
import com.github.br.ecs.simple.system.transform.TransformComponent;

public class RendererNode extends EcsNode {

    public TransformComponent transform;
    public RendererComponent renderer;

}
