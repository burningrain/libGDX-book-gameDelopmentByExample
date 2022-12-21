package com.github.br.ecs.simple.system.animation;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.IntMap;
import com.github.br.ecs.simple.engine.EcsEntity;
import com.github.br.ecs.simple.engine.EcsSystem;
import com.github.br.ecs.simple.system.render.RendererComponent;
import com.github.br.gdx.simple.animation.SimpleAnimation;
import com.github.br.gdx.simple.animation.SimpleAnimationSystem;

/**
 * Created by user on 09.04.2017.
 */
public class AnimationSystem extends EcsSystem {

    private final SimpleAnimationSystem simpleAnimationSystem = new SimpleAnimationSystem();

    public AnimationSystem() {
        super(RendererComponent.class, AnimationComponent.class);
    }

    @Override
    public void update(float delta, IntMap.Values<EcsEntity> entities) {
        for (EcsEntity entity : entities) {
            AnimationComponent animationComponent = entity.getComponent(AnimationComponent.class);
            simpleAnimationSystem.update(delta, animationComponent.simpleAnimationComponent);
            entity.getComponent(RendererComponent.class).textureRegion =
                    animationComponent.simpleAnimationComponent.animatorDynamicPart.currentFrame;
        }
    }

    public void addAnimation(SimpleAnimation simpleAnimation) {
        simpleAnimationSystem.addAnimation(simpleAnimation);
    }

    public void load(FileHandle animationsDir) {
        simpleAnimationSystem.load(animationsDir);
    }

}
