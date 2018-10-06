package com.github.br.ecs.simple.engine.debug.data;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by user on 07.04.2018.
 */
public class TableData implements DebugData {

    private LinkedHashMap<String,String> map = new LinkedHashMap<String, String>();

    private TableData(Builder builder) {
        this.map = builder.map;
    }

    public void forEach(Callback callback) {
        for (Map.Entry<String, String> entry : map.entrySet()) {
            callback.call(entry.getKey(), entry.getValue());
        }
    }

    public static class Builder {
        private LinkedHashMap<String,String> map = new LinkedHashMap<String, String>();

        public Builder put(String key,String value) {
            map.put(key, value);
            return this;
        }

        public TableData build() {
            return new TableData(this);
        }

    }

    public interface Callback {
        void call(String key, String value);
    }

}
