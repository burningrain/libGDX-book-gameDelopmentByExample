package com.github.br.paper.airplane.ecs.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Shape;

public class Box2dComponent implements Component {

    // TODO подумать, куда лучше приткнуть это все
    public Shape.Type shapeType;

    public BodyDef bodyDef;
    public FixtureDef fixtureDef;
    public Object userData;

    public Body body;

}
