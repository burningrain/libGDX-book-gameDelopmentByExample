package com.github.br.ecs.simple.fsm;

import java.util.HashMap;

/**
 * Created by user on 09.04.2017.
 */
public class FsmContext {

    private HashMap<String, Object> map = new HashMap<>();

    private String currentStateName;


    public void update(String key, Object value){
        map.put(key, value);
    }

    public <T> T get(String key){
        return (T) map.get(key);
    }

    public void delete(String key){
        map.remove(key);
    }

    public void insert(String key, Object value){
        map.put(key, value);
    }

    void setCurrentState(String stateName){
        this.currentStateName = stateName;
    }

    public String getCurrentState(){
        return currentStateName;
    }

}
