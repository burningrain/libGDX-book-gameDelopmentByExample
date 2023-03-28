package com.github.br.gdx.simple.animation.io;

import com.github.br.gdx.simple.animation.SimpleAnimation;
import com.github.br.gdx.simple.animation.io.dto.AnimationDto;

public class FolderAnimationConverter {

    private final JsonConverter jsonConverter = new JsonConverter();
    private final AnimationConverter animationConverter = new AnimationConverter();

    public SimpleAnimation from(FolderAnimDto anim) {
        AnimationDto animationDto = jsonConverter.from(anim.getFsm());
        Object[] atlasRegions = anim.getTextureAtlas().getRegions().shrink(); // TextureAtlas[]
        return animationConverter.from(animationDto, atlasRegions, anim.getJavaClasses());
    }

}
