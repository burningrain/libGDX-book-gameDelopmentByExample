package com.github.br.gdx.simple.visual.novel.api.plot.visitor.viz.settings.painter;

import com.github.br.gdx.simple.visual.novel.api.node.CompositeNode;
import com.github.br.gdx.simple.visual.novel.api.node.Node;
import com.github.br.gdx.simple.visual.novel.api.plot.visitor.viz.DotUtils;
import com.github.br.gdx.simple.visual.novel.api.plot.visitor.viz.data.NodeElementType;
import com.github.br.gdx.simple.visual.novel.api.plot.visitor.viz.data.NodeElementVizData;
import com.github.br.gdx.simple.visual.novel.api.plot.visitor.viz.settings.DotVizSettings;
import com.github.br.gdx.simple.visual.novel.api.plot.visitor.viz.settings.color.FullModeColorSchema;
import com.github.br.gdx.simple.visual.novel.api.plot.visitor.viz.settings.color.NodeColorSchema;

import java.util.Map;

public class FullDotVizModePainter implements DotVizModePainter {

    private final LegendPainter legendPainter;

    public FullDotVizModePainter(LegendPainter legendPainter) {
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
                .append("label=")
                .append(createLabelFullNodeInfo(settings, nodeId, value))
                .append("\n")
                .append("shape=").append("plaintext").append("\n");

        if (isVisited) {
            builder.append("color=").append(colorSchema.getVisitedNodesColor()).append("\n");
        } else if(isExceptionNode) {
            builder.append("color=").append(colorSchema.getErrorNodeColor()).append("\n");
            builder.append("style=filled, fillcolor=").append(colorSchema.getErrorNodeColor()).append("\n");
        }

        builder.append("];\n");
        return builder.toString();
    }

    private String createLabelFullNodeInfo(DotVizSettings settings, String nodeId, NodeElementVizData value) {
        return createLabelFullNodeInfo(settings, nodeId, value.getNode());
    }

    public String createLabelFullNodeInfo(DotVizSettings settings, String nodeId, Node<?, ?> node) {
        StringBuilder builder = new StringBuilder();
        builder
                .append("<<TABLE BORDER=\"0\" CELLBORDER=\"1\" CELLSPACING=\"0\">")
                .append(createLabelFullNodeInfo(settings, nodeId, node, 0))
                .append("</TABLE>>");

        return builder.toString();
    }

    private String createLabelFullNodeInfo(DotVizSettings settings, String nodeId, Node<?, ?> node, int level) {
        StringBuilder result = new StringBuilder();
        result.append(createFullInfoHeader(settings, nodeId, node, level));
        result.append(createFullInfoBody(settings, nodeId, node, settings.getVizDataParamExtractor().extractNodeParams(node)));

        if (node instanceof CompositeNode) {
            level++;
            CompositeNode<?, ?> compositeNode = ((CompositeNode<?, ?>) node);
            for (Node<?, ?> compositeInnerNode : compositeNode.getNodes()) {
                result.append(createLabelFullNodeInfo(settings, "", compositeInnerNode, level));
            }
        }

        return result.toString();
    }

    protected String createFullInfoHeader(DotVizSettings settings, String nodeId, Node<?, ?> node, int level) {
        String spaces = DotUtils.repeatString(" ", level * 2);
        String fullNodeId = nodeId + " " + createAdditionalHeaderInfoNodeId(node);

        String align = "";
        if (level > 0) {
            fullNodeId = spaces + level + fullNodeId;
            align = "ALIGN=\"LEFT\"";
            spaces += "    "; // для отступа в названиях классов и параметрах
        }

        FullModeColorSchema fullModeSchema = settings.getColorSchema().getFullMode();
        StringBuilder builder = new StringBuilder();
        builder
                .append("<TR>")
                .append("<TD BGCOLOR=\"" + fullModeSchema.getNodeIdColor(fullNodeId, node) + "\" ").append(align).append(">")
                .append(fullNodeId)
                .append("</TD>")
                .append("</TR>")

                .append("<TR>")
                .append("<TD BGCOLOR=\"" + fullModeSchema.getClassNameColor(fullNodeId, node) + "\" ").append(align).append(">")
                .append(spaces).append(node.getClass().getName())
                .append("</TD>")
                .append("</TR>");

        return builder.toString();
    }

    protected String createFullInfoBody(DotVizSettings settings, String nodeId, Node<?, ?> node, Map<String, String> customParams) {
        if (customParams == null || customParams.isEmpty()) {
            return "";
        }

        FullModeColorSchema fullModeSchema = settings.getColorSchema().getFullMode();
        StringBuilder builder = new StringBuilder();
        for (Map.Entry<String, String> entry : customParams.entrySet()) {
            builder
                    .append("<TR>")
                    .append("<TD BGCOLOR=\"" + fullModeSchema.getCustomParamNameColor(nodeId, node, customParams, entry.getKey()) + "\"").append(">")
                    .append(entry.getKey())
                    .append("</TD>")
                    .append("<TD BGCOLOR=\"" + fullModeSchema.getCustomParamValueColor(nodeId, node, customParams, entry.getKey(), entry.getValue()) + "\"").append(">")
                    .append(entry.getValue())
                    .append("</TD>")
                    .append("</TR>")
            ;
        }

        return builder.toString();
    }

    protected String createAdditionalHeaderInfoNodeId(Node<?, ?> node) {
        String result = "";
//        if (node instanceof SceneLinkNode) {
//            result += "(scene link)" + "<BR/>" + "sceneId=[" + ((SceneLinkNode<?, ?>) node).getSceneTitle() + "]";
//        } else if (node instanceof CompositeNode) {
//            result += "(composite)";
//        } else {
//            result += "(simple node)";
//        }

        return result;
    }

}
