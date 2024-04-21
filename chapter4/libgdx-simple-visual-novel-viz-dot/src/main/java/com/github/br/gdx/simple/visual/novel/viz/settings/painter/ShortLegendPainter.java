package com.github.br.gdx.simple.visual.novel.viz.settings.painter;

import com.github.br.gdx.simple.visual.novel.viz.data.NodeElementType;

import java.util.Collection;

public class ShortLegendPainter extends LegendPainter {

    @Override
    protected String createNodes(Collection<NodeElementType> fields) {
        StringBuilder builder = new StringBuilder();

        for (NodeElementType field : fields) {
            String name = field.getElementId().getId();
            String descriptionLabel = field.getDescriptionLabel();
            String label = field.getLabel();

            NodeElementType.ShortViz shortData = field.getShortData();

            builder
                    .append(descriptionLabel)
                    .append(" ")
                    .append("[")
                        .append("label=\"\", shape=").append(shortData.shape);

            if (shortData.borderColor != null) {
                builder.append("\ncolor=").append(shortData.borderColor);
            }
            if (shortData.fillColor != null) {
                builder.append("\nfillcolor=").append(shortData.fillColor).append(", style=filled");
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
