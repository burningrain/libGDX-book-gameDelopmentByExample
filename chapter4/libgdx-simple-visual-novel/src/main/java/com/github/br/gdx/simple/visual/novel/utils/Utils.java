package com.github.br.gdx.simple.visual.novel.utils;

public class Utils {

    private Utils() {
    }

    public static <T> T checkNotNull(T object, String fieldName) {
        if (object == null) {
            throw new NullPointerException(fieldName + " must not be null");
        }

        return object;
    }

    public static String repeatString(String symbol, int count) {
        if (symbol == null) {
            return null;
        }

        if (count < 0) {
            throw new IllegalArgumentException("varibale 'count' must not be lower than 0");
        }

        if (count == 0) {
            return "";
        }

        StringBuilder result = new StringBuilder();
        for (int i = 0; i < count; i++) {
            result.append(symbol);
        }

        return result.toString();
    }

}
