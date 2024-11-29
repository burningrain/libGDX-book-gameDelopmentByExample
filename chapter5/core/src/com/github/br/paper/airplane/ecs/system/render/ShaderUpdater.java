package com.github.br.paper.airplane.ecs.system.render;

import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by user on 06.04.2019.
 */
public interface ShaderUpdater {

    void update(float deltaTime, Vector2 resolution, ShaderProgram shaderProgram);

}
