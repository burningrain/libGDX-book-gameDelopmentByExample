package com.packt.flappeebee.screen.level.level1.systems;

import com.artemis.ComponentMapper;
import com.artemis.annotations.All;
import com.artemis.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.github.br.gdx.simple.animation.SimpleAnimation;
import com.github.br.gdx.simple.animation.SimpleAnimationSystem;
import games.rednblack.editor.renderer.components.TextureRegionComponent;

@All({AnimationComponent.class, TextureRegionComponent.class})
public class AnimationSystem extends IteratingSystem {

    private final SimpleAnimationSystem simpleAnimationSystem = new SimpleAnimationSystem();

    protected ComponentMapper<AnimationComponent> animationMapper;
    protected ComponentMapper<TextureRegionComponent> textureRegionComponentMapper;

    @Override
    protected void process(int entityId) {
        AnimationComponent animationComponent = animationMapper.get(entityId);
        TextureRegionComponent textureRegionComponent = textureRegionComponentMapper.get(entityId);

        if (animationComponent.simpleAnimationComponent == null) {
            throw new GdxRuntimeException("simpleAnimationComponent was not set for entityId=" + entityId);
        }

        textureRegionComponent.region = animationComponent.simpleAnimationComponent.animatorDynamicPart.currentFrame;
        simpleAnimationSystem.update(Gdx.graphics.getDeltaTime(), animationComponent.simpleAnimationComponent);
    }

    public void addAnimation(SimpleAnimation simpleAnimation) {
        simpleAnimationSystem.addAnimation(simpleAnimation);
    }

    public void unload(String title) {
        simpleAnimationSystem.unload(title);
    }

    @Override
    public void dispose() {
        simpleAnimationSystem.dispose();
    }

}
