package com.github.br.ecs.simple.engine.debug.data;

/**
 * Created by user on 07.04.2018.
 */
public class TableData implements DebugData {

    private final String key;
    private final String value;

    public TableData(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }

}
