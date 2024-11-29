package com.github.br.paper.airplane;

public class GameSettings {

    private final int progressBarWidth;
    private final int progressBarHeight;

    // virtual
    private final int virtualScreenWidth;
    private final int virtualScreenHeight;

    // box2d
    private final float unitsPerMeter;
    private final float unitWidth;
    private final float unitHeight;

    private final float timeStep;
    private final int velocityIterations;
    private final int positionIterations;

    private final GamePlaySettings gamePlaySettings;


    private GameSettings(Builder builder) {
        this.progressBarWidth = builder.progressBarWidth;
        this.progressBarHeight = builder.progressBarHeight;


        this.virtualScreenWidth = builder.virtualScreenWidth;
        this.virtualScreenHeight = builder.virtualScreenHeight;

        this.unitsPerMeter = builder.unitsPerMeter;
        this.unitWidth = this.virtualScreenWidth / this.unitsPerMeter;
        this.unitHeight = this.virtualScreenHeight / this.unitsPerMeter;

        this.timeStep = builder.timeStep;
        this.velocityIterations = builder.velocityIterations;
        this.positionIterations = builder.positionIterations;

        this.gamePlaySettings = builder.gamePlaySettings;
    }

    public static Builder builder() {
        return new Builder();
    }

    public int getProgressBarWidth() {
        return progressBarWidth;
    }

    public int getProgressBarHeight() {
        return progressBarHeight;
    }

    public int getVirtualScreenWidth() {
        return virtualScreenWidth;
    }

    public int getVirtualScreenHeight() {
        return virtualScreenHeight;
    }

    public float getUnitsPerMeter() {
        return unitsPerMeter;
    }

    public float getUnitWidth() {
        return unitWidth;
    }

    public float getUnitHeight() {
        return unitHeight;
    }

    public float getTimeStep() {
        return timeStep;
    }

    public int getVelocityIterations() {
        return velocityIterations;
    }

    public int getPositionIterations() {
        return positionIterations;
    }

    public GamePlaySettings getGamePlaySettings() {
        return gamePlaySettings;
    }

    public static class Builder {

        private int progressBarWidth = 100;
        private int progressBarHeight = 25;

        private int virtualScreenWidth = 1280;
        private int virtualScreenHeight = 720;

        private float unitsPerMeter = 128f;

        private float timeStep = 1 / 60f;
        private int velocityIterations = 6;
        private int positionIterations = 2;

        private GamePlaySettings gamePlaySettings = new GamePlaySettings(); //TODO задел под разные уровни сложности игры

        public Builder setProgressBarWidth(int progressBarWidth) {
            this.progressBarWidth = progressBarWidth;
            return this;
        }

        public Builder setProgressBarHeight(int progressBarHeight) {
            this.progressBarHeight = progressBarHeight;
            return this;
        }

        public Builder setVirtualScreenWidth(int virtualScreenWidth) {
            this.virtualScreenWidth = virtualScreenWidth;
            return this;
        }

        public Builder setVirtualScreenHeight(int virtualScreenHeight) {
            this.virtualScreenHeight = virtualScreenHeight;
            return this;
        }

        public Builder setUnitsPerMeter(float unitsPerMeter) {
            this.unitsPerMeter = unitsPerMeter;
            return this;
        }

        public Builder setTimeStep(float timeStep) {
            this.timeStep = timeStep;
            return this;
        }

        public Builder setVelocityIterations(int velocityIterations) {
            this.velocityIterations = velocityIterations;
            return this;
        }

        public Builder setPositionIterations(int positionIterations) {
            this.positionIterations = positionIterations;
            return this;
        }

        public Builder setGamePlaySettings(GamePlaySettings gamePlaySettings) {
            this.gamePlaySettings = gamePlaySettings;
            return this;
        }

        public GameSettings build() {
            return new GameSettings(this);
        }

    }

}
