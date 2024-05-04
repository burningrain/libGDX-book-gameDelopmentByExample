package com.github.br.libgdx.games.nutty.birds;

public final class Utils {

    private Utils() {
    }

    public static float convertUnitsToMetres(float pixels) {
        return pixels / Constants.UNITS_PER_METER;
    }

    public static float convertMetresToUnits(float metres) {
        return metres * Constants.UNITS_PER_METER;
    }

}
