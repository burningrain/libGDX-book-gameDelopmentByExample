package com.github.br.paper.airplane.ecs;

import com.badlogic.ashley.core.*;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.math.Vector2;
import com.github.br.paper.airplane.ecs.component.Box2dComponent;
import com.github.br.paper.airplane.ecs.component.HeroComponent;
import com.github.br.paper.airplane.ecs.component.TransformComponent;

public class InputSystem extends EntitySystem {

    private boolean isPressed = false;
    private Entity hero = null;

    private final Family family = Family.all(HeroComponent.class).get();
    private final ComponentMapper<TransformComponent> transformMapper = ComponentMapper.getFor(TransformComponent.class);
    private final ComponentMapper<Box2dComponent> box2dMapper = ComponentMapper.getFor(Box2dComponent.class);

    private final Vector2 impulse = new Vector2(0, 3);

    private final InputAdapter inputAdapter = new InputAdapter() {

        @Override
        public boolean touchDown(int screenX, int screenY, int pointer, int button) {
            return pressDown();
        }

        @Override
        public boolean touchUp (int screenX, int screenY, int pointer, int button) {
            return pressUp();
        }

        @Override
        public boolean keyDown (int keycode) {
            return pressDown();
        }

        @Override
        public boolean keyUp (int keycode) {
            return pressUp();
        }

    };

    private boolean pressUp() {
        isPressed = false;
        return true;
    }

    private boolean pressDown() {
        if (isPressed) {
            return false;
        }
        if (hero == null) {
            return false;
        }

        Box2dComponent box2dComponent = box2dMapper.get(hero);
        if (box2dComponent.body != null) {
            box2dComponent.body.applyLinearImpulse(impulse, box2dComponent.body.getWorldCenter(), true);
        }

        isPressed = true;
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
        ImmutableArray<Entity> entities = getEngine().getEntitiesFor(family);
        if (entities != null && entities.size() > 0) {
            hero = entities.get(0);
        }
    }

}
