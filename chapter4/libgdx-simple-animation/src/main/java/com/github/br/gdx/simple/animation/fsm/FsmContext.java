package com.github.br.gdx.simple.animation.fsm;

import com.badlogic.gdx.utils.ObjectMap;


/**
 * Created by user on 09.04.2017.
 */
public class FsmContext {

    private final ObjectMap<String, Object> params = new ObjectMap<String, Object>();
    private ObjectMap<String, FsmContext> subContexts;

    private String currentStateName;


    public void update(String key, Object value) {
        params.put(key, value);
    }

    @SuppressWarnings("unchecked")
    public <T> T get(String key) {
        return (T) params.get(key);
    }

    public void delete(String key) {
        params.remove(key);
    }

    public void insert(String key, Object value) {
        params.put(key, value);
    }

    public void setCurrentState(String stateName) {
        this.currentStateName = stateName;
    }

    public String getCurrentState() {
        return currentStateName;
    }

    public void addSubContext(String stateName, FsmContext subContext) {
        if(subContexts == null) {
            subContexts = new ObjectMap<String, FsmContext>();
        }
        subContexts.put(stateName, subContext);
    }

    public FsmContext getSubContext(String stateName) {
        return subContexts.get(stateName);
    }

}
