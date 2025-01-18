package com.github.br.paper.airplane.ecs.component.render;

public final class RenderUtils {

    private RenderUtils(){}

    public static void setEffect(RenderComponent renderComponent, ParticleEffectData effectData, int effectNumber) {
        if (renderComponent.effectData[effectNumber] == null) {
            effectData.particleEffect.reset();
            renderComponent.effectData[effectNumber] = effectData;
        }
    }


    public static void unsetEffect(RenderComponent renderComponent, int effectNumber) {
        renderComponent.effectData[effectNumber] = null;
    }

}
