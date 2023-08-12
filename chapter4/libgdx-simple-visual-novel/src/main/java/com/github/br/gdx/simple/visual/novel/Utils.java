package com.github.br.gdx.simple.visual.novel;

public class Utils {

    private Utils() {}

    public static <T> T checkNotNull(T object, String fieldName) {
        if(object == null) {
            throw new NullPointerException(fieldName + " must not be null");
        }

        return object;
    }

}
