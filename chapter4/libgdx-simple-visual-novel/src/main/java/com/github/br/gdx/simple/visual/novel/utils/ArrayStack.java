package com.github.br.gdx.simple.visual.novel.utils;

import java.util.ArrayList;
import java.util.List;

public class ArrayStack<T> implements Stack<T> {

    private final ArrayList<T> arrayList;

    public ArrayStack() {
        this(16);
    }

    public ArrayStack(int size) {
        arrayList = new ArrayList<>(size);
    }

    @Override
    public T push(T object) {
        arrayList.add(object);
        return object;
    }

    @Override
    public T pop() {
        if (arrayList.isEmpty()) {
            return null;
        }
        return arrayList.remove(arrayList.size() - 1);
    }

    @Override
    public T peek() {
        return arrayList.get(arrayList.size() - 1);
    }

    @Override
    public int size() {
        return arrayList.size();
    }

    @Override
    public T peekParent() {
        if (arrayList.size() < 2) {
            return null;
        }

        return arrayList.get(arrayList.size() - 2);
    }

    protected List<T> getArrayList() {
        return arrayList;
    }

}
