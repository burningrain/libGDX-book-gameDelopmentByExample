package com.github.br.gdx.simple.visual.novel.api.plot.visitor.viz.settings;

import com.github.br.gdx.simple.visual.novel.api.plot.visitor.viz.data.DefaultNodeElementVizDataParamExtractor;
import com.github.br.gdx.simple.visual.novel.api.plot.visitor.viz.settings.color.NodeColorSchema;
import com.github.br.gdx.simple.visual.novel.api.plot.visitor.viz.data.NodeElementVizDataParamExtractor;
import com.github.br.gdx.simple.visual.novel.api.plot.visitor.viz.settings.painter.*;
import com.github.br.gdx.simple.visual.novel.utils.Supplier;
import com.github.br.gdx.simple.visual.novel.utils.Utils;

public class DotVizSettings {

    private final RankDirType rankDirType;
    private final NodeInfoType nodeInfoType;
    private final boolean isShowLegend;

    private final DotVizModePainter shortDotVizModePainter;
    private final DotVizModePainter fullDotVizModePainter;
    private final NodeElementVizDataParamExtractor vizDataParamExtractor;

    private final NodeColorSchema colorSchema;

    public DotVizSettings(Builder builder) {
        this.rankDirType = builder.rankDirType;
        this.nodeInfoType = builder.nodeInfoType;
        this.isShowLegend = builder.isShowLegend;
        this.shortDotVizModePainter = builder.shortDotVizModePainter;
        this.fullDotVizModePainter = builder.fullDotVizModePainter;
        this.vizDataParamExtractor = builder.vizDataParamExtractor;
        this.colorSchema = builder.colorSchema;
    }

    public RankDirType getRankDirType() {
        return rankDirType;
    }

    public NodeInfoType getNodeInfoType() {
        return nodeInfoType;
    }

    public boolean isShowLegend() {
        return isShowLegend;
    }

    public DotVizModePainter getShortDotVizModePainter() {
        return shortDotVizModePainter;
    }

    public DotVizModePainter getFullDotVizModePainter() {
        return fullDotVizModePainter;
    }

    public DotVizModePainter getCurrentDotVizModePainter() {
        return (NodeInfoType.SHORT == nodeInfoType)? shortDotVizModePainter : fullDotVizModePainter;
    }

    public NodeElementVizDataParamExtractor getVizDataParamExtractor() {
        return vizDataParamExtractor;
    }

    public NodeColorSchema getColorSchema() {
        return colorSchema;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private RankDirType rankDirType = RankDirType.LR;
        private NodeInfoType nodeInfoType = NodeInfoType.SHORT;
        private boolean isShowLegend = true;

        private DotVizModePainter shortDotVizModePainter = new ShortDotVizModePainter(new ShortLegendPainter());
        private DotVizModePainter fullDotVizModePainter = new FullDotVizModePainter(new LegendPainter());
        private NodeElementVizDataParamExtractor vizDataParamExtractor = new DefaultNodeElementVizDataParamExtractor();
        private final NodeColorSchema.Builder colorSchemaBuilder = NodeColorSchema.builder();
        private NodeColorSchema colorSchema;

        public Builder setColorsSchema(Supplier<NodeColorSchema.Builder> supplier) {
            Utils.checkNotNull(supplier, "supplier");
            supplier.accept(colorSchemaBuilder);
            return this;
        }

        public Builder setRankDirType(RankDirType rankDirType) {
            this.rankDirType = Utils.checkNotNull(rankDirType, "rankDirType");
            return this;
        }

        public Builder setNodeInfoType(NodeInfoType nodeInfoType) {
            this.nodeInfoType = Utils.checkNotNull(nodeInfoType, "nodeInfoType");
            return this;
        }

        public Builder setShowLegend(boolean showLegend) {
            isShowLegend = showLegend;
            return this;
        }

        public Builder setShortDotVizModePainter(DotVizModePainter modePainter) {
            this.shortDotVizModePainter = Utils.checkNotNull(modePainter, "modePainter");
            return this;
        }

        public Builder setFullDotVizModePainter(DotVizModePainter modePainter) {
            this.fullDotVizModePainter = Utils.checkNotNull(modePainter, "modePainter");
            return this;
        }

        public Builder setVizDataParamExtractor(NodeElementVizDataParamExtractor vizDataParamExtractor) {
            this.vizDataParamExtractor = Utils.checkNotNull(vizDataParamExtractor, "vizDataParamExtractor");
            return this;
        }

        public DotVizSettings build() {
            this.colorSchema = colorSchemaBuilder.build();
            return new DotVizSettings(this);
        }

    }

    public enum RankDirType {

        LR("LR"), TB("TB");

        private final String value;

        RankDirType(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }

    }

    public enum NodeInfoType {
        SHORT, FULL
    }

}
