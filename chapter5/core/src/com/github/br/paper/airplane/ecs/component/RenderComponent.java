package com.github.br.paper.airplane.ecs.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.github.br.paper.airplane.gameworld.RenderLayers;

public class RenderComponent implements Component {

    public byte layer = RenderLayers.DEFAULT.getLayer();
    public TextureRegion region;
    public ParticleEffect particleEffect;
    public Vector2 anchorDelta = new Vector2().setZero(); // точка сдвига от центра координат

}
