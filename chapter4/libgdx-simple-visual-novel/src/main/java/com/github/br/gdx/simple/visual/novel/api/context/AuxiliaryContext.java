package com.github.br.gdx.simple.visual.novel.api.context;

import com.github.br.gdx.simple.visual.novel.utils.StateStack;
import com.github.br.gdx.simple.visual.novel.utils.Utils;
import com.github.br.gdx.simple.visual.novel.api.ElementId;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class AuxiliaryContext {

    public final StateStack stateStack = new StateStack();

    private final ArrayList<CurrentState> path;
    private final HashSet<CurrentState> visitedNodes;

    private boolean isProcessFinished = false;
    private boolean hasError = false;

    public AuxiliaryContext(boolean isMarkVisitedNodes, boolean isSavePath) {
        if (isMarkVisitedNodes) {
            visitedNodes = new HashSet<>();
        } else {
            visitedNodes = null;
        }

        if (isSavePath) {
            path = new ArrayList<>();
        } else {
            path = null;
        }
    }

    public boolean isVisited(ElementId sceneId, ElementId nodeId) {
        Utils.checkNotNull(sceneId, "sceneId");
        if (!isMarkVisitedNodes()) {
            return false;
        }

        return visitedNodes.contains(CurrentState.of(sceneId, nodeId));
    }

    private boolean isMarkVisitedNodes() {
        return visitedNodes != null;
    }

    private boolean isSavePath() {
        return path != null;
    }

    public void addToVisited(CurrentState currentState) {
        if (!(isSavePath() || isMarkVisitedNodes())) {
            return;
        }

        CurrentState copy = currentState.copy();

        if (isSavePath()) {
            path.add(copy);
        }

        if (isMarkVisitedNodes()) {
            visitedNodes.add(copy);
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

    public List<CurrentState> getPath() {
        return path;
    }

}
