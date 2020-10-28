package com.packt.flappeebee.model.scripts;

import com.badlogic.gdx.math.MathUtils;
import com.github.br.ecs.simple.engine.EcsScript;
import com.github.br.ecs.simple.system.physics.PhysicsComponent;
import com.github.br.ecs.simple.system.transform.TransformComponent;
import com.packt.flappeebee.model.GameConstants;
import com.github.br.ecs.simple.utils.ViewHelper;

/**
 * Created by user on 26.03.2017.
 */
public class FlappeeScript extends EcsScript {

    private TransformComponent transform;
    private PhysicsComponent physics;

    @Override
    public void init() {
        transform = getComponent(TransformComponent.class);
        physics = getComponent(PhysicsComponent.class);
    }

    @Override
    public void dispose() {
        transform = null;
        physics = null;
    }

    @Override
    public void update(float delta) {
        transform.rotation +=1;
        physics.movement.add(0, MathUtils.random(0f, 0.4f)); //todo хак, пока не допилю физ. систему более-менее
        blockFlappeeLeavingTheWorld();

    }

    private void blockFlappeeLeavingTheWorld() {
        if(transform.position.y <= 0){
            deleteEntity(this.getEntityId());
        }
    }


}
