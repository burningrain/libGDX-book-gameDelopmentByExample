package com.github.br.paper.airplane;

public interface GameConstants {

    int PROGRESSBAR_WIDTH = 600;
    int PROGRESSBAR_HEIGHT = 50;

    int VIRTUAL_SCREEN_WIDTH = 1920;
    int VIRTUAL_SCREEN_HEIGHT = 1080;

    float UNITS_PER_METER = 128;

    // box2d
    float UNIT_WIDTH = VIRTUAL_SCREEN_WIDTH / UNITS_PER_METER;
    float UNIT_HEIGHT = VIRTUAL_SCREEN_HEIGHT / UNITS_PER_METER;

}
