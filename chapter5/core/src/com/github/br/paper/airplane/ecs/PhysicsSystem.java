package com.github.br.paper.airplane.ecs;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.github.br.paper.airplane.GameSettings;
import com.github.br.paper.airplane.Utils;
import com.github.br.paper.airplane.ecs.component.Box2dComponent;
import com.github.br.paper.airplane.ecs.component.TransformComponent;

public class PhysicsSystem extends EntitySystem implements ContactListener {

    private final Family family = Family.all(TransformComponent.class, Box2dComponent.class).get();
    private final ComponentMapper<TransformComponent> transformMapper = ComponentMapper.getFor(TransformComponent.class);
    private final ComponentMapper<Box2dComponent> box2dMapper = ComponentMapper.getFor(Box2dComponent.class);

    private final World world;
    private final Utils utils;

    private final GameSettings gameSettings;

    public PhysicsSystem(GameSettings gameSettings, World world, Utils utils) {
        this.gameSettings = gameSettings;
        this.world = world;
        this.utils = utils;
    }

    @Override
    public void update (float deltaTime) {
        world.step(1 / 60f, 6, 2);

        ImmutableArray<Entity> entities = getEngine().getEntitiesFor(family);
        for (Entity entity : entities) {
            TransformComponent transformComponent = transformMapper.get(entity);
            Box2dComponent box2dComponent = box2dMapper.get(entity);
            if (box2dComponent.body == null) {
                box2dComponent.body = createBody(this.world, box2dComponent, transformComponent);
            }

            Transform transform = box2dComponent.body.getTransform();
            Vector2 position = transform.getPosition();
            transformComponent.position.x = utils.convertMetresToUnits(position.x) - transformComponent.width / 2f;
            transformComponent.position.y = utils.convertMetresToUnits(position.y) - transformComponent.height / 2f;
            transformComponent.angle = MathUtils.radiansToDegrees * box2dComponent.body.getAngle();
        }
    }

    private Body createBody(World world, Box2dComponent box2dComponent, TransformComponent transformComponent) {
        Body body = world.createBody(box2dComponent.bodyDef);
        // https://stackoverflow.com/questions/53987670/libgdx-box2d-body-is-being-place-at-the-wrong-y-coordinate
        // о конвертациях

        Vector2 position = new Vector2(
                utils.convertUnitsToMetres(transformComponent.position.x + transformComponent.width / 2f),
                utils.convertUnitsToMetres(transformComponent.position.y + transformComponent.height / 2f)
        );
        body.setTransform(position, MathUtils.degreesToRadians * transformComponent.angle);
        Shape shape = createShape(
                box2dComponent,
                utils.convertUnitsToMetres(transformComponent.width),
                utils.convertUnitsToMetres(transformComponent.height)
        );
        Fixture fixture = body.createFixture(shape, box2dComponent.density);
        body.setUserData(box2dComponent.userData);

        shape.dispose();
        return body;
    }

    private Shape createShape(Box2dComponent box2dComponent, float width, float height) {
        Shape.Type shapeType = box2dComponent.shapeType;
        if (Shape.Type.Circle == shapeType) {
            CircleShape circleShape = new CircleShape();
            circleShape.setRadius(utils.convertUnitsToMetres(box2dComponent.radius)); //todo грязно
            return circleShape;
        } else if (Shape.Type.Polygon == shapeType) {
            PolygonShape polygonShape = new PolygonShape();
            polygonShape.setAsBox(width / 2f,height / 2f);
            return polygonShape;
        }

        throw new IllegalArgumentException();
    }

    @Override
    public void beginContact(Contact contact) {

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
