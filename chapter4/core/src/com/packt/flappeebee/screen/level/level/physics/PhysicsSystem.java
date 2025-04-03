package com.packt.flappeebee.screen.level.level.physics;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.*;
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

    private IdentityMap<Class, ShapePosUpdater> shapeUpdaters;
    private final Vector2 gravity = new Vector2(0, -10);

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

            transform.position.add(gravity.cpy().scl(delta));
            transform.position.add(physics.velocity.scl(delta));
            blockGround(transform, physics);
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

    private void blockGround(TransformComponent transform, PhysicsComponent physics) {
        int height = Gdx.graphics.getHeight();

        if (transform.position.y <= 0 || transform.position.y >= height) {
            physics.velocity.y = 0;
        }
        transform.position.y = MathUtils.clamp(transform.position.y, 0, height);
    }

}
