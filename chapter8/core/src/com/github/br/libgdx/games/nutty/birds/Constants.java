package com.github.br.libgdx.games.nutty.birds;

import com.badlogic.gdx.math.MathUtils;

public interface Constants {

    float PROGRESS_BAR_WIDTH = 100;
    float PROGRESS_BAR_HEIGHT = 25;

    float WORLD_WIDTH = 960;
    float WORLD_HEIGHT = 544;

    // box2d
    float UNITS_PER_METER = 32F;
    float UNIT_WIDTH = WORLD_WIDTH / UNITS_PER_METER;
    float UNIT_HEIGHT = WORLD_HEIGHT / UNITS_PER_METER;
    String HORIZONTAL = "horizontal";
    String VERTICAL = "vertical";
    String FLOOR = "floor";
    String ENEMY = "enemy";

    float PIXELS_PER_TILE = 32F;
    String NUTTYBIRDS_TMX = "tiled/level0.tmx";
    // LAYERS
    String PHYSICS_BUILDINGS_LAYER = "Physics_Buildings";
    String PHYSICS_FLOOR_LAYER = "Physics_Floor";
    String PHYSICS_BIRDS_LAYER = "Physics_Birds";

    // TEXTURES
    String OBSTACLE_VERTICAL_PNG = "obstacleVertical.png";
    String OBSTACLE_HORIZONTAL_PNG = "obstacleHorizontal.png";
    String BIRD_PNG = "bird.png";
    String SLINGSHOT_PNG = "slingshot.png";
    String SQUIRREL_PNG = "squirrel.png";
    String ACORN_PNG = "acorn.png";

    // FIRE
    float MAX_STRENGTH = 15;
    float MAX_DISTANCE = 100;
    float UPPER_ANGLE = 3 * MathUtils.PI / 2f;
    float LOWER_ANGLE = MathUtils.PI / 2f;






}
