package com.github.br.paper.airplane.ecs.script;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.github.br.paper.airplane.GameSettings;
import com.github.br.paper.airplane.ecs.component.BulletTypeComponent;
import com.github.br.paper.airplane.ecs.component.DestroyedComponent;
import com.github.br.paper.airplane.ecs.component.HeroComponent;
import com.github.br.paper.airplane.ecs.component.Script;

public class BulletTypeScript extends Script {

    private final GameSettings gameSettings;

    public BulletTypeScript(GameSettings gameSettings) {
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
    public void postSolve(Contact contact, ContactImpulse impulse) {
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
            changeBulletType(entityA, entityB);
            entityB.add(new DestroyedComponent());
        } else if(isHero(entityB)) {
            changeBulletType(entityB, entityA);
            entityA.add(new DestroyedComponent());
        }
    }

    private void changeBulletType(Entity hero, Entity bulletTypeItem) {
        HeroComponent heroComponent = getMappers().heroMapper.get(hero);
        BulletTypeComponent bulletTypeComponent = getMappers().bulletTypeMapper.get(bulletTypeItem);
        heroComponent.setBulletType(bulletTypeComponent.bulletType);
        heroComponent.setBulletCount((short) (heroComponent.getBulletCount() + 10));
    }

}
