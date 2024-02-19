package com.github.br.gdx.simple.visual.novel.api.plot.visitor.viz.data;

public enum NodeElementType {

    COMPOSITE_NODE("Mcircle"),
    SCENE_LINK("doubleoctagon"),
    SIMPLE_NODE("circle"),
    CUSTOM_NODE("plaintext");

    private final String dotShape;

    NodeElementType(String dotShape) {
        this.dotShape = dotShape;
    }

    public String getDotShape() {
        return dotShape;
    }

}
