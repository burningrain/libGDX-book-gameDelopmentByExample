package com.github.br.gdx.simple.visual.novel.api.context;

import com.github.br.gdx.simple.visual.novel.Utils;
import com.github.br.gdx.simple.visual.novel.api.ElementId;

import java.util.Objects;

public class FullNodeId implements Comparable<FullNodeId> {

    public final ElementId sceneId;
    public final ElementId nodeId;

    public FullNodeId(ElementId sceneId, ElementId nodeId) {
        this.sceneId = Utils.checkNotNull(sceneId, "sceneId");
        this.nodeId = Utils.checkNotNull(nodeId, "nodeId");
    }

    public ElementId getSceneId() {
        return sceneId;
    }

    public ElementId getNodeId() {
        return nodeId;
    }

    @Override
    public int compareTo(FullNodeId o) {
        int sceneCompareResult = this.sceneId.compareTo(o.sceneId);
        int nodeCompareResult = this.nodeId.compareTo(o.nodeId);
        if(sceneCompareResult == 0 && nodeCompareResult == 0) {
            return 0;
        }

        if(sceneCompareResult != 0) {
            return sceneCompareResult;
        }

        return nodeCompareResult;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FullNodeId that = (FullNodeId) o;
        return sceneId.equals(that.sceneId) && nodeId.equals(that.nodeId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sceneId, nodeId);
    }

}
