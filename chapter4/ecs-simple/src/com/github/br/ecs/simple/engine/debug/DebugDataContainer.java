package com.github.br.ecs.simple.engine.debug;

import com.github.br.ecs.simple.engine.debug.data.DebugData;

import java.util.ArrayList;

/**
 * Created by user on 31.07.2017.
 */
public class DebugDataContainer {

    private ArrayList<DebugData> data = new ArrayList<DebugData>();

    public DebugDataContainer put(DebugData debugData) {
        data.add(debugData);
        return this;
    }

    public void clear() {
        data.clear();
    }

    public void forEach(Callback callback) {
        for (DebugData entry : data) {
            callback.call(entry);
        }
    }

    public interface Callback {
        void call(DebugData debugData);
    }

}
