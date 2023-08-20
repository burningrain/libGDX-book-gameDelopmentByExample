package com.github.br.gdx.simple.visual.novel.impl;

import com.github.br.gdx.simple.visual.novel.api.ElementId;

import java.util.HashMap;

public class TestUserMapContext<K, V> implements TestInterfaceUserContext {

    private final HashMap<K, V> map = new HashMap<>();
    private ElementId nextId;

    public void put(K key, V value) {
        map.put(key, value);
    }

    public V getByKey(K key) {
        return map.get(key);
    }

    @Override
    public void setNextId(ElementId elementId) {
        this.nextId = elementId;
    }

    @Override
    public ElementId getNextId() {
        return nextId;
    }

}
