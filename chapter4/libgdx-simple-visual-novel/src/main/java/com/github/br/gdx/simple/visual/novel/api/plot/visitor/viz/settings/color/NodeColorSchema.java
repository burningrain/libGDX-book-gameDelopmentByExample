package com.github.br.gdx.simple.visual.novel.api.plot.visitor.viz.settings.color;

import com.github.br.gdx.simple.visual.novel.api.plot.visitor.viz.data.NodeElementType;
import com.github.br.gdx.simple.visual.novel.utils.Utils;

import java.util.*;

public class NodeColorSchema {

    private final ShortModeColorSchema shortMode;
    private final FullModeColorSchema fullMode;

    private final String visitedNodesColor;
    private final String errorNodeColor;
    private final Map<String, NodeElementType> legendParams;
    private final String borderColor;

    private NodeColorSchema(Builder builder) {
        this.shortMode = builder.shortMode;
        this.fullMode = builder.fullMode;
        this.visitedNodesColor = builder.visitedNodesColor;
        this.errorNodeColor = builder.errorNodeColor;
        this.legendParams = createLegendParamsMap(builder.legendParams);
        this.borderColor = builder.borderColor;
    }

    private Map<String, NodeElementType> createLegendParamsMap(List<NodeElementType> legendParams) {
        HashMap<String, NodeElementType> result = new HashMap<>();
        for (NodeElementType legendParam : legendParams) {
            result.put(legendParam.getElementId(), legendParam);
        }

        return result;
    }

    public ShortModeColorSchema getShortMode() {
        return shortMode;
    }

    public FullModeColorSchema getFullMode() {
        return fullMode;
    }

    public String getVisitedNodesColor() {
        return visitedNodesColor;
    }

    public String getErrorNodeColor() {
        return errorNodeColor;
    }

    public Map<String, NodeElementType> getLegendParams() {
        return legendParams;
    }

    public static Builder builder() {
        return new Builder();
    }

    public String getBorderColor() {
        return borderColor;
    }

    public static class Builder {

        private ShortModeColorSchema shortMode = new DefaultShortModeColorSchema();
        private FullModeColorSchema fullMode = new DefaultFullModeColorSchema();

        private String borderColor = SvgColor.GREY;
        private String visitedNodesColor = SvgColor.GREEN;
        private String errorNodeColor = SvgColor.RED;
        private final ArrayList<NodeElementType> legendParams = new ArrayList<NodeElementType>() {{
            add(NodeElementType.SIMPLE_NODE);
            add(NodeElementType.COMPOSITE_NODE);
            add(NodeElementType.SCENE_LINK);
            add(NodeElementType.CUSTOM_NODE);
        }};

        public Builder setShortMode(ShortModeColorSchema shortMode) {
            this.shortMode = Utils.checkNotNull(shortMode, "shortMode");
            return this;
        }

        public Builder setFullMode(FullModeColorSchema fullMode) {
            this.fullMode = Utils.checkNotNull(fullMode, "fullMode");
            return this;
        }

        public Builder setBorderColor(String borderColor) {
            this.borderColor = borderColor;
            return this;
        }

        public Builder setVisitedNodesColor(String visitedNodesColor) {
            this.visitedNodesColor = Utils.checkNotNull(visitedNodesColor, "visitedNodesColor");
            return this;
        }

        public Builder setErrorNodeColor(String errorNodeColor) {
            this.errorNodeColor = errorNodeColor;
            return this;
        }

        public Builder addLegendParams(List<NodeElementType> legendParams) {
            this.legendParams.addAll(legendParams);
            return this;
        }

        public NodeColorSchema build() {
            return new NodeColorSchema(this);
        }

    }

}
