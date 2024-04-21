package com.github.br.gdx.simple.visual.novel.viz.settings.painter;

import com.github.br.gdx.simple.visual.novel.viz.data.NodeElementType;
import com.github.br.gdx.simple.visual.novel.viz.data.NodeElementVizData;
import com.github.br.gdx.simple.visual.novel.viz.settings.DotVizSettings;
import com.github.br.gdx.simple.visual.novel.viz.settings.color.DotColorsSchema;

public class ShortDotVizModePainter implements DotVizModePainter {

    private final LegendPainter legendPainter;

    public ShortDotVizModePainter(LegendPainter legendPainter) {
        this.legendPainter = legendPainter;
    }

    @Override
    public String createLegend(DotVizSettings settings) {
        return legendPainter.createLegend(settings);
    }

    @Override
    public String createNodeInfo(
            DotVizSettings settings,
            NodeElementType nodeType,
            String nodeId,
            String label,
            NodeElementVizData value,
            boolean isVisited,
            boolean isCurrentNode,
            boolean isHasException
    ) {
        DotColorsSchema colorSchema = settings.getColorSchema();
        NodeElementType.ShortViz shortData = nodeType.getShortData();

        StringBuilder builder = new StringBuilder();
        builder
                .append(nodeId)
                .append(" [\n")
                .append("label=\"")
                .append(value.getNodeId().getId())
                .append("\"").append("\n")
                .append("shape=").append(shortData.shape).append("\n");


        if (isVisited) {
            builder.append("color=").append(colorSchema.getVisitedNodesColor()).append("\n");
        } else if (isCurrentNode) {
            String color = isHasException? colorSchema.getErrorNodeColor() : colorSchema.getCurrentNodeColor();
            builder.append("color=").append(color).append(",").append("\n");
            builder.append("penwidth=2 ").append("\n");
        } else if (shortData.borderColor != null) {
            builder.append("color=").append(shortData.borderColor).append("\n");
        }

        String fillColor = shortData.fillColor;
        if (fillColor != null) {
            builder.append("style=filled, fillcolor=").append(fillColor).append("\n");
        }

        builder.append("];\n");
        return builder.toString();
    }

}
