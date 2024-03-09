package com.github.br.gdx.simple.visual.novel.api.plot.visitor.viz.data;

import com.github.br.gdx.simple.visual.novel.api.plot.visitor.viz.settings.color.SvgColor;

public class NodeElementType {

    public static final NodeElementType SIMPLE_NODE = create(
            "simple_node",
            "simple node",
            new FullViz(SvgColor.PAPAYA_WHIP),
            new ShortViz("circle", null)
    );

    public static final NodeElementType COMPOSITE_NODE = create(
            "composite_node",
            "composite node",
            new FullViz(SvgColor.PEACH_PUFF),
            new ShortViz("Mcircle", null)
    );

    public static final NodeElementType SCENE_LINK = create(
            "scene_link_node",
            "scene link node",
            new FullViz(SvgColor.LIGHT_CYAN),
            new ShortViz("doubleoctagon", null)
    );

    public static final NodeElementType CUSTOM_NODE = create(
            "custom_node",
            "custom node",
            new FullViz(SvgColor.LAVENDER),
            new ShortViz("plaintext", null)
    );

    private final String elementId;
    private final String label;

    private final FullViz fullData;
    private final ShortViz shortData;

    public static class FullViz {
        public final String headerColor;

        public FullViz(String headerColor) {
            this.headerColor = headerColor;
        }
    }

    public static class ShortViz {
        public final String shape;
        public final String legendColor;

        public ShortViz(String shape, String legendColor) {
            this.shape = shape;
            this.legendColor = legendColor;
        }
    }

    private static NodeElementType create(
            String elementId,
            String label,
            FullViz fullData,
            ShortViz shortData
    ) {
        return new NodeElementType(elementId, label, fullData, shortData);
    }

    public NodeElementType(
            String elementId,
            String label,
            FullViz fullData,
            ShortViz shortData
    ) {
        this.elementId = elementId;
        this.label = label;
        this.fullData = fullData;
        this.shortData = shortData;
    }

    public String getElementId() {
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

}
