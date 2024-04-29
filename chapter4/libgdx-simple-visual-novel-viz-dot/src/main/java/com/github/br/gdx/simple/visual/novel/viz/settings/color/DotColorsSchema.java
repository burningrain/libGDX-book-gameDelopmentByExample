package com.github.br.gdx.simple.visual.novel.viz.settings.color;

import com.github.br.gdx.simple.visual.novel.viz.DefaultElementTypeDeterminant;
import com.github.br.gdx.simple.visual.novel.viz.data.NodeElementType;
import com.github.br.gdx.simple.visual.novel.viz.data.NodeElementTypeId;
import com.github.br.gdx.simple.visual.novel.viz.ElementTypeDeterminant;
import com.github.br.gdx.simple.visual.novel.utils.Utils;
import com.github.br.gdx.simple.visual.novel.viz.utils.Supplier;

import java.util.*;

public class DotColorsSchema {

    private final String errorNodeColor;
    private final String visitedNodesColor;
    private final String currentNodeColor;
    private final Map<NodeElementTypeId, NodeElementType> elementsTypes;
    private final String borderColor;
    private final ElementTypeDeterminant typeDeterminant;
    private final String userContextHeaderColor;

    private DotColorsSchema(Builder builder) {
        this.errorNodeColor = builder.errorNodeColor;
        this.visitedNodesColor = builder.visitedNodesColor;
        this.currentNodeColor = builder.currentNodeColor;
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

    public String getCurrentNodeColor() {
        return currentNodeColor;
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
        builder.setCurrentNodeColor(this.currentNodeColor);

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

    public String getErrorNodeColor() {
        return errorNodeColor;
    }

    public static class Builder {

        private ElementTypeDeterminant typeDeterminant = new DefaultElementTypeDeterminant();

        private String errorNodeColor = GraphvizColor.RED;
        private String borderColor = GraphvizColor.GREY;
        private String visitedNodesColor = GraphvizColor.GREEN;
        private String currentNodeColor = GraphvizColor.ORANGE;
        private final ArrayList<NodeElementType> elementsTypes = new ArrayList<NodeElementType>() {{
            add(NodeElementType.SIMPLE_NODE);
            add(NodeElementType.COMPOSITE_NODE);
            add(NodeElementType.SCENE_LINK);
        }};
        private String userContextHeaderColor = GraphvizColor.LIGHT_SALMON;

        public Builder setErrorNodeColor(String errorNodeColor) {
            this.errorNodeColor = Utils.checkNotNull(errorNodeColor, "errorNodeColor");
            return this;
        }

        public Builder setBorderColor(String borderColor) {
            this.borderColor = Utils.checkNotNull(borderColor, "borderColor");
            return this;
        }

        public Builder setVisitedNodesColor(String visitedNodesColor) {
            this.visitedNodesColor = Utils.checkNotNull(visitedNodesColor, "visitedNodesColor");
            return this;
        }

        public Builder setCurrentNodeColor(String currentNodeColor) {
            this.currentNodeColor = Utils.checkNotNull(currentNodeColor, "currentNodeColor");
            return this;
        }

        public Builder setUserContextHeaderColor(String userContextHeaderColor) {
            this.userContextHeaderColor = Utils.checkNotNull(userContextHeaderColor, "userContextHeaderColor");
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
            addVisitedNodeElementType();
            addCurrentNodeElementType();
            addErrorNodeElementType();

            return new DotColorsSchema(this);
        }

        private void addVisitedNodeElementType() {
            elementsTypes.add(
                    NodeElementType.SIMPLE_NODE.copy()
                            .setElementId("visited_node")
                            .setLabel("visited node")
                            .setShortDataBuilder(new Supplier<NodeElementType.ShortViz.Builder>() {
                                @Override
                                public void accept(NodeElementType.ShortViz.Builder builder) {
                                    builder.setFillColor(GraphvizColor.WHITE);
                                    builder.setBorderColor(visitedNodesColor);
                                }
                            })
                            .setFullDataBuilder(new Supplier<NodeElementType.FullViz.Builder>() {
                                @Override
                                public void accept(NodeElementType.FullViz.Builder builder) {
                                    builder.setHeaderColor(GraphvizColor.WHITE);
                                    builder.setBorderColor(visitedNodesColor);
                                }
                            })
                            .build()
            );
        }

        private void addCurrentNodeElementType() {
            elementsTypes.add(
                    NodeElementType.SIMPLE_NODE.copy()
                            .setElementId("current_node")
                            .setLabel("current node")
                            .setShortDataBuilder(new Supplier<NodeElementType.ShortViz.Builder>() {
                                @Override
                                public void accept(NodeElementType.ShortViz.Builder builder) {
                                    builder.setFillColor(GraphvizColor.WHITE);
                                    builder.setBorderColor(currentNodeColor);
                                }
                            })
                            .setFullDataBuilder(new Supplier<NodeElementType.FullViz.Builder>() {
                                @Override
                                public void accept(NodeElementType.FullViz.Builder builder) {
                                    builder.setHeaderColor(GraphvizColor.WHITE);
                                    builder.setBorderColor(currentNodeColor);
                                }
                            })
                            .build()
            );
        }

        private void addErrorNodeElementType() {
            elementsTypes.add(
                    NodeElementType.SIMPLE_NODE.copy()
                            .setElementId("error_node")
                            .setLabel("error node")
                            .setShortDataBuilder(new Supplier<NodeElementType.ShortViz.Builder>() {
                                @Override
                                public void accept(NodeElementType.ShortViz.Builder builder) {
                                    builder.setFillColor(GraphvizColor.WHITE);
                                    builder.setBorderColor(errorNodeColor);
                                }
                            })
                            .setFullDataBuilder(new Supplier<NodeElementType.FullViz.Builder>() {
                                @Override
                                public void accept(NodeElementType.FullViz.Builder builder) {
                                    builder.setHeaderColor(GraphvizColor.WHITE);
                                    builder.setBorderColor(errorNodeColor);
                                }
                            })
                            .build()
            );
        }

    }

}
