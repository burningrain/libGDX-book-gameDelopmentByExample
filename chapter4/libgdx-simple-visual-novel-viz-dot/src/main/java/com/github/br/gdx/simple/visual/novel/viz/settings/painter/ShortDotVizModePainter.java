package com.github.br.gdx.simple.visual.novel.viz.settings.painter;

import com.github.br.gdx.simple.visual.novel.viz.data.NodeElementType;
import com.github.br.gdx.simple.visual.novel.viz.data.NodeElementVizData;
import com.github.br.gdx.simple.visual.novel.viz.settings.DotVizSettings;
import com.github.br.gdx.simple.visual.novel.viz.settings.color.NodeColorsSchema;

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
            boolean isExceptionNode
    ) {
        NodeColorsSchema colorSchema = settings.getColorSchema();

        StringBuilder builder = new StringBuilder();
        builder
                .append(nodeId)
                .append(" [\n")
                .append("label=\"")
                .append(value.getNodeId().getId())
                .append("\"").append("\n")
                .append("shape=").append(nodeType.getShortData().shape).append("\n");


        if (isVisited) {
            builder.append("color=").append(colorSchema.getVisitedNodesColor()).append("\n");
        } else if (isExceptionNode) {
            builder.append("color=").append(colorSchema.getErrorNodeColor()).append("\n");
            //builder.append("style=filled, fillcolor=").append(colorSchema.getErrorNodeColor()).append("\n");
        }

        String color = nodeType.getShortData().color;
        if (color != null) {
            builder.append("style=filled, fillcolor=").append(color).append("\n");
        }

        builder.append("];\n");
        return builder.toString();
    }

}
