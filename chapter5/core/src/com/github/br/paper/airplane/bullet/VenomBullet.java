package com.github.br.paper.airplane.bullet;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.math.Vector2;
import com.github.br.paper.airplane.GameManager;
import com.github.br.paper.airplane.GamePlaySettings;
import com.github.br.paper.airplane.ecs.component.TransformComponent;
import com.github.br.paper.airplane.gameworld.Res;

public class VenomBullet extends BulletStrategy {

    public VenomBullet(GameManager gameManager) {
        super(gameManager);
    }

    @Override
    public boolean isBulletsEnough(short bulletCount) {
        return bulletCount - getGameManager().gameSettings.getGamePlaySettings().getVenomBulletCost() >= 0;
    }

    @Override
    public short reduceBullets(short bulletCount) {
        return (short) (bulletCount - getGameManager().gameSettings.getGamePlaySettings().getVenomBulletCost());
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
                getGameManager().gameEntityFactory.createCircleBullet(
                        engine,
                        bulletX,
                        bulletY,
                        10,
                        heroTransformComponent.degreeAngle,
                        gamePlaySettings.getVenomBulletVelocity(),
                        gamePlaySettings.getVenomBulletDamage(),
                        Res.PARTICLE_VENOM_BULLET_P
                ),
                getGameManager().gameEntityFactory.createCircleBullet(
                        engine,
                        bulletX,
                        bulletY + 21,
                        10,
                        heroTransformComponent.degreeAngle + 90,
                        gamePlaySettings.getVenomBulletVelocity(),
                        gamePlaySettings.getVenomBulletDamage(),
                        Res.PARTICLE_VENOM_BULLET_P
                ),
                getGameManager().gameEntityFactory.createCircleBullet(
                        engine,
                        bulletX,
                        bulletY - 21,
                        10,
                        heroTransformComponent.degreeAngle - 90,
                        gamePlaySettings.getVenomBulletVelocity(),
                        gamePlaySettings.getVenomBulletDamage(),
                        Res.PARTICLE_VENOM_BULLET_P
                )
        };
    }

}
