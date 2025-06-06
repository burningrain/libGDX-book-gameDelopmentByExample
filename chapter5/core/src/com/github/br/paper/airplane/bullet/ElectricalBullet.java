package com.github.br.paper.airplane.bullet;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.math.Vector2;
import com.github.br.paper.airplane.GameManager;
import com.github.br.paper.airplane.GamePlaySettings;
import com.github.br.paper.airplane.ecs.component.TransformComponent;
import com.github.br.paper.airplane.gameworld.Res;

public class ElectricalBullet extends BulletStrategy {

    private final Vector2 anchor = new Vector2(30, 0);

    public ElectricalBullet(GameManager gameManager) {
        super(gameManager);
    }

    @Override
    public boolean isBulletsEnough(short bulletCount) {
        return bulletCount - getGameManager().gameSettings.getGamePlaySettings().getElectricalBulletCost() >= 0;
    }

    @Override
    public short reduceBullets(short bulletCount) {
        return (short) (bulletCount - getGameManager().gameSettings.getGamePlaySettings().getElectricalBulletCost());
    }

    @Override
    public Entity[] createBullet(Engine engine, TransformComponent heroTransformComponent) {
        float deltaX = (heroTransformComponent.width / 2 + 22);
        float deltaY = -8;
        // heroTransformComponent.position.x + heroTransformComponent.width / 2 - это центр самолетика
        //todo а может можно просто через нормаль к вектору все это дело посчитать, без синусов и косинусов?
        Vector2 newPosition = getGameManager().utils.rotatePointToAngle(deltaX, deltaY, heroTransformComponent.degreeAngle);
        int bulletX = (int) (heroTransformComponent.position.x + heroTransformComponent.width / 2 + newPosition.x);
        int bulletY = (int) (heroTransformComponent.position.y + heroTransformComponent.height / 2 + newPosition.y);

        GamePlaySettings gamePlaySettings = getGameManager().gameSettings.getGamePlaySettings();
        return new Entity[]{
                getGameManager().gameEntityFactory.createRectangleBullet(
                        engine,
                        bulletX,
                        bulletY + 37,
                        60,
                        5,
                        heroTransformComponent.degreeAngle + 55,
                        gamePlaySettings.getElectricalBulletVelocity(),
                        gamePlaySettings.getElectricalBulletDamage(),
                        Res.Particles.PARTICLE_ELECTRO_BULLET_P,
                        anchor
                ),
                getGameManager().gameEntityFactory.createRectangleBullet(
                        engine,
                        bulletX,
                        bulletY - 37,
                        60,
                        5,
                        heroTransformComponent.degreeAngle - 55,
                        gamePlaySettings.getElectricalBulletVelocity(),
                        gamePlaySettings.getElectricalBulletDamage(),
                        Res.Particles.PARTICLE_ELECTRO_BULLET_P,
                        anchor
                ),
        };
    }

}
