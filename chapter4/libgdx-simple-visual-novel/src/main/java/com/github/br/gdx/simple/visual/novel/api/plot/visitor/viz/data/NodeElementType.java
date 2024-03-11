package com.github.br.gdx.simple.visual.novel.api.plot.visitor.viz.data;

import com.github.br.gdx.simple.visual.novel.api.plot.visitor.viz.settings.color.*;
import com.github.br.gdx.simple.visual.novel.utils.Utils;

public class NodeElementType {

    public static final NodeElementType SIMPLE_NODE =
            builder()
                    .setElementId("simple_node")
                    .setLabel("simple node")
                    .setShortData(ShortViz.builder().setShape("circle").build())
                    .setFullData(FullViz.builder().setHeaderColor(GraphvizColor.PAPAYA_WHIP).build())
                    .build();

    public static final NodeElementType COMPOSITE_NODE =
            builder()
                    .setElementId("composite_node")
                    .setLabel("composite node")
                    .setShortData(ShortViz.builder().setShape("Mcircle").build())
                    .setFullData(FullViz.builder().setHeaderColor(GraphvizColor.PEACH_PUFF).build())
                    .build();

    public static final NodeElementType SCENE_LINK =
            builder()
                    .setElementId("scene_link_node")
                    .setLabel("scene link node")
                    .setShortData(ShortViz.builder().setShape("doubleoctagon").build())
                    .setFullData(FullViz.builder().setHeaderColor(GraphvizColor.LIGHT_CYAN).build())
                    .build();

    private final NodeElementTypeId elementId;
    private final String label;

    private final FullViz fullData;
    private final ShortViz shortData;

    public static class FullViz {

        public final String headerColor;
        public final FullModeColorSchema colorSchema;

        private FullViz(Builder builder) {
            this.headerColor = builder.headerColor;
            this.colorSchema = builder.colorSchema;
        }

        public static Builder builder() {
            return new Builder();
        }

        public static class Builder {

            private static final FullModeColorSchema DEFAULT_FULL_MODE_COLOR_SCHEMA = new DefaultFullModeColorSchema();

            private String headerColor;
            private FullModeColorSchema colorSchema = DEFAULT_FULL_MODE_COLOR_SCHEMA;

            public Builder setHeaderColor(String headerColor) {
                this.headerColor = Utils.checkNotNull(headerColor, "headerColor");
                return this;
            }

            public Builder setColorSchema(FullModeColorSchema colorSchema) {
                this.colorSchema = Utils.checkNotNull(colorSchema, "colorSchema");
                return this;
            }

            public FullViz build() {
                return new FullViz(this);
            }

        }

    }

    public static class ShortViz {

        public final String shape;
        public final String color;
        public final ShortModeColorSchema colorSchema;

        private ShortViz(Builder builder) {
            this.shape = builder.shape;
            this.color = builder.color;
            this.colorSchema = builder.colorSchema;
        }

        public static Builder builder() {
            return new Builder();
        }

        public static class Builder {

            private static final ShortModeColorSchema DEFAULT_SHORT_COLOR_SCHEMA = new DefaultShortModeColorSchema();

            private String shape;
            private String color;
            private ShortModeColorSchema colorSchema = DEFAULT_SHORT_COLOR_SCHEMA;

            public Builder setShape(String shape) {
                this.shape = Utils.checkNotNull(shape, "shape");
                return this;
            }

            public Builder setColor(String color) {
                this.color = Utils.checkNotNull(color, "color");
                return this;
            }

            public Builder setColorSchema(ShortModeColorSchema colorSchema) {
                this.colorSchema = Utils.checkNotNull(colorSchema, "colorSchema");
                return this;
            }

            public ShortViz build() {
                return new ShortViz(this);
            }

        }

    }

    private NodeElementType(Builder builder) {
        this.elementId = builder.elementId;
        this.label = builder.label;
        this.fullData = builder.fullData;
        this.shortData = builder.shortData;
    }

    public NodeElementTypeId getElementId() {
        return elementId;
    }

    public String getLabel() {
        return label;
    }

    public FullViz getFullData() {
        return fullData;
    }

    public ShortViz getShortData() {
        return shortData;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private NodeElementTypeId elementId;
        private String label;
        private FullViz fullData;
        private ShortViz shortData;

        public Builder setElementId(String elementId) {
            this.elementId = NodeElementTypeId.of(elementId);
            return this;
        }

        public Builder setLabel(String label) {
            this.label = Utils.checkNotNull(label, "label");
            return this;
        }

        public Builder setFullData(FullViz fullData) {
            this.fullData = Utils.checkNotNull(fullData, "fullData");
            return this;
        }

        public Builder setShortData(ShortViz shortData) {
            this.shortData = Utils.checkNotNull(shortData, "shortData");
            return this;
        }

        public NodeElementType build() {
            return new NodeElementType(this);
        }

    }

}
