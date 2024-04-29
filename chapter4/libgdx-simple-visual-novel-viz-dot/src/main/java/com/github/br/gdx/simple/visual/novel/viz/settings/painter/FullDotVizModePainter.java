package com.github.br.gdx.simple.visual.novel.viz.settings.painter;

import com.github.br.gdx.simple.visual.novel.api.ElementId;
import com.github.br.gdx.simple.visual.novel.api.node.CompositeNode;
import com.github.br.gdx.simple.visual.novel.api.node.Node;
import com.github.br.gdx.simple.visual.novel.api.node.NodeType;
import com.github.br.gdx.simple.visual.novel.viz.DotUtils;
import com.github.br.gdx.simple.visual.novel.viz.NodeWrapperViz;
import com.github.br.gdx.simple.visual.novel.viz.data.NodeElementType;
import com.github.br.gdx.simple.visual.novel.viz.data.NodeElementTypeId;
import com.github.br.gdx.simple.visual.novel.viz.data.NodeElementVizData;
import com.github.br.gdx.simple.visual.novel.viz.settings.DotVizSettings;
import com.github.br.gdx.simple.visual.novel.viz.settings.color.FullModeColorSchema;
import com.github.br.gdx.simple.visual.novel.viz.settings.color.DotColorsSchema;

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

        StringBuilder builder = new StringBuilder();
        builder
                .append(nodeId)
                .append(" [\n")
                .append("label=")
                .append(createLabelFullNodeInfo(settings, nodeType, value))
                .append("\n")
                .append("shape=").append("plaintext").append("\n");

        if (isVisited) {
            builder.append("color=").append(colorSchema.getVisitedNodesColor()).append("\n");
            builder.append("style=filled, fillcolor=").append(colorSchema.getVisitedNodesColor()).append("\n");
        } else if (isCurrentNode) {
            String color = isHasException ? colorSchema.getErrorNodeColor() : colorSchema.getCurrentNodeColor();
            builder.append("color=").append(color).append("\n");
            builder.append("style=filled, fillcolor=").append(color).append("\n");
        }

        builder.append("];\n");
        return builder.toString();
    }

    private String createLabelFullNodeInfo(DotVizSettings settings, NodeElementType nodeType, NodeElementVizData value) {
        return createLabelFullNodeInfo(settings, nodeType, value.getNodeWrapper());
    }

    private String createLabelFullNodeInfo(DotVizSettings settings, NodeElementType nodeType, NodeWrapperViz<?> nodeWrapper) {
        StringBuilder builder = new StringBuilder();
        builder
                .append("<<TABLE BORDER=\"0\" CELLBORDER=\"1\" CELLSPACING=\"0\">")
                .append(createLabelFullNodeInfo(settings, nodeType, nodeWrapper, 0))
                .append("</TABLE>>");

        return builder.toString();
    }

    private String createLabelFullNodeInfo(DotVizSettings settings, NodeElementType nodeType, NodeWrapperViz<?> nodeWrapper, int level) {
        Node<?, ?> node = nodeWrapper.node;

        StringBuilder result = new StringBuilder();
        result.append(createFullInfoHeader(settings, nodeType, nodeWrapper, level));
        result.append(createFullInfoBody(settings, nodeType, nodeWrapper, settings.getVizDataParamExtractor().extractNodeParams(node)));

        if (node instanceof CompositeNode) {
            level++;
            CompositeNode<?, ?> compositeNode = ((CompositeNode<?, ?>) node);
            for (Node<?, ?> compositeInnerNode : compositeNode.getNodes()) {
                NodeElementType nodeElementType = extractNodeElementType(settings, compositeInnerNode);

                NodeWrapperViz<?> compositeInnerNodeWrapper = NodeWrapperViz.of(ElementId.of(""), nodeWrapper.nodeId, compositeInnerNode, NodeType.IMMEDIATELY);
                result.append(createLabelFullNodeInfo(settings, nodeElementType, compositeInnerNodeWrapper, level));
            }
        }

        return result.toString();
    }

    protected String createFullInfoHeader(DotVizSettings settings, NodeElementType nodeType, NodeWrapperViz<?> nodeWrapper, int level) {
        String nodeId = nodeWrapper.nodeId.getId();
        Node<?, ?> node = nodeWrapper.node;

        String spaces = DotUtils.repeatString(" ", level * 2);
        String fullNodeId = nodeId + " " + createAdditionalHeaderInfoNodeId(node);

        String align = "";
        if (level > 0) {
            fullNodeId = spaces + fullNodeId;
            align = "ALIGN=\"LEFT\"";
            spaces += "    "; // для отступа в названиях классов и параметрах
        }

        NodeElementType.FullViz fullData = nodeType.getFullData();
        String headerColor = fullData.headerColor;

        StringBuilder builder = new StringBuilder();
        if (level == 0) {
            builder
                    .append("<TR>").append("<TD BGCOLOR=\"").append(headerColor).append("\" ").append(align).append(">")
                    .append(fullNodeId)
                    .append("</TD>")
                    .append("</TR>")
            ;
            builder
                    .append("<TR>").append("<TD BGCOLOR=\"").append(headerColor).append("\" ").append(align).append(">")
                    .append(nodeWrapper.nodeType.name())
                    .append("</TD>")
                    .append("</TR>")
            ;
        }

        builder
                .append("<TR>")
                .append("<TD BGCOLOR=\"" + headerColor + "\" ").append(align).append(">")
                .append(spaces).append(node.getClass().getName())
                .append("</TD>")
                .append("</TR>");

        return builder.toString();
    }

    protected String createFullInfoBody(
            DotVizSettings settings,
            NodeElementType nodeType,
            NodeWrapperViz<?> nodeWrapper,
            Map<String, String> customParams
    ) {
        if (customParams == null || customParams.isEmpty()) {
            return "";
        }

        FullModeColorSchema fullModeSchema = nodeType.getFullData().colorSchema;
        StringBuilder builder = new StringBuilder();
        for (Map.Entry<String, String> entry : customParams.entrySet()) {
            builder
                    .append("<TR>").append("<TD BGCOLOR=\"").append(fullModeSchema.getCustomParamNameColor(nodeWrapper, customParams, entry.getKey())).append("\"").append(">")
                    .append(entry.getKey())
                    .append("</TD>").append("<TD BGCOLOR=\"").append(fullModeSchema.getCustomParamValueColor(nodeWrapper, customParams, entry.getKey(), entry.getValue())).append("\"").append(">")
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

    private NodeElementType extractNodeElementType(DotVizSettings settings, Node<?, ?> compositeInnerNode) {
        DotColorsSchema colorSchema = settings.getColorSchema();
        NodeElementTypeId nodeElementTypeId = colorSchema.getTypeDeterminant().determineType(compositeInnerNode);
        return colorSchema.getElementsTypes().get(nodeElementTypeId);
    }

}
