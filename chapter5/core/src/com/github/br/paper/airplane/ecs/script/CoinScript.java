package com.github.br.paper.airplane.ecs.script;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.github.br.paper.airplane.GameSettings;
import com.github.br.paper.airplane.ecs.component.DestroyedComponent;
import com.github.br.paper.airplane.ecs.component.HeroComponent;
import com.github.br.paper.airplane.ecs.component.Script;

public class CoinScript extends Script {

    private final GameSettings gameSettings;

    public CoinScript(GameSettings gameSettings) {
        this.gameSettings = gameSettings;
    }

    @Override
    public void update(Entity entity, float deltaTime) {

    }

    @Override
    public void beginContact(Contact contact) {

    }

    @Override
    public void endContact(Contact contact) {

    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {
        if(!contact.isTouching()) {
            return;
        }

        contact.setEnabled(false);

        Entity entityA = (Entity) contact.getFixtureA().getBody().getUserData();
        Entity entityB = (Entity) contact.getFixtureB().getBody().getUserData();

        if (isHero(entityA)) {
            incrementBulletCount(entityA);
            entityB.add(new DestroyedComponent());
        } else if(isHero(entityB)) {
            incrementBulletCount(entityB);
            entityA.add(new DestroyedComponent());
        }
    }

    private void incrementBulletCount(Entity entityA) {
        HeroComponent heroComponent = getMappers().heroMapper.get(entityA);
        heroComponent.setBulletCount((short) (heroComponent.getBulletCount() + gameSettings.getGamePlaySettings().getBulletAddingByCoinCount()));
    }

    private boolean isHero(Entity entity) {
        HeroComponent heroComponent = getMappers().heroMapper.get(entity);
        if (heroComponent == null) {
            return false;
        }
        return true;
    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }

}
