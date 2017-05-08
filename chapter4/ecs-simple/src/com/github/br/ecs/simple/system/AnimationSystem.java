package com.github.br.ecs.simple.system;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.github.br.ecs.simple.node.AnimationNode;

/**
 * Created by user on 09.04.2017.
 */
public class AnimationSystem extends EcsSystem<AnimationNode> {

    public AnimationSystem() {
        super(AnimationNode.class);
    }

    @Override
    public void update(float delta) {
        for(AnimationNode node : nodes){
            node.animationComponent.controller.update();
            TextureRegion textureRegion = node.animationComponent.controller.getCurrentAnimator().getCurrentFrame();
            node.rendererComponent.textureRegion = textureRegion;
        }
    }
}
