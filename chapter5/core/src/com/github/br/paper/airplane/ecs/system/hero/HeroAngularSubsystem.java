package com.github.br.paper.airplane.ecs.system.hero;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.math.MathUtils;
import com.github.br.paper.airplane.GameManager;
import com.github.br.paper.airplane.GamePlaySettings;
import com.github.br.paper.airplane.ecs.component.*;

public class HeroAngularSubsystem implements HeroSybSystem {


    public void processHero(
            Engine engine,
            GameManager gameManager,
            Mappers mappers,
            Entity entity
    ) {
        GamePlaySettings gamePlaySettings = gameManager.gameSettings.getGamePlaySettings();
        Box2dComponent box2dComponent = mappers.box2dMapper.get(entity);
        InputComponent inputComponent = mappers.inputMapper.get(entity);
        TransformComponent transformComponent = mappers.transformMapper.get(entity);
        HeroComponent heroComponent = mappers.heroMapper.get(entity);

        // если не стреляем, значит поднимаем самолетик вверх
        if (inputComponent.isClick) {
            box2dComponent.body.applyAngularImpulse(-gamePlaySettings.getAngularImpulse(), true);
        }

        box2dComponent.body.applyTorque(gamePlaySettings.getDefaultTorque(), true);
        float angle = box2dComponent.body.getAngle() * MathUtils.radiansToDegrees;
        if (angle >= gamePlaySettings.getEndAngle() || angle <= -gamePlaySettings.getEndAngle()) {
            inputComponent.turnOff();
        }
    }

}
