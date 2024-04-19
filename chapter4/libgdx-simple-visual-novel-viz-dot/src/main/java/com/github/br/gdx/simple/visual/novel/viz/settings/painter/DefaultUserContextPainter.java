package com.github.br.gdx.simple.visual.novel.viz.settings.painter;

import com.github.br.gdx.simple.visual.novel.api.context.UserContext;
import com.github.br.gdx.simple.visual.novel.viz.settings.DotVizSettings;
import com.github.br.gdx.simple.visual.novel.viz.settings.color.DotColorsSchema;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.Map;

public class DefaultUserContextPainter implements UserContextPainter {

    public static final String INDENT = "    ";

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

        result.append("<TABLE>").append("\n");
        result.append("<TR>").append("\n")
                .append("<TD COLSPAN=\"3\" BGCOLOR=\"").append(userContextHeaderColor).append("\">").append("\n")
                .append(userContext.getClass().getSimpleName())
                .append("</TD>").append("\n")
                .append("</TR>").append("\n")
        ;
        result.append("<TR>").append("\n");
        result
                .append("<TD BGCOLOR=\"").append(userContextHeaderColor).append("\">").append("name").append("</TD>")
                .append("<TD BGCOLOR=\"").append(userContextHeaderColor).append("\">").append("type").append("</TD>")
                .append("<TD BGCOLOR=\"").append(userContextHeaderColor).append("\">").append("value").append("</TD>")

                .append("</TR>").append("\n")
        ;
        try {
            fillUserParams(result, "", userContext);
        } catch (Exception e) {
            result.append("<TR>").append("\n");
            result.append("<TD COLSPAN=\"3\" BGCOLOR=\"").append(colorSchema.getErrorNodeColor()).append("\">").append("\n")
                    .append(e.getMessage())
                    .append("</TD>").append("\n");
            result.append("</TR>").append("\n");
        }
        result.append("</TABLE>").append("\n");

        result.append(">").append("]").append("\n");
        result.append("}");
        return result.toString();
    }

    private void fillUserParams(StringBuilder result, String indent, Object object) throws IllegalAccessException {
        Class<?> clazz = object.getClass();
        Field[] declaredFields = clazz.getDeclaredFields();
        for (Field field : declaredFields) {
            String name = field.getName();

            Type genericType = field.getGenericType();
            String classNameString = genericType.toString();

            field.setAccessible(true);
            Object v = field.get(object);
            if (v == null) {
                v = "";
            }
            fillParam(result, indent, name, classNameString, v);
        }
    }

    private void fillParam(StringBuilder result, String indent, String fieldName, String fieldClass, Object obj) {
        String value = "";
        if (!(obj instanceof Iterable || obj instanceof Map)) {
            value = obj.toString();
        }

        result
                .append("<TR>").append("\n")
                .append("<TD ALIGN=\"LEFT\">").append("<![CDATA[").append(indent).append(fieldName).append("]]>").append("</TD>").append("\n")
                .append("<TD>").append("<![CDATA[").append(fieldClass).append("]]>").append("</TD>").append("\n")
                .append("<TD>").append("<![CDATA[").append(value).append("]]>").append("</TD>").append("\n")
                .append("</TR>").append("\n")
        ;

        Class<?> clazz = obj.getClass();
        // если тип простой, то дальше ничего не делаем
        if (byte.class.isAssignableFrom(clazz) ||
                short.class.isAssignableFrom(clazz) ||
                int.class.isAssignableFrom(clazz) ||
                long.class.isAssignableFrom(clazz) ||
                float.class.isAssignableFrom(clazz) ||
                double.class.isAssignableFrom(clazz) ||
                boolean.class.isAssignableFrom(clazz) ||
                Byte.class.isAssignableFrom(clazz) ||
                Short.class.isAssignableFrom(clazz) ||
                Integer.class.isAssignableFrom(clazz) ||
                Long.class.isAssignableFrom(clazz) ||
                Float.class.isAssignableFrom(clazz) ||
                Double.class.isAssignableFrom(clazz) ||
                Number.class.isAssignableFrom(clazz) ||
                Boolean.class.isAssignableFrom(clazz) ||
                String.class.isAssignableFrom(clazz)
        ) {
            return;
        }
        if (Object[].class.isAssignableFrom(clazz)) {
            Object[] array = (Object[]) obj;
            for (int i = 0; i < array.length; i++) {
                fillParam(result, indent + INDENT, "[" + i + "]", array[i].getClass().getName(), array[i]);
            }
        } else if (Iterable.class.isAssignableFrom(clazz)) {
            Iterable<?> list = (Iterable<?>) obj;
            int i = 0;
            for (Object o : list) {
                fillParam(result, indent + INDENT, "[" + i + "]", o.getClass().getName(), o);
                i++;
            }
        } else if (Map.class.isAssignableFrom(clazz)) {
            for (Map.Entry<?, ?> entry : ((Map<?, ?>) obj).entrySet()) {
                fillParam(result, indent + INDENT, entry.getKey().toString(), entry.getClass().getName(), entry.getValue());
            }
        }
    }

    //TODO 1) подсветить текущую ноду 2) научиться выводить diff между конеткстом в заранее помеченных точках процесса

}
