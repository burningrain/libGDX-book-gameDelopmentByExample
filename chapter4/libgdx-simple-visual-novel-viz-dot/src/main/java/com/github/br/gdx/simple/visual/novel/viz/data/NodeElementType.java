package com.github.br.gdx.simple.visual.novel.viz.data;

import com.github.br.gdx.simple.visual.novel.viz.settings.painter.GraphvizShape;
import com.github.br.gdx.simple.visual.novel.utils.Utils;
import com.github.br.gdx.simple.visual.novel.viz.settings.color.*;
import com.github.br.gdx.simple.visual.novel.viz.utils.Supplier;

public class NodeElementType {

    public static final NodeElementType SIMPLE_NODE =
            builder()
                    .setElementId("simple_node")
                    .setLabel("simple node")
                    .setShortDataBuilder(new Supplier<ShortViz.Builder>() {
                        @Override
                        public void accept(ShortViz.Builder builder) {
                            builder.setShape(GraphvizShape.CIRCLE);
                        }
                    })
                    .setFullDataBuilder(new Supplier<FullViz.Builder>() {
                        @Override
                        public void accept(FullViz.Builder builder) {
                            builder.setHeaderColor(GraphvizColor.PAPAYA_WHIP);
                        }
                    })
                    .build();

    public static final NodeElementType COMPOSITE_NODE =
            builder()
                    .setElementId("composite_node")
                    .setLabel("composite node")
                    .setShortDataBuilder(new Supplier<ShortViz.Builder>() {
                        @Override
                        public void accept(ShortViz.Builder builder) {
                            builder.setShape(GraphvizShape.M_CIRCLE);
                        }
                    })
                    .setFullDataBuilder(new Supplier<FullViz.Builder>() {
                        @Override
                        public void accept(FullViz.Builder builder) {
                            builder.setHeaderColor(GraphvizColor.PEACH_PUFF);
                        }
                    })
                    .build();

    public static final NodeElementType SCENE_LINK =
            builder()
                    .setElementId("scene_link_node")
                    .setLabel("scene link node")
                    .setShortDataBuilder(new Supplier<ShortViz.Builder>() {
                        @Override
                        public void accept(ShortViz.Builder builder) {
                            builder.setShape(GraphvizShape.DOUBLE_OCTAGON);
                        }
                    })
                    .setFullDataBuilder(new Supplier<FullViz.Builder>() {
                        @Override
                        public void accept(FullViz.Builder builder) {
                            builder.setHeaderColor(GraphvizColor.LIGHT_CYAN);
                        }
                    })
                    .build();

    private final NodeElementTypeId elementId;
    private final String label;
    private final String descriptionLabel;

    private final FullViz fullData;
    private final ShortViz shortData;

    public static class FullViz {

        public final String headerColor;
        public final String borderColor;
        public final FullModeColorSchema colorSchema;

        private FullViz(Builder builder) {
            this.headerColor = builder.headerColor;
            this.borderColor = builder.borderColor;
            this.colorSchema = builder.colorSchema;
        }

        public Builder copy() {
            Builder builder = new Builder();
            builder.setColorSchema(this.colorSchema);
            builder.setHeaderColor(this.headerColor);
            builder.setBorderColor(this.borderColor);
            return builder;
        }

        public static Builder builder() {
            return new Builder();
        }

        public static class Builder {

            private static final FullModeColorSchema DEFAULT_FULL_MODE_COLOR_SCHEMA = new DefaultFullModeColorSchema();

            private String headerColor;
            private String borderColor;
            private FullModeColorSchema colorSchema = DEFAULT_FULL_MODE_COLOR_SCHEMA;

            public Builder setHeaderColor(String headerColor) {
                this.headerColor = Utils.checkNotNull(headerColor, "headerColor");
                return this;
            }

