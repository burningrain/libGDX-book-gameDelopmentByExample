package com.github.br.paper.airplane.ecs.system.physics.collision;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.physics.box2d.*;
import com.github.br.paper.airplane.ecs.component.DestroyedComponent;
import com.github.br.paper.airplane.ecs.component.HealthComponent;
import com.github.br.paper.airplane.ecs.component.Mappers;
import com.github.br.paper.airplane.ecs.system.physics.PhysicsUtils;

public class DefaultCollisionStrategy implements ContactListener {

    private final World world;
    private final Mappers mappers;

    private final PhysicsUtils physicsUtils;

    public DefaultCollisionStrategy(PhysicsUtils physicsUtils, World world, Mappers mappers) {
        this.world = world;
        this.mappers = mappers;

        this.physicsUtils = physicsUtils;
    }

    @Override
    public void beginContact(Contact contact) {

    }

    @Override
    public void endContact(Contact contact) {

    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse contactImpulse) {
        if (!contact.isTouching()) {
            return;
        }

        Entity entityA = (Entity) contact.getFixtureA().getBody().getUserData();
        Entity entityB = (Entity) contact.getFixtureB().getBody().getUserData();

        resolve(contact, entityA, entityB);
    }

    private void resolve(Contact contact, Entity entityA, Entity entityB) {
        if (entityA == null || entityB == null) {
            return;
        }
        HealthComponent healthA = mappers.healthMapper.get(entityA);
        HealthComponent healthB = mappers.healthMapper.get(entityB);
        if (healthA == null || healthB == null) {
            return;
        }

        boolean isWasDamage = false;
        if (healthA.health > 0) {
            healthA.health -= healthB.damage;
            isWasDamage = true;
            if (healthA.health <= 0) {
                entityA.add(new DestroyedComponent());
            }
        }

        if (healthB.health > 0) {
            healthB.health -= healthA.damage;
            isWasDamage = true;
            if (healthB.health <= 0) {
                entityB.add(new DestroyedComponent());
            }
        }

        contact.setEnabled(!isWasDamage);
    }

}
