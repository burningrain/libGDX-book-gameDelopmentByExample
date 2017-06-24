package com.packt.flappeebee.model.scripts;

import com.badlogic.gdx.math.MathUtils;
import com.github.br.ecs.simple.engine.EcsScript;
import com.github.br.ecs.simple.system.transform.TransformComponent;

/**
 * Created by user on 24.06.2017.
 */
public class CloudScript extends EcsScript {

    private TransformComponent transform;
    private float velocity;

    @Override
    public void init() {
        transform = getComponent(TransformComponent.class);
        velocity = MathUtils.random(2f, 10f);
    }

    @Override
    public void update(float delta) {
        transform.position.x -= velocity;
        if(transform.position.x <= 0){
            deleteEntity(this.getEntityId());
        }
    }

}
