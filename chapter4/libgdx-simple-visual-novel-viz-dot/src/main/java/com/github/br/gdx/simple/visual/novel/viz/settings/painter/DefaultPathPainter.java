package com.github.br.gdx.simple.visual.novel.viz.settings.painter;

import com.github.br.gdx.simple.visual.novel.api.context.CurrentState;
import com.github.br.gdx.simple.visual.novel.api.context.UserContext;
import com.github.br.gdx.simple.visual.novel.utils.NullObjects;
import com.github.br.gdx.simple.visual.novel.viz.DotUtils;
import com.github.br.gdx.simple.visual.novel.viz.settings.DotVizSettings;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DefaultPathPainter implements PathPainter {

    @Override
    public String createPath(DotVizSettings settings, UserContext userContext, List<CurrentState> states) {
        StringBuilder builder = new StringBuilder();
        builder
                .append("subgraph cluster_path {")
                .append("shape=plaintext;").append("\n")
                .append("style=invisible;").append("\n")
                .append("label = \"user context\";").append("\n")
                .append("user_path [shape=plaintext; label=<").append("\n")
                .append("<TABLE>").append("\n");

        // header
        String visitedNodesColor = settings.getColorSchema().getVisitedNodesColor();
        builder.append("<TR>").append("\n")
                .append("<TD COLSPAN=\"4\" BGCOLOR=\"").append(visitedNodesColor).append("\">").append("\n")
                .append("PATH")
                .append("</TD>").append("\n")
                .append("</TR>").append("\n")
        ;
        builder
                .append("<TR>").append("\n")
                .append("<TD BGCOLOR=\"").append(visitedNodesColor).append("\">").append("№").append("</TD>").append("\n")
                .append("<TD BGCOLOR=\"").append(visitedNodesColor).append("\">").append("level").append("</TD>").append("\n")
                .append("<TD BGCOLOR=\"").append(visitedNodesColor).append("\">").append("scene").append("</TD>").append("\n")
                .append("<TD BGCOLOR=\"").append(visitedNodesColor).append("\">").append("node").append("</TD>").append("\n")
                .append("</TR>").append("\n")
        ;

        Map<Integer, String> indentMap = new HashMap<>();
        int indent = 0;
        indentMap.put(indent, "");

        int size = states.size();
        int counter = 1;
        for (int i = 0; i < size; i++) {
            CurrentState currentState = states.get(i);
            if (NullObjects.UP_TO_PARENT_PROCESS == currentState || NullObjects.DOWN_INTO_SUB_PROCESS == currentState) {
                indent += (NullObjects.UP_TO_PARENT_PROCESS == currentState) ? -1 : 1;
                indentMap.put(indent, DotUtils.repeatString(" ", indent * 4));
                continue;
            }
            builder
                    .append("<TR>").append("\n")
                        .append("<TD ALIGN=\"RIGHT\">").append(counter).append("</TD>").append("\n")
                        .append("<TD ALIGN=\"RIGHT\">").append(indent).append("</TD>").append("\n")
                        .append("<TD>").append(currentState.sceneId.getId()).append("</TD>").append("\n")
                        .append("<TD ALIGN=\"LEFT\">").append(indentMap.get(indent)).append(currentState.nodeId.getId()).append("</TD>").append("\n")
                    .append("</TR>").append("\n")
            ;
            counter++;
        }

        builder.append("</TABLE>").append("\n");
        builder.append(">").append("]").append("\n");
        builder.append("}");
        return builder.toString();
    }

}
