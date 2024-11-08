package com.github.br.paper.airplane.ecs.system.physics;

import com.badlogic.gdx.physics.box2d.*;
import com.github.br.paper.airplane.ecs.component.Mappers;
import com.github.br.paper.airplane.ecs.component.Script;
import com.github.br.paper.airplane.ecs.system.physics.collision.DefaultCollisionStrategy;
import com.github.br.paper.airplane.ecs.system.physics.collision.PhysicsUtils;

public class ContactListenerImpl implements ContactListener {

    private final Mappers mappers;
    private final World world;

    private final PhysicsUtils physicsUtils;

    private final DefaultCollisionStrategy collisionStrategy;

    public ContactListenerImpl(PhysicsUtils physicsUtils, World world, Mappers mappers) {
        this.world = world;
        this.mappers = mappers;
        this.physicsUtils = physicsUtils;

        this.collisionStrategy = new DefaultCollisionStrategy(physicsUtils, world, mappers);
    }

    @Override
    public void beginContact(Contact contact) {
        Script[] scriptsA = physicsUtils.getScripts(contact.getFixtureA());
        if (scriptsA != null) {
            for (Script script : scriptsA) {
                script.beginContact(contact);
            }
        }

        Script[] scriptsB = physicsUtils.getScripts(contact.getFixtureB());
        if (scriptsB != null) {
            for (Script script : scriptsB) {
                script.beginContact(contact);
            }
        }

        collisionStrategy.beginContact(contact);
    }

    @Override
    public void endContact(Contact contact) {
        Script[] scriptsA = physicsUtils.getScripts(contact.getFixtureA());
        if (scriptsA != null) {
            for (Script script : scriptsA) {
                script.endContact(contact);
            }
        }

        Script[] scriptsB = physicsUtils.getScripts(contact.getFixtureB());
        if (scriptsB != null) {
            for (Script script : scriptsB) {
                script.endContact(contact);
            }
        }

        collisionStrategy.endContact(contact);
    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {
        Script[] scriptsA = physicsUtils.getScripts(contact.getFixtureA());
        if (scriptsA != null) {
            for (Script script : scriptsA) {
                script.preSolve(contact, oldManifold);
            }
        }

        Script[] scriptsB = physicsUtils.getScripts(contact.getFixtureB());
        if (scriptsB != null) {
            for (Script script : scriptsB) {
                script.preSolve(contact, oldManifold);
            }
        }

        collisionStrategy.preSolve(contact, oldManifold);
    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {
        Script[] scriptsA = physicsUtils.getScripts(contact.getFixtureA());
        if (scriptsA != null) {
            for (Script script : scriptsA) {
                script.postSolve(contact, impulse);
            }
        }

        Script[] scriptsB = physicsUtils.getScripts(contact.getFixtureB());
        if (scriptsB != null) {
            for (Script script : scriptsB) {
                script.postSolve(contact, impulse);
            }
        }

        collisionStrategy.postSolve(contact, impulse);
    }

}
