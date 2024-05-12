package com.github.br.paper.airplane;

public final class Utils {

    private Utils() {
    }

    public static float convertUnitsToMetres(float pixels) {
        return pixels / GameConstants.UNITS_PER_METER;
    }

    public static float convertMetresToUnits(float metres) {
        return metres * GameConstants.UNITS_PER_METER;
    }

}
