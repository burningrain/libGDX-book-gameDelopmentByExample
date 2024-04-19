package com.github.br.gdx.simple.visual.novel.viz.impl;

import com.github.br.gdx.simple.visual.novel.api.ElementId;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.concurrent.locks.ReentrantLock;

public class TestUserContext implements TestInterfaceUserContext {

    public ElementId nextId;
    private final HashMap<String, Object> map = new HashMap<String, Object>() {{
        put("obj", new Object());
        put("21321", new A("ddd", 5));
        put("2424", new A("3cassaadasd", 12345));
        put("list", new ArrayList<Double>(){{
            add(4d);
            add(2323.223);
            add(22_654_032.9292021);
            add(32342.234234);
        }});
    }};

    private final HashSet<String> set = new HashSet<String>() {{
        add("the first");
        add("the second");
    }};

    @Override
    public void setNextId(ElementId elementId) {
        this.nextId = elementId;
    }

    @Override
    public ElementId getNextId() {
        return nextId;
    }

    public HashMap<String, Object> getMap() {
        return map;
    }

    private static final class A {
        private String str;
        private int i;
        private ReentrantLock reentrantLock = new ReentrantLock();

        public A(String str, int i) {
            this.str = str;
            this.i = i;
        }

        public String getStr() {
            return str;
        }

        public int getI() {
            return i;
        }
    }

}
