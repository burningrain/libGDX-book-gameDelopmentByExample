package com.github.br.gdx.simple.visual.novel.api.context;

import com.github.br.gdx.simple.visual.novel.Utils;
import com.github.br.gdx.simple.visual.novel.api.ElementId;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class AuxiliaryContext {

    public final CurrentState currentState = new CurrentState();

    private final ArrayList<FullNodeId> path;
    private final HashSet<FullNodeId> visitedNodes;

    private boolean isProcessFinished = false;
    private boolean hasError = false;

    public AuxiliaryContext(boolean isMarkVisitedNodes, boolean isSavePath) {
        if (isMarkVisitedNodes) {
            visitedNodes = new HashSet<>();
        } else {
            visitedNodes = null;
        }

        if(isSavePath) {
            path = new ArrayList<>();
        } else {
            path = null;
        }
    }

    public boolean isVisited(ElementId sceneId, ElementId nodeId) {
        Utils.checkNotNull(sceneId, "sceneId");
        if(!isMarkVisitedNodes()) {
            return false;
        }

        return visitedNodes.contains(new FullNodeId(sceneId, nodeId));
    }

    private boolean isMarkVisitedNodes() {
        return visitedNodes != null;
    }

    private boolean isSavePath() {
        return path != null;
    }

    public void addToVisited(ElementId sceneId, ElementId nodeId) {
        FullNodeId fullNodeId = new FullNodeId(sceneId, nodeId);
        if(isSavePath()) {
            path.add(fullNodeId);
        }

        if(isMarkVisitedNodes()) {
            visitedNodes.add(fullNodeId);
        }

    }

    public boolean isProcessFinished() {
        return isProcessFinished;
    }

    public void setProcessFinished(boolean processFinished) {
        isProcessFinished = processFinished;
    }

    public void setHasError(boolean hasError) {
        this.hasError = hasError;
    }

    public boolean isHasError() {
        return hasError;
    }

    public List<FullNodeId> getPath() {
        return path;
    }

}
