package com.github.br.paper.airplane.ecs.component.render;

public final class RenderUtils {

    private RenderUtils() {
    }

    public static ParticleEffectData setEffect(RenderComponent renderComponent, ParticleEffectData effectData, int effectNumber) {
        if (renderComponent.effectData[effectNumber] == null) {
            effectData.particleEffect.reset();
            renderComponent.effectData[effectNumber] = effectData;
        }

        return renderComponent.effectData[effectNumber];
    }


    public static void unsetEffect(RenderComponent renderComponent, int effectNumber) {
        renderComponent.effectData[effectNumber] = null;
    }

    public static void setEffectType(ParticleEffectData effectData, EffectType effectType) {
        effectData.effectType = effectType;
    }

    public static void unsetEffectType(ParticleEffectData effectData) {
        effectData.effectType = null;
    }

}
