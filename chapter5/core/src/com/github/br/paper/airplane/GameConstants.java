package com.github.br.paper.airplane;

public interface GameConstants {

    int PROGRESSBAR_WIDTH = 100;
    int PROGRESSBAR_HEIGHT = 25;

    int VIRTUAL_SCREEN_WIDTH = 960;
    int VIRTUAL_SCREEN_HEIGHT = 544;

    float UNITS_PER_METER = 32f;

    // box2d
    float UNIT_WIDTH = VIRTUAL_SCREEN_WIDTH / UNITS_PER_METER;
    float UNIT_HEIGHT = VIRTUAL_SCREEN_HEIGHT / UNITS_PER_METER;

    float PIXELS_PER_TILE = 32F; // FIXME поправить

}
