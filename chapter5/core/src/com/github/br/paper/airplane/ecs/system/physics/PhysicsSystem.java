package com.github.br.paper.airplane.ecs.system.physics;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.github.br.paper.airplane.GameSettings;
import com.github.br.paper.airplane.Utils;
import com.github.br.paper.airplane.ecs.component.*;

public class PhysicsSystem extends EntitySystem {

    private boolean isDrawDebugBox2d;
    private final Box2DDebugRenderer debugRenderer;
    private final OrthographicCamera box2dCam;

    private final ContactListenerImpl contactListenerImpl;
    private final Family family = Family.all(TransformComponent.class, Box2dComponent.class).get();
    private final Mappers mappers;

    private final World world;
    private final Utils utils;

    private final GameSettings gameSettings;

    private final PhysicsUtils physicsUtils;

    private float accumulator = 0;

    public boolean isDrawDebugBox2d() {
        return isDrawDebugBox2d;
    }

    public PhysicsSystem setDrawDebugBox2d(boolean drawDebugBox2d) {
        isDrawDebugBox2d = drawDebugBox2d;
        return this;
    }

    public PhysicsSystem(GameSettings gameSettings, Utils utils, Mappers mappers) {
        this.gameSettings = gameSettings;
        this.world = new World(new Vector2(0, -3f), true);
        this.utils = utils;
        this.mappers = mappers;
        this.physicsUtils = new PhysicsUtils(utils, world, mappers);

        this.contactListenerImpl = new ContactListenerImpl(physicsUtils, world, mappers);
        this.world.setContactListener(this.contactListenerImpl);

        this.debugRenderer = new Box2DDebugRenderer();
        this.box2dCam = new OrthographicCamera(gameSettings.getUnitWidth(), gameSettings.getUnitHeight());
    }

    public void drawDebugBox2d() {
        box2dCam.position.set(gameSettings.getUnitWidth() / 2, gameSettings.getUnitHeight() / 2, 0);
        box2dCam.update();
        debugRenderer.render(world, box2dCam.combined);
    }

    private void doPhysicsStep(float deltaTime, float timeStep, int velocityIter, int positionIter) {
        // fixed time step
        // max frame time to avoid spiral of death (on slow devices)
        float frameTime = Math.min(deltaTime, 0.25f);
        accumulator += frameTime;
        while (accumulator >= timeStep) {
            world.step(timeStep, velocityIter, positionIter);
            accumulator -= timeStep;
        }
    }

    @Override
    public void update(float deltaTime) {
        doPhysicsStep(deltaTime, gameSettings.getTimeStep(), gameSettings.getVelocityIterations(), gameSettings.getPositionIterations());

        ImmutableArray<Entity> entities = getEngine().getEntitiesFor(family);
        for (Entity entity : entities) {
            TransformComponent transformComponent = mappers.transformMapper.get(entity);
            Box2dComponent box2dComponent = mappers.box2dMapper.get(entity);
            if (box2dComponent.body == null) {
                //TODO обязательно вынести создание тела из системы в генератор сущностей, а то создает логическую кривоту и плодит ифы
                box2dComponent.body = physicsUtils.createBody(this.world, entity, box2dComponent, transformComponent);
                if (box2dComponent.isGravityOff) {
                    box2dComponent.body.setGravityScale(0);
                }
            }

            Transform transform = box2dComponent.body.getTransform();
            Vector2 position = transform.getPosition();
            transformComponent.position.x = utils.convertMetresToUnits(position.x) - transformComponent.width / 2f;
            transformComponent.position.y = utils.convertMetresToUnits(position.y) - transformComponent.height / 2f;
            transformComponent.degreeAngle = MathUtils.radiansToDegrees * box2dComponent.body.getAngle();
        }

    }

    public PhysicsUtils getPhysicsUtils() {
        return physicsUtils;
    }

}
