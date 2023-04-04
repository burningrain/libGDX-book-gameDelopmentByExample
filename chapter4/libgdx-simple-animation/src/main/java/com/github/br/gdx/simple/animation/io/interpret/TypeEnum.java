package com.github.br.gdx.simple.animation.io.interpret;

public enum TypeEnum {
    FLOAT("Float"), INTEGER("Integer"), BOOLEAN("Boolean");

    private String value;

    TypeEnum(String value) {
        this.value = value;
    }

    public static TypeEnum getByValue(String value) {
        for (TypeEnum type : values()) {
            if(type.value.equals(value)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown value [" + value + "]");
    }

}
