package com.github.br.ecs.simple.system.render;

/**
 * Created by user on 06.04.2019.
 */
public class LayerData {

    public String title;
    public ShaderData shaderData;

    public LayerData(String title, ShaderData shaderData) {
        this.title = title;
        this.shaderData = shaderData;
    }

}
