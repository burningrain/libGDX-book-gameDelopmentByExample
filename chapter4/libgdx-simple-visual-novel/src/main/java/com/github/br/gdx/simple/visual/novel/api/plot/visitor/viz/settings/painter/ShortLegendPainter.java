package com.github.br.gdx.simple.visual.novel.api.plot.visitor.viz.settings.painter;

import com.github.br.gdx.simple.visual.novel.api.plot.visitor.viz.data.NodeElementType;

import java.util.Collection;

public class ShortLegendPainter extends LegendPainter {

    @Override
    protected String createNodes(Collection<NodeElementType> fields) {
        StringBuilder builder = new StringBuilder();

        for (NodeElementType field : fields) {
            String name = field.getElementId().getId();
            String value = field.getFullData().headerColor;
            String label = field.getLabel();

            NodeElementType.ShortViz shortData = field.getShortData();

            builder
                    .append(value)
                    .append(" ")
                    .append("[")
                        .append("label=\"\", shape=").append(shortData.shape);

            if (shortData.color != null) {
                builder.append("\nfillcolor=").append(shortData.color).append(", style=filled");
            }

            builder
                    .append("]")
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

}
