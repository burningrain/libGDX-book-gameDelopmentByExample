package com.github.br.paper.airplane.ecs.component.render;

import com.badlogic.gdx.math.Vector2;
import com.github.br.paper.airplane.gameworld.RenderLayers;

public class RenderPosition {

    public byte layer = RenderLayers.DEFAULT.getLayer();
    public Vector2 anchorDelta = Vector2.Zero;

}
