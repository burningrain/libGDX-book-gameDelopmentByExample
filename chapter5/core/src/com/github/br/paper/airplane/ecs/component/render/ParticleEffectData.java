package com.github.br.paper.airplane.ecs.component.render;

import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.github.br.paper.airplane.ecs.system.render.RenderData;

public class ParticleEffectData implements RenderData {

    public RenderPosition renderPosition;
    public ParticleEffect particleEffect;
    public EffectType effectType;

    private ParticleEffect copy;

    public ParticleEffectData(RenderPosition renderPosition, ParticleEffect particleEffect) {
        this(renderPosition, particleEffect, null);
    }

    public ParticleEffectData(RenderPosition renderPosition, ParticleEffect particleEffect, EffectType effectType) {
        this.renderPosition = renderPosition;
        this.particleEffect = particleEffect;
        this.effectType = effectType;

        this.copy = new ParticleEffect(this.particleEffect);
    }

    public void reset() {
        particleEffect = new ParticleEffect(this.copy);
    }

}
