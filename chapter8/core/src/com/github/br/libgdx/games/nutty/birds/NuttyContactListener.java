package com.github.br.libgdx.games.nutty.birds;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.github.br.libgdx.games.nutty.birds.screen.GameScreen;

public class NuttyContactListener implements ContactListener {

    public static final float REMOVING_WATERMARK = 1.75f;
    private final GameScreen gameScreen;

    public NuttyContactListener(GameScreen gameScreen) {
        this.gameScreen = gameScreen;
    }

    @Override
    public void beginContact(Contact contact) {
        if (!contact.isTouching()) {
            return;
        }

        Fixture attacker = contact.getFixtureA();
        Fixture defender = contact.getFixtureB();
        WorldManifold worldManifold = contact.getWorldManifold();
        if ("enemy".equals(defender.getUserData())) {
            Vector2 vel1 = attacker.getBody().getLinearVelocityFromWorldPoint(worldManifold.getPoints()[0]);
            Vector2 vel2 = defender.getBody().getLinearVelocityFromWorldPoint(worldManifold.getPoints()[0]);
            Vector2 impactVelocity = vel1.sub(vel2);
            if (Math.abs(impactVelocity.x) > REMOVING_WATERMARK || Math.abs(impactVelocity.y) > REMOVING_WATERMARK) {
                gameScreen.remove(defender.getBody());
            }
        }
    }

    @Override
    public void endContact(Contact contact) {
    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {
    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {
    }

}
