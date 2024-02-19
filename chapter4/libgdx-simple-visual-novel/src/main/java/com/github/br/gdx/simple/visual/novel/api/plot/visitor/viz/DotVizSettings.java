package com.github.br.gdx.simple.visual.novel.api.plot.visitor.viz;

import com.github.br.gdx.simple.visual.novel.api.plot.visitor.viz.data.DefaultNodeElementVizDataFactory;
import com.github.br.gdx.simple.visual.novel.api.plot.visitor.viz.data.NodeElementVizDataFactory;

public class DotVizSettings {

    private final RankDirType rankDirType;
    private final NodeInfoType nodeInfoType;
    private final boolean isShowLegend;
    public final NodeElementVizDataFactory nodeElementVizDataFactory;

    public DotVizSettings(Builder builder) {
        this.rankDirType = builder.rankDirType;
        this.nodeInfoType = builder.nodeInfoType;
        this.isShowLegend = builder.isShowLegend;
        this.nodeElementVizDataFactory = builder.nodeElementVizDataFactory;
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

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private RankDirType rankDirType = RankDirType.LR;
        private NodeInfoType nodeInfoType = NodeInfoType.SHORT;
        private boolean isShowLegend = true;
        private NodeElementVizDataFactory nodeElementVizDataFactory = new DefaultNodeElementVizDataFactory();

        public Builder setRankDirType(RankDirType rankDirType) {
            this.rankDirType = rankDirType;
            return this;
        }

        public Builder setInfoType(NodeInfoType nodeInfoType) {
            this.nodeInfoType = nodeInfoType;
            return this;
        }

        public Builder setShowLegend(boolean showLegend) {
            isShowLegend = showLegend;
            return this;
        }

        public Builder setNodeElementVizDataFactory(NodeElementVizDataFactory nodeElementVizDataFactory) {
            this.nodeElementVizDataFactory = nodeElementVizDataFactory;
            return this;
        }

        public DotVizSettings build() {
            return new DotVizSettings(this);
        }

    }

    public enum RankDirType {

        LR("LR"), TB("TB");

        private String value;

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