            public Builder setBorderColor(String borderColor) {
                this.borderColor = Utils.checkNotNull(borderColor, "borderColor");
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
        public final String fillColor;
        public final String borderColor;
        public final ShortModeColorSchema colorSchema;

        private ShortViz(Builder builder) {
            this.shape = builder.shape;
            this.fillColor = builder.fillColor;
            this.borderColor = builder.borderColor;
            this.colorSchema = builder.colorSchema;
        }

        public Builder copy() {
            Builder builder = new Builder();
            builder.setFillColor(this.fillColor);
            builder.setBorderColor(this.borderColor);
            builder.setShape(this.shape);
            builder.setColorSchema(this.colorSchema);

            return builder;
        }

        public static Builder builder() {
            return new Builder();
        }

        public static class Builder {

            private static final ShortModeColorSchema DEFAULT_SHORT_COLOR_SCHEMA = new DefaultShortModeColorSchema();

            private ShortModeColorSchema colorSchema = DEFAULT_SHORT_COLOR_SCHEMA;
            private String shape;
            private String fillColor;
            private String borderColor;

            public Builder setShape(String shape) {
                this.shape = Utils.checkNotNull(shape, "shape");
                return this;
            }

            public Builder setFillColor(String fillColor) {
                this.fillColor = Utils.checkNotNull(fillColor, "color");
                return this;
            }

            public Builder setColorSchema(ShortModeColorSchema colorSchema) {
                this.colorSchema = Utils.checkNotNull(colorSchema, "colorSchema");
                return this;
            }

            public void setBorderColor(String borderColor) {
                this.borderColor = Utils.checkNotNull(borderColor, "borderColor");
            }

            public ShortViz build() {
                return new ShortViz(this);
            }

        }

    }

    private NodeElementType(Builder builder) {
        this.elementId = builder.elementId;
        this.label = builder.label;
        this.descriptionLabel = this.elementId.getId() + "_description";
        this.fullData = builder.fullDataBuilder.build();
        this.shortData = builder.shortDataBuilder.build();
    }

    public NodeElementTypeId getElementId() {
        return elementId;
    }

    public String getLabel() {
        return label;
    }

    public String getDescriptionLabel() {
        return descriptionLabel;
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

    public Builder copy() {
        Builder builder = new Builder();
        builder.setElementId(this.elementId.getId());
        builder.setLabel(this.getLabel());
        builder.setShortDataBuilder(new Supplier<ShortViz.Builder>() {
            @Override
            public void accept(ShortViz.Builder builder) {
                ShortViz shortData = NodeElementType.this.getShortData();

                // FIXME кривой дизайн. разобраться с ифами
                if (shortData.fillColor != null) {
                    builder.setFillColor(shortData.fillColor);
                }
                if (shortData.borderColor != null) {
                    builder.setBorderColor(shortData.borderColor);
                }
                builder.setColorSchema(shortData.colorSchema);
                builder.setShape(shortData.shape);
            }
        });
        builder.setFullDataBuilder(new Supplier<FullViz.Builder>() {
            @Override
            public void accept(FullViz.Builder builder) {
                FullViz fullData = NodeElementType.this.getFullData();
                builder.setHeaderColor(fullData.headerColor);
                if (fullData.borderColor != null) {
                    builder.setBorderColor(fullData.borderColor);
                }
                builder.setColorSchema(fullData.colorSchema);
            }
        });

        return builder;
    }

    public static class Builder {

        private NodeElementTypeId elementId;
        private String label;
        private final FullViz.Builder fullDataBuilder = new FullViz.Builder();
        private final ShortViz.Builder shortDataBuilder = new ShortViz.Builder();

        public Builder setElementId(String elementId) {
            this.elementId = NodeElementTypeId.of(elementId);
            return this;
        }

        public Builder setLabel(String label) {
            this.label = Utils.checkNotNull(label, "label");
            return this;
        }

        public Builder setFullDataBuilder(Supplier<FullViz.Builder> fullDataBuilderSupplier) {
            Utils.checkNotNull(fullDataBuilderSupplier, "fullDataBuilderSupplier");
            fullDataBuilderSupplier.accept(fullDataBuilder);
            return this;
        }

        public Builder setShortDataBuilder(Supplier<ShortViz.Builder> shortDataBuilderSupplier) {
            Utils.checkNotNull(shortDataBuilderSupplier, "shortDataBuilderSupplier");
            shortDataBuilderSupplier.accept(shortDataBuilder);
            return this;
        }

        public NodeElementType build() {
            return new NodeElementType(this);
        }

    }

}
