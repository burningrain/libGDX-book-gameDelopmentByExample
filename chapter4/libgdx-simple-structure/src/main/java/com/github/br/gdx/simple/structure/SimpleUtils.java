package com.github.br.gdx.simple.structure;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

public final class SimpleUtils {

    private final GameSettings gameSettings;

    public SimpleUtils(GameSettings gameSettings) {
        this.gameSettings = gameSettings;
    }

    public float convertUnitsToMetres(float pixels) {
        return pixels / gameSettings.getUnitsPerMeter();
    }

    public float convertMetresToUnits(float metres) {
        return metres * gameSettings.getUnitsPerMeter();
    }

    public Vector2 rotatePointToAngle(float pointX, float pointY, float degreeAngle) {
        float sinT = MathUtils.sinDeg(degreeAngle);
        float cosT = MathUtils.cosDeg(degreeAngle);

        return new Vector2(pointX * cosT - pointY * sinT, pointX * sinT + pointY * cosT);
    }

}
