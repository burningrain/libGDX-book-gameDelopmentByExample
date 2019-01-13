package com.github.br.ecs.simple.utils.cache;

/**
 * Created by user on 12.01.2019.
 */
public class PeriodCacheValueLong {

    private long value;
    private float period;
    private float time;
    private Updater updateCallback;

    public PeriodCacheValueLong(float period, Updater updateCallback) {
        this(0, period, updateCallback);
    }

    public PeriodCacheValueLong(long value, float period, Updater updateCallback) {
        this.value = value;
        this.period = period;
        this.updateCallback = updateCallback;
    }

    public void update(long value, float diff) {
        time += diff;
        boolean isElapsed = false;
        if(time > period) {
            time = 0;
            this.value = value;
            isElapsed = true;
        }
        this.value = updateCallback.update(isElapsed, this.value, value);
    }

    public void setValue(long value) {
        this.value = value;
    }

    public long getValue() {
        return value;
    }

    public interface Updater {
        long update(boolean isElapsed, long oldValue, long newValue);
    }

    public static class AvgForPeriod implements Updater {

        private long sum;
        private int frequency = 0;

        @Override
        public long update(boolean isElapsed, long oldValue, long newValue) {
            long result = 0;
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
        public long update(boolean isElapsed, long oldValue, long newValue) {
            if(isElapsed) {
                return newValue;
            }
            return oldValue;
        }
    }

}
