package com.github.br.ecs.simple.utils.cache;

/**
 * Created by user on 12.01.2019.
 */
public class PeriodCacheValueObject<V> {

    private V value;
    private float period;
    private float time;
    private Updater<V> updateCallback;

    public PeriodCacheValueObject(V value, float period, Updater<V> updateCallback) {
        this.value = value;
        this.period = period;
        this.updateCallback = updateCallback;
    }

    public void update(V value, float diff) {
        time += diff;
        boolean isElapsed = false;
        if(time > period) {
            time = 0;
            this.value = value;
            isElapsed = true;
        }
        this.value = updateCallback.update(isElapsed, this.value, value);
    }

    public void setValue(V value) {
        this.value = value;
    }

    public V getValue() {
        return value;
    }

    public interface Updater<V> {
        V update(boolean isElapsed, V oldValue, V newValue);
    }

    public static class NewValue<V> implements Updater<V> {

        @Override
        public V update(boolean isElapsed, V oldValue, V newValue) {
            if(isElapsed) {
                return newValue;
            }
            return oldValue;
        }
    }

}
