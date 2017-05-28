package com.github.br.ecs.simple.system.animation;

import com.github.br.ecs.simple.engine.EcsNode;
import com.github.br.ecs.simple.system.render.RendererComponent;
import com.github.br.ecs.simple.system.transform.TransformComponent;


/**
 * Created by user on 10.04.2017.
 */
public class AnimationNode extends EcsNode {

    public TransformComponent transform;
    public RendererComponent rendererComponent;
    public AnimationComponent animationComponent;

}
