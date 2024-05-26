package com.github.br.paper.airplane.ecs.component;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.physics.box2d.ContactListener;

public interface Script extends ContactListener {

    void update(Entity entity, float deltaTime);

}
