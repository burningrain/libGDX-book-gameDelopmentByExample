package com.github.br.paper.airplane;

public final class Utils {

    private final GameSettings gameSettings;

    public Utils(GameSettings gameSettings) {
        this.gameSettings = gameSettings;
    }

    public float convertUnitsToMetres(float pixels) {
        return pixels / gameSettings.getUnitsPerMeter();
    }

    public float convertMetresToUnits(float metres) {
        return metres * gameSettings.getUnitsPerMeter();
    }

}
