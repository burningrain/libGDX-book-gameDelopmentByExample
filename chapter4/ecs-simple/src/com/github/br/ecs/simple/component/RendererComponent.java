package com.github.br.ecs.simple.component;


import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class RendererComponent implements EcsComponent {

    public TextureRegion textureRegion;
    public boolean flipX = false;
    public boolean flipY = false;

}
