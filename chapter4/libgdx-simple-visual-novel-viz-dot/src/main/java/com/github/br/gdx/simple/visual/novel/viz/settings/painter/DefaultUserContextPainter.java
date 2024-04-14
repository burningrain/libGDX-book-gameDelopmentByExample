package com.github.br.gdx.simple.visual.novel.viz.settings.painter;

import com.github.br.gdx.simple.visual.novel.api.context.UserContext;
import com.github.br.gdx.simple.visual.novel.viz.settings.DotVizSettings;
import com.github.br.gdx.simple.visual.novel.viz.settings.color.DotColorsSchema;

import java.lang.reflect.Field;

public class DefaultUserContextPainter implements UserContextPainter {

    @Override
    public String createContext(DotVizSettings settings, UserContext userContext) {
        DotColorsSchema colorSchema = settings.getColorSchema();
        String userContextHeaderColor = colorSchema.getUserContextHeaderColor();

        StringBuilder result = new StringBuilder();
        result.append("subgraph cluster_user_context {")
                .append("shape=plaintext;").append("\n")
                .append("style=invisible;").append("\n")
                .append("label = \"user context\";")
                .append("\n");

        result.append("userContext [")
                .append("shape=plaintext; ").append("label=").append("<").append("\n");

        result.append("<table>").append("\n");
        result.append("<tr>").append("\n")
                .append("<td colspan=\"3\" bgcolor=\"").append(userContextHeaderColor).append("\">").append("\n")
                .append(userContext.getClass().getSimpleName())
                .append("</td>").append("\n")
                .append("</tr>").append("\n")
        ;
        result.append("<tr>").append("\n");
        result
                .append("<td bgcolor=\"").append(userContextHeaderColor).append("\">").append("name").append("</td>")
                .append("<td bgcolor=\"").append(userContextHeaderColor).append("\">").append("type").append("</td>")
                .append("<td bgcolor=\"").append(userContextHeaderColor).append("\">").append("value").append("</td>")

                .append("</tr>").append("\n")
        ;
        try {
            fillUserParams(result, "", userContext);
        } catch (Exception e) {
            result.append("<tr>").append("\n");
            result.append("<td colspan=\"3\" bgcolor=\"").append(colorSchema.getErrorNodeColor()).append("\">").append("\n")
                    .append(userContext.getClass().getSimpleName())
                    .append("</td>").append("\n");
            result.append("</tr>").append("\n");
        }
        result.append("</table>").append("\n");

        result.append(">").append("]").append("\n");
        result.append("}");
        return result.toString();
    }

    private void fillUserParams(StringBuilder result, String indent, Object object) throws IllegalAccessException {
        Class<?> clazz = object.getClass();
        Field[] declaredFields = clazz.getDeclaredFields();
        for (Field field : declaredFields) {
            fillParam(result, indent, field, field.get(object));
        }
    }

    private void fillParam(StringBuilder result, String indent, Field field, Object value) throws IllegalAccessException {
        Object v = value == null ? "" : field.get(value);
        result
                .append("<tr>").append("\n")
                .append("<td>").append(indent).append(field.getName()).append("</td>").append("\n")
                .append("<td>").append(indent).append(field.getClass().getName()).append("</td>").append("\n")
                .append("<td>").append(indent).append(v).append("</td>").append("\n")
                .append("</tr>").append("\n")
        ;
    }

}
