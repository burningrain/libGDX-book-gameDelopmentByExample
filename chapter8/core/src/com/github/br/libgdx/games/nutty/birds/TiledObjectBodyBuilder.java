package com.github.br.libgdx.games.nutty.birds;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.EllipseMapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Ellipse;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;

import java.util.function.Consumer;

public class TiledObjectBodyBuilder {

    private static final float HALF = 0.5F;


    public static void buildBuildingBodies(TiledMap tiledMap, World world) {
        MapObjects objects = tiledMap.getLayers().get(Constants.PHYSICS_BUILDINGS_LAYER).getObjects();
        for (MapObject object : objects) {
            RectangleMapObject rectangleMapObject = (RectangleMapObject) object;
            PolygonShape rectangle = getRectangle(rectangleMapObject);
            BodyDef bd = new BodyDef();
            bd.type = BodyDef.BodyType.DynamicBody;
            Body body = world.createBody(bd);

            if (rectangleMapObject.getRectangle().width > rectangleMapObject.getRectangle().height) {
                body.setUserData(Constants.HORIZONTAL);
            } else {
                body.setUserData(Constants.VERTICAL);
            }

            body.createFixture(rectangle, 1);
            body.setTransform(getTransformForRectangle(rectangleMapObject.getRectangle()), 0);

            rectangle.dispose();
        }
    }

    private static PolygonShape getRectangle(RectangleMapObject rectangleObject) {
        Rectangle rectangle = rectangleObject.getRectangle();
        PolygonShape polygon = new PolygonShape();
        polygon.setAsBox(
                rectangle.width * HALF / Constants.PIXELS_PER_TILE,
                rectangle.height * HALF / Constants.PIXELS_PER_TILE
        );
        return polygon;
    }

    private static Vector2 getTransformForRectangle(Rectangle rectangle) {
        return new Vector2(
                (rectangle.x + (rectangle.width * HALF)) / Constants.PIXELS_PER_TILE,
                (rectangle.y + (rectangle.height * HALF)) / Constants.PIXELS_PER_TILE
        );
    }

    public static void buildFloorBodies(TiledMap tiledMap, World world) {
        MapObjects objects = tiledMap.getLayers().get(Constants.PHYSICS_FLOOR_LAYER).getObjects();
        for (MapObject object : objects) {
            RectangleMapObject rectangleMapObject = (RectangleMapObject) object;
            PolygonShape rectangle = getRectangle(rectangleMapObject);
            BodyDef bd = new BodyDef();
            bd.type = BodyDef.BodyType.StaticBody;
            Body body = world.createBody(bd);

            body.setUserData(Constants.FLOOR);
            body.createFixture(rectangle, 1);
            body.setTransform(getTransformForRectangle(rectangleMapObject.getRectangle()), 0);

            rectangle.dispose();
        }
    }

    private static CircleShape getCircle(EllipseMapObject ellipseObject) {
        Ellipse ellipse = ellipseObject.getEllipse();
        CircleShape circleShape = new CircleShape();
        circleShape.setRadius(ellipse.width * HALF / Constants.PIXELS_PER_TILE);

        return circleShape;
    }

    public static void buildBirdBodies(TiledMap tiledMap, World world) {
        MapObjects objects = tiledMap.getLayers().get(Constants.PHYSICS_BIRDS_LAYER).getObjects();
        for (MapObject object : objects) {
            EllipseMapObject ellipseMapObject = (EllipseMapObject) object;
            CircleShape circle = getCircle(ellipseMapObject);
            BodyDef bd = new BodyDef();
            bd.type = BodyDef.BodyType.DynamicBody;
            Body body = world.createBody(bd);
            Fixture fixture = body.createFixture(circle, 1);
            fixture.setUserData(Constants.ENEMY);
            body.setUserData(Constants.ENEMY);

            Ellipse ellipse = ellipseMapObject.getEllipse();
            body.setTransform(
                    new Vector2(
                            (ellipse.x + ellipse.width * HALF) / Constants.PIXELS_PER_TILE,
                            (ellipse.y + ellipse.height * HALF) / Constants.PIXELS_PER_TILE
                    ),
                    0
            );

            circle.dispose();
        }
    }

    public static void createBullet(World world, Vector2 firingPosition, float angle, float distance, Consumer<Body> consumer) {
        CircleShape circleShape = new CircleShape();
        circleShape.setRadius(0.5f);

        BodyDef bd = new BodyDef();
        bd.type = BodyDef.BodyType.DynamicBody;
        Body body = world.createBody(bd);
        body.setTransform(
                new Vector2(
                        Utils.convertUnitsToMetres(firingPosition.x),
                        Utils.convertUnitsToMetres(firingPosition.y)
                ),
                0
        );

        Fixture fixture = body.createFixture(circleShape, 1);
        fixture.setUserData(Constants.ACORN_PNG);
        body.setUserData(Constants.ACORN_PNG);
        consumer.accept(body);

        float velX = Math.abs((Constants.MAX_STRENGTH * -MathUtils.cos(angle) * (distance / 100f)));
        float velY = Math.abs((Constants.MAX_STRENGTH * -MathUtils.sin(angle) * (distance / 100f)));
        body.setLinearVelocity(velX, velY);

        circleShape.dispose();
    }

}
