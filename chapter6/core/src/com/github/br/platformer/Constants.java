package com.github.br.platformer;

public interface Constants {

    float WORLD_WIDTH = 640;
    float WORLD_HEIGHT = 480;

    String PETE_TMX = "tiled_src/pete.tmx";
    String PETE_TEXTURE = "pete/pete.png";
    String ACORN_TEXTURE = "acorn.png";

    int CELL_SIZE = 16;

    // слои
    String LAYER_PLATFORMS = "platforms";
    String LAYER_COLLECTABLES = "Collectables";

    String AUDIO_JUMP = "audio/sfx/jump_c_02-102843.mp3";
    String AUDIO_COLLECT = "audio/sfx/pickup.mp3";
    String AUDIO_THEME = "audio/music/track08.mp3";

}
