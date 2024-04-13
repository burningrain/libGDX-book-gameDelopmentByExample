package com.github.br.gdx.simple.visual.novel.viz;

import com.github.br.gdx.simple.visual.novel.api.ElementId;
import com.github.br.gdx.simple.visual.novel.api.context.CurrentState;
import com.github.br.gdx.simple.visual.novel.api.node.Node;
import com.github.br.gdx.simple.visual.novel.api.node.NodeVisitor;
import com.github.br.gdx.simple.visual.novel.api.scene.Edge;

import java.util.*;

public class PLotViz<T extends NodeVisitor> {

    private final LinkedHashMap<ElementId, SceneViz<T>> scenes = new LinkedHashMap<>();
    private ElementId beginSceneId;
    private CurrentState currentNodeId;
    private String currentNodeMessage;
    private List<CurrentState> path; // nullable
    private Exception exception;

    public void addNode(ElementId sceneId, ElementId nodeId, Node<?, T> node) {
        SceneViz<T> sceneViz = scenes.get(sceneId);
        if (sceneViz == null) {
            sceneViz = new SceneViz<>();
            scenes.put(sceneId, sceneViz);
        }
        sceneViz.putNode(nodeId, node);
    }

    public void addEdge(ElementId sceneId, ElementId nodeId, Edge<?> edge) {
        SceneViz<T> sceneViz = scenes.get(sceneId);
        if (sceneViz == null) {
            sceneViz = new SceneViz<>();
            scenes.put(sceneId, sceneViz);
        }
        sceneViz.putEdge(nodeId, edge);
    }

    public void addBeginNode(ElementId sceneId, ElementId beginNodeId) {
        scenes.get(sceneId).setBeginNodeId(beginNodeId);
    }

    public void setBeginScene(ElementId sceneId) {
        this.beginSceneId = sceneId;
    }

    public void setCurrentNode(ElementId sceneId, ElementId nodeId, String currentNodeMessage) {
        this.currentNodeId = CurrentState.of(sceneId, nodeId);
        this.currentNodeMessage = currentNodeMessage;
    }

    public void setPlotPath(List<CurrentState> path) {
        this.path = path;
    }

    public void setException(Exception ex) {
        this.exception = ex;
    }

    public Map<ElementId, SceneViz<T>> getScenes() {
        return scenes;
    }

    public ElementId getBeginSceneId() {
        return beginSceneId;
    }

    public CurrentState getCurrentNodeId() {
        return currentNodeId;
    }

    public String getCurrentNodeMessage() {
        return currentNodeMessage;
    }

    public List<CurrentState> getPath() {
        return path;
    }

    public Exception getException() {
        return exception;
    }

}
