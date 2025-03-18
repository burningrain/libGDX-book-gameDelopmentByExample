package com.github.br.ecs.simple.system.physics;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Shape2D;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.IdentityMap;
import com.badlogic.gdx.utils.IntMap;
import com.github.br.ecs.simple.engine.EcsEntity;
import com.github.br.ecs.simple.engine.debug.DebugDataContainer;
import com.github.br.ecs.simple.engine.debug.DebugSystem;
import com.github.br.ecs.simple.engine.debug.data.CircleData;
import com.github.br.ecs.simple.engine.debug.data.DebugData;
import com.github.br.ecs.simple.engine.debug.data.PointData;
import com.github.br.ecs.simple.engine.debug.data.RectangleData;
import com.github.br.ecs.simple.system.transform.TransformComponent;

public class PhysicsSystem extends DebugSystem {

    private static final float MS_PER_UPDATE = 0.006f;
    private IdentityMap<Class, ShapePosUpdater> shapeUpdaters;

    private static int variant = 1;

    private float previous = 0;
    private float lag = 0;

    public PhysicsSystem() {
        super(TransformComponent.class, PhysicsComponent.class);

        shapeUpdaters = new IdentityMap<Class, ShapePosUpdater>();
        shapeUpdaters.put(Circle.class, new CirclePosUpdater());
        shapeUpdaters.put(Rectangle.class, new RectanglePosUpdater());
    }

    // подумать, может эти батчи через прокси сделать, как транзакции
    @Override
    public void update(float delta, IntMap.Values<EcsEntity> entities, DebugDataContainer debugDataContainer) {
        for (EcsEntity entity : entities) {
            TransformComponent transform = entity.getComponent(TransformComponent.class);
            PhysicsComponent physics = entity.getComponent(PhysicsComponent.class);

            switch (variant) {
                case 1:
                    moveNode1(transform, physics);
                    break;
                case 2:
                    moveNode2(transform, physics);
                    break;
                case 3:
                    moveNode3(transform, physics);
                    break;
            }
            shapeUpdaters.get(physics.shape.getClass()).update(physics.shape, transform.position);

            if (isDebugMode()) {
                //fillDebugData(debugDataContainer, physics.boundary.shape, transform); todo
            }
        }
    }

    private void fillDebugData(DebugDataContainer debugDataContainer, Shape2D shape, TransformComponent transform) {
        DebugData debugData = null;
        PointData pointData = null;
        Vector2 position = transform.position;
        if (Circle.class == shape.getClass()) {
            Circle circle = (Circle) shape;
            debugData = new CircleData(position.x, position.y, circle.radius, transform.rotation);
            pointData = new PointData(position.x, position.y);
        } else if (Rectangle.class == shape.getClass()) {
            Rectangle rect = (Rectangle) shape;
            debugData = new RectangleData(position.x, position.y, rect.width, rect.height);
            pointData = new PointData(position.x, position.y);
        }
        debugDataContainer.put(debugData);
        debugDataContainer.put(pointData);
    }

    private void moveNode1(TransformComponent transform, PhysicsComponent physics) {
        //TODO где ротация???
        physics.movement.add(physics.acceleration);
        transform.position.add(physics.movement);
    }

    private void moveNode2(TransformComponent transform, PhysicsComponent physics) {
        float deltaTime = Gdx.graphics.getDeltaTime();
        physics.movement.add(physics.acceleration.cpy().scl(deltaTime * deltaTime / 2f * 60f));
        transform.position.add(physics.movement.cpy().scl(deltaTime * 60f));
    }

    private void moveNode3(TransformComponent transform, PhysicsComponent physics) {
        float current = Gdx.graphics.getDeltaTime();
        float elapsed = current - previous;
        previous = current;
        lag += elapsed;

        while(lag >= MS_PER_UPDATE) {
            float deltaTime = Gdx.graphics.getDeltaTime();
            physics.movement.add(physics.acceleration.cpy().scl(deltaTime * deltaTime / 2 * 10));
            transform.position.add(physics.movement.cpy().scl(deltaTime * 10));
        }
        lag -= MS_PER_UPDATE;
    }

    public void changeStrategy(int variant) {
        this.variant = variant;
    }

    public static int getVariant() {
        return variant;
    }

}
