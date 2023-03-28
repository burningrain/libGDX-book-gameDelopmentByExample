package com.github.br.gdx.simple.animation.io;

import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonWriter;
import com.github.br.gdx.simple.animation.io.dto.AnimationDto;

public class JsonConverter implements Converter<String, AnimationDto> {

    private final Json JSON = new Json(JsonWriter.OutputType.json);

    public AnimationDto from(String json) {
        return JSON.fromJson(AnimationDto.class, json);
    }

    public String to(AnimationDto animationDto) {
        return JSON.prettyPrint(animationDto);
    }

}
