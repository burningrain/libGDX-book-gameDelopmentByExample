package com.github.br.ecs.simple.system;


import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.*;
import com.github.br.ecs.simple.utils.EcsReflectionHelper;
import com.github.br.ecs.simple.component.PhysicsComponent;
import com.github.br.ecs.simple.component.TransformComponent;
import com.github.br.ecs.simple.node.PhysicsNode;
import com.github.br.ecs.simple.physics.Boundary;
import com.github.br.ecs.simple.physics.GroupShape;
import com.github.br.ecs.simple.utils.ViewHelper;

public class PhysicsSystem extends EcsSystem<PhysicsNode> {

    private ShapeRenderer shapeRenderer = new ShapeRenderer();

    public PhysicsSystem() {
        super(PhysicsNode.class);
    }

    // подумать, может эти батчи через прокси сделать, как транзакции
    @Override
    public void update(float delta) {
        if (isDebugMode()) {
            ViewHelper.applyCameraAndViewPort(shapeRenderer);
            shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        }
        for (PhysicsNode physicsNode : nodes) {
            TransformComponent transform = physicsNode.transform;
            PhysicsComponent physics = physicsNode.physics;

            moveNode(transform, physics);
            if (isDebugMode()) {
                drawBoundary(physics.boundary);
                drawDebugShape(physics.boundary.shape, transform.rotation);
            }
        }
        if (isDebugMode()) {
            shapeRenderer.end();
        }
    }

    private void moveNode(TransformComponent transform, PhysicsComponent physics){
        //TODO где ротация???
        physics.movement.add(physics.acceleration);
        transform.position.add(physics.movement);
        updateShapePosition(physics.boundary, transform.position);
    }

    private void updateShapePosition(Boundary boundary, Vector2 vector2) {
        EcsReflectionHelper.setValue(boundary.shape, "x", vector2.x + boundary.getOffset().x);
        EcsReflectionHelper.setValue(boundary.shape, "y", vector2.y + boundary.getOffset().y);
    }

    private void drawBoundary(Boundary boundary){
        shapeRenderer.setColor(Color.LIGHT_GRAY);
        shapeRenderer.line(boundary.getX() - 2, boundary.getY() - 2, boundary.getX() + 2, boundary.getY() + 2);
        shapeRenderer.line(boundary.getX() - 2, boundary.getY() + 2, boundary.getX() + 2, boundary.getY() - 2);
        shapeRenderer.setColor(Color.DARK_GRAY);
        shapeRenderer.rect(boundary.getX(), boundary.getY(), boundary.getWidth(), boundary.getHeight());
    }

    private void drawDebugShape(Shape2D debugShape, float degree) {
        shapeRenderer.setColor(Color.YELLOW);
        float x = EcsReflectionHelper.getValue(debugShape,"x");
        float y = EcsReflectionHelper.getValue(debugShape,"y");
        shapeRenderer.line(x - 2, y - 2, x + 2, y + 2);
        shapeRenderer.line(x - 2, y + 2, x + 2, y - 2);

        shapeRenderer.setColor(Color.GOLD);
        if (debugShape instanceof Circle) {
            Circle circle = (Circle) debugShape;
            shapeRenderer.circle(circle.x, circle.y, circle.radius);
            shapeRenderer.line(circle.x, circle.y,
                    circle.x + circle.radius * MathUtils.cosDeg(degree), circle.y + circle.radius * MathUtils.sinDeg(degree));
        } else if (debugShape instanceof Rectangle) {
            Rectangle rectangle = (Rectangle) debugShape;
            shapeRenderer.rect(rectangle.x, rectangle.y, rectangle.width, rectangle.height);
        } else if(debugShape instanceof GroupShape){
            GroupShape groupShape = (GroupShape) debugShape;
            for(Shape2D shape2D : groupShape.getGlobalPosShapes()){
                drawDebugShape(shape2D, degree);
            }
        } else {
            throw new IllegalArgumentException(String.format("Тип физической формы %s пока не поддерживается",
                    debugShape.getClass().getSimpleName()));
        }
    }



}
