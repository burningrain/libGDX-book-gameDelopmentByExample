package com.github.br.paper.airplane.ecs.system;

import com.badlogic.ashley.core.ComponentMapper;
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

public class PhysicsSystem extends EntitySystem implements ContactListener {

    private boolean isDrawDebugBox2d;
    private Box2DDebugRenderer debugRenderer;
    private OrthographicCamera box2dCam;

    private final Family family = Family.all(TransformComponent.class, Box2dComponent.class).get();
    private final ComponentMapper<TransformComponent> transformMapper = ComponentMapper.getFor(TransformComponent.class);
    private final ComponentMapper<Box2dComponent> box2dMapper = ComponentMapper.getFor(Box2dComponent.class);
    private final ComponentMapper<DeleteComponent> deleteMapper = ComponentMapper.getFor(DeleteComponent.class);
    private final ComponentMapper<ScriptComponent> scriptMapper = ComponentMapper.getFor(ScriptComponent.class);

    private final World world;
    private final Utils utils;

    private final GameSettings gameSettings;

    private float accumulator = 0;

    public boolean isDrawDebugBox2d() {
        return isDrawDebugBox2d;
    }

    public PhysicsSystem setDrawDebugBox2d(boolean drawDebugBox2d) {
        isDrawDebugBox2d = drawDebugBox2d;
        return this;
    }

    public PhysicsSystem(GameSettings gameSettings, Utils utils) {
        this.gameSettings = gameSettings;
        this.world = new World(new Vector2(0, -3f), true);
        this.utils = utils;

        debugRenderer = new Box2DDebugRenderer();
        box2dCam = new OrthographicCamera(gameSettings.getUnitWidth(), gameSettings.getUnitHeight());
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
    public void update (float deltaTime) {
        doPhysicsStep(deltaTime, gameSettings.getTimeStep(), gameSettings.getVelocityIterations(), gameSettings.getPositionIterations());

        ImmutableArray<Entity> entities = getEngine().getEntitiesFor(family);
        for (Entity entity : entities) {
            TransformComponent transformComponent = transformMapper.get(entity);
            Box2dComponent box2dComponent = box2dMapper.get(entity);
            if (box2dComponent.body == null) {
                box2dComponent.body = createBody(this.world, entity, box2dComponent, transformComponent);
            }

            DeleteComponent deleteComponent = deleteMapper.get(entity);
            if (deleteComponent != null) {
                world.destroyBody(box2dComponent.body);
                return;
            }

            Transform transform = box2dComponent.body.getTransform();
            Vector2 position = transform.getPosition();
            transformComponent.position.x = utils.convertMetresToUnits(position.x) - transformComponent.width / 2f;
            transformComponent.position.y = utils.convertMetresToUnits(position.y) - transformComponent.height / 2f;
            transformComponent.angle = MathUtils.radiansToDegrees * box2dComponent.body.getAngle();
        }


    }

    //TODO обязательно вынести создание тела из системы в генератор сущностей, а то создает логическую кривоту и плодит ифы
    private Body createBody(World world, Entity entity, Box2dComponent box2dComponent, TransformComponent transformComponent) {
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
                transformComponent
        );

        box2dComponent.fixtureDef.shape = shape;
        Fixture fixture = body.createFixture(box2dComponent.fixtureDef);
        body.setUserData(entity);

        shape.dispose();
        return body;
    }

    private Shape createShape(Box2dComponent box2dComponent, TransformComponent transformComponent) {
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

    @Override
    public void beginContact(Contact contact) {
        Script[] scriptsA = getScripts(contact.getFixtureA());
        if (scriptsA != null) {
            for (Script script : scriptsA) {
                script.beginContact(contact);
            }
        }

        Script[] scriptsB = getScripts(contact.getFixtureB());
        if (scriptsB != null) {
            for (Script script : scriptsB) {
                script.beginContact(contact);
            }
        }
    }

    @Override
    public void endContact(Contact contact) {
        Script[] scriptsA = getScripts(contact.getFixtureA());
        if (scriptsA != null) {
            for (Script script : scriptsA) {
                script.endContact(contact);
            }
        }

        Script[] scriptsB = getScripts(contact.getFixtureB());
        if (scriptsB != null) {
            for (Script script : scriptsB) {
                script.endContact(contact);
            }
        }
    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {
        Script[] scriptsA = getScripts(contact.getFixtureA());
        if (scriptsA != null) {
            for (Script script : scriptsA) {
                script.preSolve(contact, oldManifold);
            }
        }

        Script[] scriptsB = getScripts(contact.getFixtureB());
        if (scriptsB != null) {
            for (Script script : scriptsB) {
                script.preSolve(contact, oldManifold);
            }
        }
    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {
        Script[] scriptsA = getScripts(contact.getFixtureA());
        if (scriptsA != null) {
            for (Script script : scriptsA) {
                script.postSolve(contact, impulse);
            }
        }

        Script[] scriptsB = getScripts(contact.getFixtureB());
        if (scriptsB != null) {
            for (Script script : scriptsB) {
                script.postSolve(contact, impulse);
            }
        }
    }

    private Script[] getScripts(Fixture fixture) {
        ScriptComponent scriptComponent = getScriptComponent(fixture);
        if (scriptComponent == null) {
            return null;
        }
        return scriptComponent.scripts;
    }

    private ScriptComponent getScriptComponent(Fixture fixture) {
        Body body = fixture.getBody();
        Entity entity = (Entity) body.getUserData();
        if (entity == null) {
            return null;
        }
        return scriptMapper.get(entity);
    }

}
