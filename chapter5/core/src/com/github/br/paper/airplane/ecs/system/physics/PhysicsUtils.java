package com.github.br.paper.airplane.ecs.system.physics;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.github.br.paper.airplane.Utils;
import com.github.br.paper.airplane.ecs.component.*;

public final class PhysicsUtils {

    private final Utils utils;
    private final World world;
    private final Mappers mappers;


    public PhysicsUtils(Utils utils, World world, Mappers mappers) {
        this.utils = utils;
        this.world = world;
        this.mappers = mappers;
    }

    public Body createBody(World world, Entity entity, Box2dComponent box2dComponent, TransformComponent transformComponent) {
        Body body = world.createBody(box2dComponent.bodyDef);
        // https://stackoverflow.com/questions/53987670/libgdx-box2d-body-is-being-place-at-the-wrong-y-coordinate
        // о конвертациях

        Vector2 position = new Vector2(
                utils.convertUnitsToMetres(transformComponent.position.x + transformComponent.width / 2f),
                utils.convertUnitsToMetres(transformComponent.position.y + transformComponent.height / 2f)
        );
        body.setTransform(position, MathUtils.degreesToRadians * transformComponent.degreeAngle);
        Shape shape = createShape(
                box2dComponent,
                transformComponent
        );

        box2dComponent.fixtureDef.shape = shape;
        Fixture fixture = body.createFixture(box2dComponent.fixtureDef);
        body.setUserData(entity);

        shape.dispose();
        return body;
    }

    public Shape createShape(Box2dComponent box2dComponent, TransformComponent transformComponent) {
        Shape.Type shapeType = box2dComponent.shapeType;
        if (Shape.Type.Circle == shapeType) {
            CircleShape circleShape = new CircleShape();
            circleShape.setRadius(utils.convertUnitsToMetres(transformComponent.radius)); //todo грязно
            return circleShape;
        } else if (Shape.Type.Polygon == shapeType) {
            PolygonShape polygonShape = new PolygonShape();
            polygonShape.setAsBox(
                    utils.convertUnitsToMetres(transformComponent.width) / 2f,
                    utils.convertUnitsToMetres(transformComponent.height) / 2f
            );
            return polygonShape;
        }

        throw new IllegalArgumentException();
    }

    public void destroyObject(Box2dComponent box2dComponent) {
        world.destroyBody(box2dComponent.body);
    }

    public Script[] getScripts(Fixture fixture) {
        ScriptComponent scriptComponent = getScriptComponent(fixture);
        if (scriptComponent == null) {
            return null;
        }
        return scriptComponent.scripts;
    }

    public ScriptComponent getScriptComponent(Fixture fixture) {
        Body body = fixture.getBody();
        Entity entity = (Entity) body.getUserData();
        if (entity == null) {
            return null;
        }
        return mappers.scriptMapper.get(entity);
    }

}
