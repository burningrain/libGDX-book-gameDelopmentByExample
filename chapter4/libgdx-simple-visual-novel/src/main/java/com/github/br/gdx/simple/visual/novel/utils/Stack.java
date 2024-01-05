package com.github.br.gdx.simple.visual.novel.utils;

public interface Stack<T> {

    T push(T object);

    T pop();

    T peek();

    int size();

    T peekParent();

}
