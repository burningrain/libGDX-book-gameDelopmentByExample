package com.github.br.paper.airplane.ecs.system;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.math.Vector2;
import com.github.br.paper.airplane.ecs.component.Box2dComponent;
import com.github.br.paper.airplane.ecs.component.DeleteComponent;
import com.github.br.paper.airplane.ecs.component.TransformComponent;
import com.github.br.paper.airplane.ecs.component.WallComponent;

public class WallSystem extends IteratingSystem {

    private final ComponentMapper<WallComponent> wallMapper = ComponentMapper.getFor(WallComponent.class);
    private final ComponentMapper<TransformComponent> transformMapper = ComponentMapper.getFor(TransformComponent.class);
    private final ComponentMapper<Box2dComponent> box2dMapper = ComponentMapper.getFor(Box2dComponent.class);

    private static final Vector2 velocity = new Vector2(-5, 0);

    public WallSystem() {
        super(Family.all(TransformComponent.class, Box2dComponent.class, WallComponent.class).get());
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        WallComponent wallComponent = wallMapper.get(entity);
        // если только что создали стену, то присваиваем ей скорость.
        // Это не делается при создании, так как body не существует. Кривота, конечно.
        if (wallComponent.isNew) {
            Box2dComponent box2dComponent = box2dMapper.get(entity);
            if (box2dComponent.body != null) {
                box2dComponent.body.setLinearVelocity(velocity);
                wallComponent.isNew = false;
            }
        }

        TransformComponent transformComponent = transformMapper.get(entity);
        if (transformComponent.position.x + transformComponent.width < -5) {
            entity.add(new DeleteComponent());
        }
    }

}
