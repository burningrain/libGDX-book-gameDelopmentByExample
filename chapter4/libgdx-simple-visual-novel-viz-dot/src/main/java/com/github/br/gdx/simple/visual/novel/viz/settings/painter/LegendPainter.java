package com.github.br.gdx.simple.visual.novel.viz.settings.painter;

import com.github.br.gdx.simple.visual.novel.viz.data.NodeElementType;
import com.github.br.gdx.simple.visual.novel.viz.data.NodeElementTypeId;
import com.github.br.gdx.simple.visual.novel.viz.settings.DotVizSettings;
import com.github.br.gdx.simple.visual.novel.viz.settings.color.DotColorsSchema;

import java.util.Collection;
import java.util.Map;

public class LegendPainter {

    public String createLegend(DotVizSettings settings) {
        DotColorsSchema colorSchema = settings.getColorSchema();
        Map<NodeElementTypeId, NodeElementType> legendParams = colorSchema.getElementsTypes();

        StringBuilder builder = new StringBuilder();
        builder.append("subgraph cluster_legend { \n" + "    label = \"Legend\";\n");
        builder.append("color=").append(colorSchema.getBorderColor()).append(";\n");

        Collection<NodeElementType> colorsData = legendParams.values();

        builder.append(createNodes(colorsData)).append("\n");
        builder.append(createSubgraphDescription(colorsData)).append("\n");
        builder.append(createSubgraphColor(colorsData)).append("\n");

        builder.append("edge[style=invisible, dir=none, constraint=false];").append("\n");
        builder.append(createEdges(colorsData));

        builder.append("}");

        return builder.toString();
    }

    private String createSubgraphDescription(Collection<NodeElementType> colorsData) {
        StringBuilder builder = new StringBuilder();
        builder
                .append("subgraph cluster_B {\n" +
                        "      label = \"description\";\n" +
                        "      style=invisible;\n" +
                        "      edge [style=invisible,dir=none];\n" +
                        "      node [style=filled,color=white];\n");

        fillSubgraphDescription(colorsData, builder);

        builder.append("}");
        return builder.toString();
    }

    private String createSubgraphColor(Collection<NodeElementType> colorsData) {
        StringBuilder builder = new StringBuilder();
        builder.append("subgraph cluster_A {\n" +
                "      label = \"element\";\n" +
                "      style=invisible;\n" +
                "      edge [style=invisible,dir=none];\n" +
                "      node [style=filled,color=white];\n");

        fillSubgraphColor(colorsData, builder);

        builder.append("}");
        return builder.toString();
    }

    protected String createNodes(Collection<NodeElementType> fields) {
        StringBuilder builder = new StringBuilder();

        for (NodeElementType field : fields) {
            String name = field.getElementId().getId();
            String value = field.getFullData().headerColor;
            String label = field.getLabel();

            builder
                    .append(value)
                    .append(" ")
                    .append("[label=\"\", shape=rounded, style=filled, fillcolor=").append(value).append("]")
                    .append("\n")
            ;
            builder
                    .append(name)
                    .append(" ")
                    .append("[label=\"").append(label).append("\", shape=plaintext]")
                    .append("\n")
            ;
        }

        return builder.toString();
    }

    protected void fillSubgraphColor(Collection<NodeElementType> colorsData, StringBuilder builder) {
        NodeElementType prev = null;
        for (NodeElementType field : colorsData) {
            if (prev == null) {
                prev = field;
                continue;
            }

            String value = field.getFullData().headerColor;
            String prevValue = prev.getFullData().headerColor;

            builder.append(prevValue).append(" -> ").append(value).append(";\n");
            prev = field;
        }
    }

    protected String createEdges(Collection<NodeElementType> colorsData) {
        StringBuilder builder = new StringBuilder();
        for (NodeElementType field : colorsData) {
            String name = field.getElementId().getId();
            String value = field.getFullData().headerColor;

            builder.append(value).append(" -> ").append(name).append(";\n");
        }

        return builder.toString();
    }

    protected void fillSubgraphDescription(Collection<NodeElementType> colorsData, StringBuilder builder) {
        NodeElementType prev = null;
        for (NodeElementType field : colorsData) {
            if (prev == null) {
                prev = field;
                continue;
            }

            String name = field.getElementId().getId();
            String prevName = prev.getElementId().getId();

            builder.append(prevName).append(" -> ").append(name).append(";\n");
            prev = field;
        }
    }

}
