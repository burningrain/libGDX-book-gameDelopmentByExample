package com.github.br.paper.airplane.gameworld;

public interface Res {

    interface Pictures {
        String AIR_HERO_PNG = "air_hero.png";
        String BACKGROUND_PNG = "background.png";
    }

    interface HUD {
        String HERO_LIFE_COUNT_PNG = "hero_life_count.png";
        String BULLETS_COUNT_PNG = "bullets_count.png";
    }

    interface Skins {
        String SKIN = "skin/game_skin.json";
        String MAIN_MENU_SCENE = "scene/main_menu.json";
    }

    interface Shaders {
        String SHADER_DEFAULT_VERT = "shaders/default.vert";
        String SHADER_COSMOS_BACKGROUND = "shaders/cosmos_background.vert";
    }

    interface Particles {
        String PARTICLE_COIN_PNG = "particles/coin/particle.png";
        String PARTICLE_COIN_P = "particles/coin/coin.p";
        String PARTICLE_BULLET_TYPE_P = "particles/coin/bullet_type.p";

        // bullets
        String PARTICLE_FIRE_BULLET_PNG = "particles/bullet/particle-fire.png";
        String PARTICLE_FIRE_BULLET_P = "particles/bullet/bullet.p";
        String PARTICLE_ELECTRO_BULLET_PNG = "particles/bullet/flash.png";
        String PARTICLE_ELECTRO_BULLET_P = "particles/bullet/electrical_bullet.p";
        String PARTICLE_VENOM_BULLET_PNG = "particles/bullet/particle.png";
        String PARTICLE_VENOM_BULLET_P = "particles/bullet/venom_bullet.p";

        // smoke
        String PARTICLE_SMOKE_PARTICLE_CLOUD_PNG = "particles/smoke/particle-cloud.png";
        String PARTICLE_SMOKE_FAST_FADE_OUT_P = "particles/smoke/smoke_fast_fade_out.p";

        // fire
        String PARTICLE_FIRE_PNG = "particles/fire/particle.png";
        String PARTICLE_FIRE_P = "particles/fire/fire.p";
    }


    interface Music {
        String NOSTRO_5 = "sound/music/nostro5.mp3";
        String NOSTRO_28 = "sound/music/nostro28.mp3";
        String ROLL40000005500505 = "sound/music/ROLL40000005500505.mp3";
        String STONE_14 = "sound/music/stone14.mp3";
        String STONE_INSTRUMENTS = "sound/music/STONEINSTRUMENTS.mp3";
    }

}
