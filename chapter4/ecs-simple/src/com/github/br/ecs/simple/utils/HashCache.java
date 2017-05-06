package com.github.br.ecs.simple.utils;

/**
 * Тупо кешируем по хэш-сумме.
 * Грязный способ отследить, изменился объект или нет.
 */
public class HashCache<T> {

    private int hashcode;
    private T value;

    public HashCache(Object o, T value){
        this.hashcode = o.hashCode();
        this.value = value;
    }

    public void update(Object o, T value){
        this.hashcode = o.hashCode();
        this.value = value;
    }

    public T getValue(){
        return value;
    }

    public boolean isValid(Object o){
        return this.hashcode == o.hashCode();
    }

}
