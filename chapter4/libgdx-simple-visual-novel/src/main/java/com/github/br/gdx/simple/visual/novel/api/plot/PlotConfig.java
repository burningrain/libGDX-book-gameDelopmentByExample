package com.github.br.gdx.simple.visual.novel.api.plot;

import com.github.br.gdx.simple.visual.novel.Utils;

public class PlotConfig<ID> {

    private final boolean isMarkVisitedNodes;
    private final GeneratorPlotId<ID> generatorPlotId;
    private final boolean isSavePath;

    public PlotConfig(Builder<ID> builder) {
        this.isMarkVisitedNodes = builder.isMarkVisitedNodes;
        this.isSavePath = builder.isSavePath;
        this.generatorPlotId = Utils.checkNotNull(builder.generatorPlotId, "generatorPlotId");
    }

    public boolean isMarkVisitedNodes() {
        return isMarkVisitedNodes;
    }

    public GeneratorPlotId<ID> getGeneratorPlotId() {
        return generatorPlotId;
    }

    public static <ID> Builder<ID> builder() {
        return new Builder<>();
    }

    public boolean isSavePath() {
        return isSavePath;
    }

    public static class Builder<ID> {

        public boolean isSavePath = true;
        private boolean isMarkVisitedNodes = true;
        private GeneratorPlotId<ID> generatorPlotId;

        public Builder<ID> setMarkVisitedNodes(boolean markVisitedNodes) {
            isMarkVisitedNodes = markVisitedNodes;
            return this;
        }

        public Builder<ID> setSavePath(boolean savePath) {
            isSavePath = savePath;
            return this;
        }

        public Builder<ID> setGeneratorPlotId(GeneratorPlotId<ID> generatorPlotId) {
            this.generatorPlotId = Utils.checkNotNull(generatorPlotId, "generatorPlotId");
            return this;
        }

        public PlotConfig<ID> build() {
            return new PlotConfig<>(this);
        }

    }

}
