package com.github.br.ecs.simple.system.animation;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.IntMap;
import com.github.br.ecs.simple.engine.EcsEntity;
import com.github.br.ecs.simple.engine.EcsSystem;
import com.github.br.ecs.simple.system.render.RendererComponent;

import java.util.Collection;

/**
 * Created by user on 09.04.2017.
 */
public class AnimationSystem extends EcsSystem {

    public AnimationSystem() {
        super(RendererComponent.class, AnimationComponent.class);
    }

    @Override
    public void update(float delta, IntMap.Values<EcsEntity> entities) {
        for (EcsEntity entity : entities) {
            AnimationComponent animationComponent = entity.getComponent(AnimationComponent.class);
            animationComponent.controller.update();
            TextureRegion textureRegion = animationComponent.controller.getCurrentAnimator().getCurrentFrame();
            entity.getComponent(RendererComponent.class).textureRegion = textureRegion;
        }
    }

}
