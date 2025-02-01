package com.github.br.paper.airplane.ecs.system.render;

import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.ParticleEmitter;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.github.br.paper.airplane.Utils;
import com.github.br.paper.airplane.ecs.component.TransformComponent;
import com.github.br.paper.airplane.ecs.component.render.ParticleEffectData;

public class RenderParticleSubsystem {

    private final Utils utils;

    public RenderParticleSubsystem(Utils utils) {
        this.utils = utils;
    }

    public void render(TransformComponent transformComponent, SpriteBatch spriteBatch, float deltaTime, ParticleEffectData effectData) {
        Vector2 anchor = effectData.renderPosition.anchorDelta;
        ParticleEffect particleEffect = effectData.particleEffect;

        Vector2 newPosition = utils.rotatePointToAngle(anchor.x, anchor.y, transformComponent.degreeAngle);
        particleEffect.setPosition(
                transformComponent.position.x + transformComponent.width / 2 + newPosition.x,
                transformComponent.position.y + transformComponent.height / 2 + newPosition.y
        );
        rotateBy(particleEffect, transformComponent.degreeAngle - 180); //TODO FIXME ?!
        if(effectData.effectType != null) {
            applyEffectType(effectData);
        }
        particleEffect.draw(spriteBatch, deltaTime);
    }

    private void applyEffectType(ParticleEffectData effectData) {
        switch (effectData.effectType) {
            case TRANSPARENCY_FADE_OUT:
                for (ParticleEmitter emitter : effectData.particleEffect.getEmitters()) {
                    ParticleEmitter.ScaledNumericValue transparency = emitter.getTransparency();
                    float[] scaling = transparency.getScaling();
                    int length = scaling.length;
                    for (int i = 0; i < length; i++) {
                        scaling[i] = MathUtils.lerp(scaling[i], 0, 0.05f);
                    }
                }
                break;
        }
    }

    private void rotateBy(ParticleEffect particleEffect, float targetAngle) {
        Array<ParticleEmitter> emitters = particleEffect.getEmitters();
        for (int i = 0; i < emitters.size; i++) {
            /* find angle property and adjust that by letting the min, max of low and high span their current size around your angle */
            ParticleEmitter particleEmitter = emitters.get(i);
            ParticleEmitter.ScaledNumericValue angle = particleEmitter.getAngle();

            float angleHighMin = angle.getHighMin();
            float angleHighMax = angle.getHighMax();
            float spanHigh = angleHighMax - angleHighMin;
            angle.setHigh(targetAngle, targetAngle);

            float angleLowMin = angle.getLowMin();
            float angleLowMax = angle.getLowMax();
            float spanLow = angleLowMax - angleLowMin;
            angle.setLow(0);
        }
    }

}
