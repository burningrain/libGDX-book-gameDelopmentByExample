package com.github.br.gdx.simple.visual.novel.viz.settings.color;

import com.github.br.gdx.simple.visual.novel.viz.DefaultElementTypeDeterminant;
import com.github.br.gdx.simple.visual.novel.viz.data.NodeElementType;
import com.github.br.gdx.simple.visual.novel.viz.data.NodeElementTypeId;
import com.github.br.gdx.simple.visual.novel.viz.ElementTypeDeterminant;
import com.github.br.gdx.simple.visual.novel.utils.Utils;

import java.util.*;

public class DotColorsSchema {

    private final String visitedNodesColor;
    private final String errorNodeColor;
    private final Map<NodeElementTypeId, NodeElementType> elementsTypes;
    private final String borderColor;
    private final ElementTypeDeterminant typeDeterminant;
    private final String userContextHeaderColor;

    private DotColorsSchema(Builder builder) {
        this.visitedNodesColor = builder.visitedNodesColor;
        this.errorNodeColor = builder.errorNodeColor;
        this.elementsTypes = createElementsTypesMap(builder.elementsTypes);
        this.borderColor = builder.borderColor;
        this.typeDeterminant = builder.typeDeterminant;
        this.userContextHeaderColor = builder.userContextHeaderColor;
    }

    private Map<NodeElementTypeId, NodeElementType> createElementsTypesMap(List<NodeElementType> elementsTypes) {
        LinkedHashMap<NodeElementTypeId, NodeElementType> result = new LinkedHashMap<>();
        for (NodeElementType elementType : elementsTypes) {
            result.put(elementType.getElementId(), elementType);
        }

        return result;
    }

    public String getVisitedNodesColor() {
        return visitedNodesColor;
    }

    public String getErrorNodeColor() {
        return errorNodeColor;
    }

    public String getUserContextHeaderColor() {
        return userContextHeaderColor;
    }

    public Map<NodeElementTypeId, NodeElementType> getElementsTypes() {
        return elementsTypes;
    }

    public static Builder builder() {
        return new Builder();
    }

    public Builder copy() {
        Builder builder = new Builder();
        builder.setBorderColor(this.borderColor);
        builder.setVisitedNodesColor(this.visitedNodesColor);
        builder.setErrorNodeColor(this.errorNodeColor);

        builder.elementsTypes.clear();
        builder.elementsTypes.addAll(this.elementsTypes.values());

        builder.setElementTypeDeterminant(this.typeDeterminant);

        return builder;
    }

    public String getBorderColor() {
        return borderColor;
    }

    public ElementTypeDeterminant getTypeDeterminant() {
        return typeDeterminant;
    }

    public static class Builder {

        private ElementTypeDeterminant typeDeterminant = new DefaultElementTypeDeterminant();

        private String borderColor = GraphvizColor.GREY;
        private String visitedNodesColor = GraphvizColor.GREEN;
        private String errorNodeColor = GraphvizColor.RED;
        private final ArrayList<NodeElementType> elementsTypes = new ArrayList<NodeElementType>() {{
            add(NodeElementType.SIMPLE_NODE);
            add(NodeElementType.COMPOSITE_NODE);
            add(NodeElementType.SCENE_LINK);
        }};
        private String userContextHeaderColor = GraphvizColor.LIGHT_SALMON;

        public Builder setBorderColor(String borderColor) {
            this.borderColor = borderColor;
            return this;
        }

        public Builder setVisitedNodesColor(String visitedNodesColor) {
            this.visitedNodesColor = Utils.checkNotNull(visitedNodesColor, "visitedNodesColor");
            return this;
        }

        public Builder setErrorNodeColor(String errorNodeColor) {
            this.errorNodeColor = errorNodeColor;
            return this;
        }

        public Builder setUserContextHeaderColor(String userContextHeaderColor) {
            this.userContextHeaderColor = userContextHeaderColor;
            return this;
        }

        public Builder addElementsTypes(List<NodeElementType> legendParams) {
            this.elementsTypes.addAll(legendParams);
            return this;
        }

        public Builder clearAllElementsTypes() {
            this.elementsTypes.clear();
            return this;
        }

        public Builder setElementTypeDeterminant(ElementTypeDeterminant typeDeterminant) {
            this.typeDeterminant = Utils.checkNotNull(typeDeterminant, "typeDeterminant");
            return this;
        }

        public DotColorsSchema build() {
            return new DotColorsSchema(this);
        }

    }

}
