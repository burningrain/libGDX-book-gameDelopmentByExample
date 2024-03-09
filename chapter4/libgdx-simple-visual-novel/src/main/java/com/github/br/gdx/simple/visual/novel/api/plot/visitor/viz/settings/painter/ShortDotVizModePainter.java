package com.github.br.gdx.simple.visual.novel.api.plot.visitor.viz.settings.painter;

import com.github.br.gdx.simple.visual.novel.api.node.CompositeNode;
import com.github.br.gdx.simple.visual.novel.api.node.Node;
import com.github.br.gdx.simple.visual.novel.api.plot.visitor.viz.data.NodeElementType;
import com.github.br.gdx.simple.visual.novel.api.plot.visitor.viz.data.NodeElementVizData;
import com.github.br.gdx.simple.visual.novel.api.plot.visitor.viz.settings.DotVizSettings;
import com.github.br.gdx.simple.visual.novel.api.plot.visitor.viz.settings.color.NodeColorSchema;
import com.github.br.gdx.simple.visual.novel.inner.SceneLinkNode;

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
    public String createNodeInfo(DotVizSettings settings, String nodeId, String label, NodeElementVizData value,
                                 boolean isVisited, boolean isExceptionNode) {
        NodeColorSchema colorSchema = settings.getColorSchema();

        StringBuilder builder = new StringBuilder();
        builder
                .append(nodeId)
                .append(" [\n")
                .append("label=\"")
                .append(value.getNodeId().getId())
                .append("\"").append("\n")
                .append("shape=").append(getNodeShape(value.getNode())).append("\n");

        if (isVisited) {
            builder.append("color=").append(colorSchema.getVisitedNodesColor()).append("\n");
        } else if(isExceptionNode) {
            builder.append("color=").append(colorSchema.getErrorNodeColor()).append("\n");
            builder.append("style=filled, fillcolor=").append(colorSchema.getErrorNodeColor()).append("\n");
        }

        builder.append("];\n");
        return builder.toString();
    }

    protected String getNodeShape(Node<?, ?> node) {
        if (node instanceof CompositeNode) {
            return NodeElementType.COMPOSITE_NODE.getShortData().shape;
        } else if (node instanceof SceneLinkNode) {
            return NodeElementType.SCENE_LINK.getShortData().shape;
        } else {
            return NodeElementType.SIMPLE_NODE.getShortData().shape;
        }
    }

}
