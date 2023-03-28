package com.github.br.gdx.simple.animation.io;


public interface Converter<IN, OUT> {

    OUT from(IN in);

    IN to(OUT out);

}
