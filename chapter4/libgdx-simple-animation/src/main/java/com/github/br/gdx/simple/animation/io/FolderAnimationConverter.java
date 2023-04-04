package com.github.br.gdx.simple.animation.io;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.github.br.gdx.simple.animation.SimpleAnimation;
import com.github.br.gdx.simple.animation.io.dto.AnimationDto;

public class FolderAnimationConverter {

    private final JsonConverter jsonConverter = new JsonConverter();
    private final AnimationConverter animationConverter = new AnimationConverter();

    public SimpleAnimation from(FolderAnimDto anim) {
        AnimationDto animationDto = jsonConverter.from(anim.getFsm());
        TextureAtlas textureAtlas = anim.getTextureAtlas();
        return animationConverter.from(animationDto, textureAtlas);
    }

}
