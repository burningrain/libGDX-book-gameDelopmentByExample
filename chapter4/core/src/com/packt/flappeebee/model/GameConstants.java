package com.packt.flappeebee.model;


import com.badlogic.gdx.math.Vector2;
import com.github.br.ecs.simple.utils.ViewHelper;

public interface GameConstants {

    Vector2 FLAPPEE_START_POS = new Vector2(ViewHelper.WORLD_WIDTH / 4, ViewHelper.WORLD_HEIGHT / 2);
    Vector2 DIVE_ACCEL = new Vector2(0f, -0.30F);
    Vector2 FLY_ACCEL = new Vector2(0f, 1F);

    Vector2 CRAB_DIVE_ACCEL = new Vector2(0f, -0.10F);
    Vector2 CRAB_DIVE_ACCEL_ACCEL = new Vector2(0f, 0.4F);

}
