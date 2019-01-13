package com.github.br.ecs.simple.utils.cache;

/**
 * Created by user on 12.01.2019.
 */
public class PeriodCacheValueFloat {

    private float value;
    private float period;
    private float time;
    private Updater updateCallback;

    public PeriodCacheValueFloat(float period, Updater updateCallback) {
        this(0, period, updateCallback);
    }

    public PeriodCacheValueFloat(float value, float period, Updater updateCallback) {
        this.value = value;
        this.period = period;
        this.updateCallback = updateCallback;
    }

    public void update(float value, float diff) {
        time += diff;
        boolean isElapsed = false;
        if(time > period) {
            time = 0;
            this.value = value;
            isElapsed = true;
        }
        this.value = updateCallback.update(isElapsed, this.value, value);
    }

    public void setValue(float value) {
        this.value = value;
    }

    public float getValue() {
        return value;
    }

    public interface Updater {
        float update(boolean isElapsed, float oldValue, float newValue);
    }

    public static class AvgForPeriod implements Updater {

        private float sum;
        private int frequency = 0;

        @Override
        public float update(boolean isElapsed, float oldValue, float newValue) {
            float result = 0f;
            if(isElapsed && frequency != 0) {
                result = sum/frequency;
                frequency = 0;
                sum = 0;
            } else {
                result = oldValue;
                sum += newValue;
                frequency++;
            }
            return result;
        }
    }

    public static class NewValue implements Updater {
        @Override
        public float update(boolean isElapsed, float oldValue, float newValue) {
            if(isElapsed) {
                return newValue;
            }
            return oldValue;
        }
    }

}
