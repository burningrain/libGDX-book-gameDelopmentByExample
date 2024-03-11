package com.github.br.gdx.simple.visual.novel;

import com.github.br.gdx.simple.visual.novel.api.plot.visitor.viz.settings.color.GraphvizColor;
import org.junit.Test;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

public class GraphvizColorTest {

    @Test
    public void testSvgColors() throws IllegalAccessException {
        StringBuilder builder = new StringBuilder();
        builder.append("digraph G {\n" +
                "charset=\"UTF-8\"\n" +
                "\n" +
                "rankdir=TB\n" +
                "\n" +
                "// https://stackoverflow.com/questions/7599279/how-to-get-horizontal-edges-between-nodes-of-two-or-more-clusters\n" +
                "subgraph cluster_legend { \n" +
                "    label = \"Legend\";\n");


        List<Field> fields = Arrays.asList(GraphvizColor.class.getFields());

        builder.append(createNodes(fields)).append("\n");
        builder.append(createSubgraphDescription(fields)).append("\n");
        builder.append(createSubgraphColor(fields)).append("\n");

        builder.append("edge[style=invisible, dir=none, constraint=false];").append("\n");
        builder.append(createEdges(fields));

        builder.append("}}");

        System.out.println(builder.toString());
    }

    private String createSubgraphColor(List<Field> fields) throws IllegalAccessException {
        StringBuilder builder = new StringBuilder();
        builder.append("subgraph cluster_A {\n" +
                "      label = \"element\";\n" +
                "      style=invisible;\n" +
                "      edge [style=invisible,dir=none];\n" +
                "      node [style=filled,color=white];\n");

        Field prev = null;

        for (Field field : fields) {
            if (prev == null) {
                prev = field;
                continue;
            }

            String value = (String) field.get(GraphvizColor.class);
            String prevValue = (String) prev.get(GraphvizColor.class);

            builder.append(prevValue).append(" -> ").append(value).append(";\n");
            prev = field;
        }

        builder.append("}");
        return builder.toString();
    }

    private String createSubgraphDescription(List<Field> fields) {
        StringBuilder builder = new StringBuilder();
        builder
                .append("subgraph cluster_B {\n" +
                        "      label = \"description\";\n" +
                        "      style=invisible;\n" +
                        "      edge [style=invisible,dir=none];\n" +
                        "      node [style=filled,color=white];\n");

        Field prev = null;

        for (Field field : fields) {
            if (prev == null) {
                prev = field;
                continue;
            }

            String name = field.getName();
            String prevName = prev.getName();

            builder.append(prevName).append(" -> ").append(name).append(";\n");
            prev = field;
        }

        builder.append("}");
        return builder.toString();
    }

    private String createEdges(List<Field> fields) throws IllegalAccessException {
        StringBuilder builder = new StringBuilder();
        for (Field field : fields) {
            String name = field.getName();
            String value = (String) field.get(GraphvizColor.class);

            builder.append(value).append(" -> ").append(name).append(";\n");
        }

        return builder.toString();
    }

    public String createNodes(List<Field> fields) throws IllegalAccessException {
        StringBuilder builder = new StringBuilder();

        for (Field field : fields) {
            String name = field.getName();
            String value = (String) field.get(GraphvizColor.class);

            builder
                    .append(value)
                    .append(" ")
                    .append("[label=\"\", shape=rounded, style=filled, fillcolor=").append(value).append("]")
                    .append("\n")
            ;
            builder
                    .append(name)
                    .append(" ")
                    .append("[label=").append(value).append(", shape=plaintext]")
                    .append("\n")
            ;
        }

        return builder.toString();
    }

}
