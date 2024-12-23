package com.github.br.paper.airplane.ecs.system;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.github.br.paper.airplane.GameManager;
import com.github.br.paper.airplane.bullet.BulletStrategy;
import com.github.br.paper.airplane.ecs.component.Box2dComponent;
import com.github.br.paper.airplane.ecs.component.HeroComponent;
import com.github.br.paper.airplane.ecs.component.Mappers;
import com.github.br.paper.airplane.ecs.component.TransformComponent;

public class InputSystem extends EntitySystem {

    private final Family family = Family.all(HeroComponent.class).get();
    private final Mappers mappers;
    private final GameManager gameManager;

    private long lastTime;
    private boolean isFire = false;
    private boolean isPressed = false;
    private final Vector2 force = new Vector2(0, 4);
    private Entity hero = null;

    private final InputAdapter inputAdapter = new InputAdapter() {

        @Override
        public boolean touchDown(int screenX, int screenY, int pointer, int button) {
            return pressDown();
        }

        @Override
        public boolean touchUp(int screenX, int screenY, int pointer, int button) {
            return pressUp();
        }

        @Override
        public boolean keyDown(int keycode) {
            return pressDown();
        }

        @Override
        public boolean keyUp(int keycode) {
            return pressUp();
        }

    };

    public InputSystem(Mappers mappers, GameManager gameManager) {
        this.mappers = mappers;
        this.gameManager = gameManager;
    }

    private boolean pressUp() {
        isPressed = false;
        return true;
    }

    private boolean pressDown() {
        isPressed = true;
        long now = System.currentTimeMillis();
        isFire = ((float) (now - lastTime)) < 200;
        lastTime = now;

        return true;
    }

    @Override
    public void addedToEngine(Engine engine) {
        Gdx.input.setInputProcessor(inputAdapter);
    }

    @Override
    public void removedFromEngine(Engine engine) {
        Gdx.input.setInputProcessor(null);
    }

    @Override
    public void update(float deltaTime) {
        HeroComponent heroComponent = null;
        if (hero != null) {
            heroComponent = mappers.heroMapper.get(hero);
            heroComponent.setUp(isPressed);
            heroComponent.setFire(isFire);
        }

        if (!isPressed) {
            return;
        }

        if (hero == null) {
            ImmutableArray<Entity> entities = getEngine().getEntitiesFor(family);
            if (entities != null && entities.size() > 0) {
                hero = entities.get(0);
            }
        }

        // либо стреляем, либо просто поднимаем вверх. Два действия повешены на тапы, поэтому так.
        // два быстрых тапа - выстрел. Один тап и удержание - подъем вверх
        if (isFire) {
            short bulletCount = heroComponent.getBulletCount();
            BulletStrategy bulletStrategy = gameManager.bulletFactory.getBulletStrategy(heroComponent.getBulletType());
            if (bulletCount == 0 || !bulletStrategy.isBulletsEnough(bulletCount)) {
                // нечем стрелять
                return;
            }

            heroComponent.setBulletCount(bulletStrategy.reduceBullets(heroComponent.getBulletCount()));
            TransformComponent heroTransformComponent = mappers.transformMapper.get(hero);
            Entity[] bullets = bulletStrategy.createBullet(this.getEngine(), heroTransformComponent);
            for (Entity bullet : bullets) {
                getEngine().addEntity(bullet);
            }

            isFire = false;
        } else {
            // если не стреляем, значит поднимаем самолетик вверх
            Box2dComponent box2dComponent = mappers.box2dMapper.get(hero);
            if (box2dComponent.body != null) {
                box2dComponent.body.applyForce(force, box2dComponent.body.getWorldCenter(), true);
            }
        }
    }

}
