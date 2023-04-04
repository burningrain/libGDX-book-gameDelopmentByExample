package com.github.br.gdx.simple.animation.io.interpret;

public enum OperatorEnum {
    LT("<"), GT(">"), EQ("=="), LT_OR_EQ("<="), GT_OR_EQ(">=");

    private String value;

    OperatorEnum(String value) {
        this.value = value;
    }

    public static OperatorEnum getByValue(String value) {
        for (OperatorEnum type : values()) {
            if(type.value.equals(value)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown value [" + value + "]");
    }

}
