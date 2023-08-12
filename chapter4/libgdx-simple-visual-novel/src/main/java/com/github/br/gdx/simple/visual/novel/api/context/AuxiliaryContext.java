package com.github.br.gdx.simple.visual.novel.api.context;

import com.github.br.gdx.simple.visual.novel.Utils;
import com.github.br.gdx.simple.visual.novel.api.ElementId;

import java.util.HashSet;

public class AuxiliaryContext {

    public final CurrentState currentState = new CurrentState();
    private HashSet<FullNodeId> visitedNodes;

    private final boolean isMarkVisitedNodes;
    private boolean isProcessFinished = false;

    public AuxiliaryContext(boolean isMarkVisitedNodes) {
        this.isMarkVisitedNodes = isMarkVisitedNodes;
        if (this.isMarkVisitedNodes) {
            visitedNodes = new HashSet<>();
        }
    }

    public boolean isVisited(ElementId sceneId, ElementId nodeId) {
        Utils.checkNotNull(sceneId, "sceneId");
        if(!isMarkVisitedNodes) {
            return false;
        }

        return visitedNodes.contains(new FullNodeId(sceneId, nodeId));
    }

    public void addToVisited(ElementId sceneId, ElementId nodeId) {
        if(!isMarkVisitedNodes) {
            return;
        }

        visitedNodes.add(new FullNodeId(sceneId, nodeId));
    }

    public boolean isProcessFinished() {
        return isProcessFinished;
    }

    public void setProcessFinished(boolean processFinished) {
        isProcessFinished = processFinished;
    }

}
