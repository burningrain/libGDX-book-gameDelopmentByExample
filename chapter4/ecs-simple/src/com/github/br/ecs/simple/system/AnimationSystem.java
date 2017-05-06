package com.github.br.ecs.simple.system;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.github.br.ecs.simple.node.AnimationNode;

/**
 * Created by user on 09.04.2017.
 */
public class AnimationSystem extends EcsSystem<AnimationNode> {

    private SpriteBatch spriteBatch = new SpriteBatch();

    public AnimationSystem() {
        super(AnimationNode.class);
    }

    @Override
    public void update(float delta) {
        spriteBatch.begin();
        for(AnimationNode node : nodes){
            node.animationComponent.controller.update();
            TextureRegion textureRegion = node.animationComponent.controller.getCurrentAnimator().getCurrentFrame();
            spriteBatch.draw(textureRegion, node.transform.position.x, node.transform.position.y);
        }
        spriteBatch.end();
    }
}
