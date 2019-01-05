package com.github.br.ecs.simple.system.animation;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.github.br.ecs.simple.engine.EcsSystem;

import java.util.Collection;

/**
 * Created by user on 09.04.2017.
 */
public class AnimationSystem extends EcsSystem<AnimationNode> {

    public AnimationSystem() {
        super(AnimationNode.class);
    }

    @Override
    public void update(float delta, Collection<AnimationNode> nodes) {
        for (AnimationNode node : nodes) {
            node.animationComponent.controller.update();
            TextureRegion textureRegion = node.animationComponent.controller.getCurrentAnimator().getCurrentFrame();
            node.rendererComponent.textureRegion = textureRegion;
        }
    }

}
