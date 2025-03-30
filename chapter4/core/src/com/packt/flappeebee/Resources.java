package com.packt.flappeebee;

public interface Resources {

    interface HyperLap2D {
        String PROJECT = "project.dt";
    }

    interface Animations {
        String CRAB = "crab";
        String CRAB_ANIM_FSM = "animation/crab/crab.afsm";
    }

    interface Backgrounds {
        String BACKGROUND_PNG = "background.png";
    }

    interface Shaders {
        String BLINK_SHADER = "shaders/blink_shader.vert";
    }

    interface TextureAtlases {
        String PACK = "orig/pack.atlas";
    }

    interface Images {
        String PEARL = "pearl";

        String LIFE_1 = "life1";
        String LIFE_2 = "life2";
        String LIFE_3 = "life3";
        String LIFE_4 = "life4";
    }

}
