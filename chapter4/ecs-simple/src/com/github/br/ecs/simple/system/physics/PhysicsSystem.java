package com.github.br.ecs.simple.system.physics;


import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Shape2D;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.IdentityMap;
import com.badlogic.gdx.utils.IntMap;
import com.github.br.ecs.simple.engine.debug.DebugSystem;
import com.github.br.ecs.simple.engine.debug.DebugDataContainer;
import com.github.br.ecs.simple.engine.debug.data.CircleData;
import com.github.br.ecs.simple.engine.debug.data.DebugData;
import com.github.br.ecs.simple.engine.debug.data.PointData;
import com.github.br.ecs.simple.engine.debug.data.RectangleData;
import com.github.br.ecs.simple.system.transform.TransformComponent;

import java.util.Collection;

public class PhysicsSystem extends DebugSystem<PhysicsNode> {

    private IdentityMap<Class, ShapePosUpdater> shapeUpdaters;

    public PhysicsSystem() {
        super(PhysicsNode.class);

        shapeUpdaters = new IdentityMap<Class, ShapePosUpdater>();
        shapeUpdaters.put(Circle.class, new CirclePosUpdater());
        shapeUpdaters.put(Rectangle.class, new RectanglePosUpdater());
    }

    // подумать, может эти батчи через прокси сделать, как транзакции
    @Override
    public void update(float delta, IntMap.Values<PhysicsNode> nodes, DebugDataContainer debugDataContainer) {
        for (PhysicsNode physicsNode : nodes) {
            TransformComponent transform = physicsNode.transform;
            PhysicsComponent physics = physicsNode.physics;

            moveNode(transform, physics);
            if (isDebugMode()) {
                //fillDebugData(debugDataContainer, physics.boundary.shape, transform); todo
            }
        }
    }

    private void fillDebugData(DebugDataContainer debugDataContainer, Shape2D shape, TransformComponent transform) {
        DebugData debugData = null;
        PointData pointData = null;
        Vector2 position = transform.position;
        if(Circle.class == shape.getClass()) {
            Circle circle = (Circle) shape;
            debugData = new CircleData(position.x, position.y, circle.radius, transform.rotation);
            pointData = new PointData(position.x, position.y);
        } else if(Rectangle.class == shape.getClass()) {
            Rectangle rect = (Rectangle) shape;
            debugData = new RectangleData(position.x, position.y, rect.width, rect.height);
            pointData = new PointData(position.x, position.y);
        }
        debugDataContainer.put(debugData);
        debugDataContainer.put(pointData);
    }

    private void moveNode(TransformComponent transform, PhysicsComponent physics){
        //TODO где ротация???
        physics.movement.add(physics.acceleration);
        transform.position.add(physics.movement);
        shapeUpdaters.get(physics.shape.getClass()).update(physics.shape, transform.position);
    }

}
