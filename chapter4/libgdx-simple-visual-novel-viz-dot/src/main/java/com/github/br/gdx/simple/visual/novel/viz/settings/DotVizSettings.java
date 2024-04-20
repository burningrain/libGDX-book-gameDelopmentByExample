package com.github.br.gdx.simple.visual.novel.viz.settings;

import com.github.br.gdx.simple.visual.novel.viz.data.DefaultNodeElementVizDataParamExtractor;
import com.github.br.gdx.simple.visual.novel.viz.data.NodeElementType;
import com.github.br.gdx.simple.visual.novel.viz.settings.color.DotColorsSchema;
import com.github.br.gdx.simple.visual.novel.viz.data.NodeElementVizDataParamExtractor;
import com.github.br.gdx.simple.visual.novel.viz.settings.painter.*;
import com.github.br.gdx.simple.visual.novel.viz.utils.Supplier;
import com.github.br.gdx.simple.visual.novel.utils.Utils;

import java.util.ArrayList;

public class DotVizSettings {

    private final RankDirType rankDirType;
    private final NodeInfoType nodeInfoType;
    private final boolean isShowLegend;
    private final boolean isShowContext;

    private final DotVizModePainter shortDotVizModePainter;
    private final DotVizModePainter fullDotVizModePainter;
    private final NodeElementVizDataParamExtractor vizDataParamExtractor;

    private final DotColorsSchema colorSchema;
    private final UserContextPainter userContextPainter;

    public DotVizSettings(Builder builder) {
        this.rankDirType = builder.rankDirType;
        this.nodeInfoType = builder.nodeInfoType;
        this.isShowLegend = builder.isShowLegend;
        this.isShowContext = builder.isShowContext;
        this.shortDotVizModePainter = builder.shortDotVizModePainter;
        this.fullDotVizModePainter = builder.fullDotVizModePainter;
        this.userContextPainter = builder.userContextPainter;
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

    public boolean isShowContext() {
        return isShowContext;
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

    public DotColorsSchema getColorSchema() {
        return colorSchema;
    }

    public Builder copy() {
        Builder builder = new Builder();
        builder.setColorsSchema(new Supplier<DotColorsSchema.Builder>() {
            @Override
            public void accept(DotColorsSchema.Builder builder) {
                builder.setBorderColor(colorSchema.getBorderColor());
                builder.setVisitedNodesColor(colorSchema.getVisitedNodesColor());
                builder.setCurrentNodeColor(colorSchema.getCurrentNodeColor());

                builder.clearAllElementsTypes();
                builder.addElementsTypes(new ArrayList<NodeElementType>(colorSchema.getElementsTypes().values()));

                builder.setElementTypeDeterminant(colorSchema.getTypeDeterminant());
            }
        });
        builder.setRankDirType(this.rankDirType);
        builder.setNodeInfoType(this.nodeInfoType);
        builder.setShowLegend(this.isShowLegend);
        builder.setShortDotVizModePainter(this.shortDotVizModePainter);
        builder.setFullDotVizModePainter(this.fullDotVizModePainter);
        builder.setVizDataParamExtractor(this.vizDataParamExtractor);

        return builder;
    }

    public static Builder builder() {
        return new Builder();
    }

    public UserContextPainter getUserContextPainter() {
        return userContextPainter;
    }

    public static class Builder {

        private RankDirType rankDirType = RankDirType.LR;
        private NodeInfoType nodeInfoType = NodeInfoType.SHORT;
        private boolean isShowLegend = true;
        private boolean isShowContext = true; //TODO изменить на false по умолчанию

        private DotVizModePainter shortDotVizModePainter = new ShortDotVizModePainter(new ShortLegendPainter());
        private DotVizModePainter fullDotVizModePainter = new FullDotVizModePainter(new LegendPainter());
        private UserContextPainter userContextPainter = new DefaultUserContextPainter();
        private NodeElementVizDataParamExtractor vizDataParamExtractor = new DefaultNodeElementVizDataParamExtractor();
        private final DotColorsSchema.Builder colorSchemaBuilder = DotColorsSchema.builder();
        private DotColorsSchema colorSchema;

        public Builder setColorsSchema(Supplier<DotColorsSchema.Builder> supplier) {
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

        public Builder setShowContext(boolean showContext) {
            isShowContext = showContext;
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

        public Builder setUserContextPainter(UserContextPainter userContextPainter) {
            this.userContextPainter = Utils.checkNotNull(userContextPainter, "userContextPainter");
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

    /**
     * Тип отрисовки схемы
     */
    public enum RankDirType {

        LR("LR"), // слева-направо
        TB("TB")  // сверху-вниз
        ;

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
