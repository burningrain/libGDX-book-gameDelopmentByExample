package com.github.br.paper.airplane.ecs.system;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.physics.box2d.*;
import com.github.br.paper.airplane.ecs.component.*;

public class HealthSystem implements ContactListener {

    private final Mappers mappers;

    public HealthSystem(Mappers mappers) {
        this.mappers = mappers;
    }

    @Override
    public void beginContact(Contact contact) {

    }

    @Override
    public void endContact(Contact contact) {

    }

    @Override
    public void preSolve(Contact contact, Manifold manifold) {
        if(!contact.isTouching()) {
            return;
        }

        Entity entityA = (Entity) contact.getFixtureA().getBody().getUserData();
        Entity entityB = (Entity) contact.getFixtureB().getBody().getUserData();

        boolean resolveA = resolve(entityA, entityB);
        boolean resolveB = resolve(entityB, entityA);

        if (resolveA || resolveB) {
            contact.setEnabled(false);
        }
    }

    private boolean resolve(Entity entityA, Entity entityB) {
        BulletComponent bulletComponent = mappers.bulletMapper.get(entityA);
        if (bulletComponent != null) {
            entityA.add(new DestroyComponent());
            HealthComponent healthComponent = mappers.healthMapper.get(entityB);
            if (healthComponent != null) {
                healthComponent.health -= bulletComponent.damage;
                if (healthComponent.health <= 0) {
                    entityB.add(new DestroyComponent());
                }
                return true;
            }
        }
        return false;
    }

    @Override
    public void postSolve(Contact contact, ContactImpulse contactImpulse) {

    }

}
