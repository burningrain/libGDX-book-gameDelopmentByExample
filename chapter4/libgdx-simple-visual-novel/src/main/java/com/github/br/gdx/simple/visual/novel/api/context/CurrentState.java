package com.github.br.gdx.simple.visual.novel.api.context;

import com.github.br.gdx.simple.visual.novel.api.ElementId;

import java.util.Objects;

public class CurrentState implements Comparable<CurrentState> {

    public ElementId sceneId; // nullable because the first scene
    public ElementId nodeId;  // nullable because the first state

    private CurrentState() {
    }

    public static CurrentState of(ElementId sceneId, ElementId nodeId) {
        CurrentState currentState = new CurrentState();
        currentState.sceneId = sceneId;
        currentState.nodeId = nodeId;

        return currentState;
    }

    public CurrentState copy() {
        CurrentState result = new CurrentState();
        result.sceneId = this.sceneId;
        result.nodeId = this.nodeId;
        return result;
    }

    @Override
    public String toString() {
        return "sceneId=[" + sceneId + "], nodeId=[" + nodeId + "]";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CurrentState that = (CurrentState) o;
        return Objects.equals(sceneId, that.sceneId) && Objects.equals(nodeId, that.nodeId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sceneId, nodeId);
    }


    @Override
    public int compareTo(CurrentState o) {
        int sceneResult = this.sceneId.compareTo(o.sceneId);
        if (sceneResult != 0) {
            return sceneResult;
        }

        return this.nodeId.compareTo(o.nodeId);
    }

}
