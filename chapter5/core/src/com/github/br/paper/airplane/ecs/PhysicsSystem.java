package com.github.br.paper.airplane.ecs;

import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.physics.box2d.World;

public class PhysicsSystem extends EntitySystem {

    private final World world;

    public PhysicsSystem(World world) {
        this.world = world;
    }

    @Override
    public void update (float deltaTime) {
        world.step(1 / 60f, 6, 2);
    }

}
