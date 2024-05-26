package com.github.br.paper.airplane.ecs.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector2;

public class InitComponent implements Component {

    public boolean isNew = true;
    public Vector2 velocity;

}
