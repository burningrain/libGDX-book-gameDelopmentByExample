package com.github.br.paper.airplane.ecs.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class RenderComponent implements Component {

    public TextureRegion region;
    public ParticleEffect particleEffect;

}
