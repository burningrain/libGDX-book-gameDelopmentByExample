package com.packt.flappeebee.model.scripts;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.MathUtils;
import com.github.br.ecs.simple.engine.EcsScript;
import com.github.br.ecs.simple.system.physics.PhysicsComponent;
import com.github.br.ecs.simple.system.render.RendererComponent;
import com.github.br.ecs.simple.system.transform.TransformComponent;
import com.github.br.simple.input.controller.ControllerProxy;
import com.packt.flappeebee.model.GameConstants;
import com.packt.flappeebee.model.LayerEnum;

/**
 * Created by user on 16.04.2017.
 */
public class CrabScript extends EcsScript {

    public static final int VELOCITY = 2;
    private TransformComponent transform;
    private PhysicsComponent physics;

    private RendererComponent rendererComponent;

    @Override
    public void init() {
        transform = getComponent(TransformComponent.class);
        physics = getComponent(PhysicsComponent.class);
        rendererComponent = getComponent(RendererComponent.class);
    }

    @Override
    public void dispose() {
        transform = null;
        physics = null;
    }

    @Override
    public void update(float delta) {
        blockFlappeeLeavingTheWorld();

        // TODO должен быть маппинг кнопок на действия, а в условиях проверять уже сами действия случились или нет, в не кнопки
        ControllerProxy controller = ControllerProxy.INSTANCE;
        if (Gdx.input.isKeyPressed(Input.Keys.SPACE) || controller.getButton(controller.getMapping().buttonA)) {
            flyUp(delta);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.D) || controller.getAxis(controller.getMapping().axisLeftX) > 0) {
            physics.movement.x = VELOCITY;
        } else if (Gdx.input.isKeyPressed(Input.Keys.A) || controller.getAxis(controller.getMapping().axisLeftX) < 0) {
            physics.movement.x = -VELOCITY;
        } else {
            physics.movement.x = 0;
        }

        if (Gdx.input.isKeyPressed(Input.Keys.F) || controller.getButton(controller.getMapping().buttonX)) {
            rendererComponent.newLayerTitle = LayerEnum.FRONT_EFFECTS.name();
        }
        if (Gdx.input.isKeyPressed(Input.Keys.G) || controller.getButton(controller.getMapping().buttonY)) {
            rendererComponent.newLayerTitle = LayerEnum.MAIN_LAYER.name();
        }
    }

    private void blockFlappeeLeavingTheWorld() {
        int height = Gdx.graphics.getHeight();

        if (transform.position.y <= 0 || transform.position.y >= height) {
            physics.movement.y = 0;
        }
        transform.position.y = MathUtils.clamp(transform.position.y, 0, height);
    }

    public void flyUp(float delta) {
        physics.movement.add(GameConstants.CRAB_DIVE_ACCEL_ACCEL.cpy().scl(delta * delta * 360f / 2f));
    }

}
