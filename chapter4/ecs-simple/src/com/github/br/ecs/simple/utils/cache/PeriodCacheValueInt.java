package com.github.br.ecs.simple.utils.cache;

/**
 * Created by user on 12.01.2019.
 */
public class PeriodCacheValueInt {

    private int value;
    private float period;
    private float time;
    private Updater updateCallback;

    public PeriodCacheValueInt(float period, Updater updateCallback) {
        this(0, period, updateCallback);
    }

    public PeriodCacheValueInt(int value, float period, Updater updateCallback) {
        this.value = value;
        this.period = period;
        this.updateCallback = updateCallback;
    }

    public void update(int value, float diff) {
        time += diff;
        boolean isElapsed = false;
        if(time > period) {
            time = 0;
            this.value = value;
            isElapsed = true;
        }
        this.value = updateCallback.update(isElapsed, this.value, value);
    }

    public void setValue(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public interface Updater {
        int update(boolean isElapsed, int oldValue, int newValue);
    }

    public static class AvgForPeriod implements Updater {

        private int sum;
        private int frequency = 0;

        @Override
        public int update(boolean isElapsed, int oldValue, int newValue) {
            int result = 0;
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
        public int update(boolean isElapsed, int oldValue, int newValue) {
            if(isElapsed) {
                return newValue;
            }
            return oldValue;
        }
    }


}
