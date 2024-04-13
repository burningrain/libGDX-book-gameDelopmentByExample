package com.github.br.gdx.simple.visual.novel.api.plot.visitor;

import com.github.br.gdx.simple.visual.novel.api.ElementId;
import com.github.br.gdx.simple.visual.novel.api.context.CurrentState;
import com.github.br.gdx.simple.visual.novel.api.node.Node;
import com.github.br.gdx.simple.visual.novel.api.node.NodeVisitor;
import com.github.br.gdx.simple.visual.novel.api.scene.Edge;

import java.util.List;

public class DefaultPlotVisitor<V extends NodeVisitor> implements PlotVisitor<V> {

    private final StringBuilder builder = new StringBuilder();
    private final DefaultNodeVisitor defaultNodeVisitor = new DefaultNodeVisitor();

    @Override
    public void visitNode(ElementId sceneId, ElementId nodeId, Node<?, V> node) {
        node.accept(sceneId, nodeId, (V) defaultNodeVisitor); //TODO кривота тут. про дженерики прочесть все-таки книгу
        builder.append(defaultNodeVisitor.getData());
    }

    @Override
    public void visitEdge(ElementId sceneId, ElementId nodeId, Edge<?> edge) {
        builder.append("scene node: ").append(sceneId).append("\nedge: ").append(nodeId).append("\nfrom: ")
                .append(edge.getSourceId()).append("\nto: ").append(edge.getDestId()).append("\n");
    }

    @Override
    public void visitBeginNodeId(ElementId sceneId, ElementId beginNodeId) {
        builder.append("scene node: ").append(sceneId).append("\nbegin node: ").append(beginNodeId).append("\n");
    }

    @Override
    public void visitBeginSceneId(ElementId sceneId) {
        builder.append("begin scene: ").append(sceneId).append("\n");
    }

    @Override
    public void visitCurrentNodeId(ElementId sceneId, ElementId nodeId, String currentNodeMessage) {
        builder.append("current nodeId: ").append(sceneId).append(" ").append(nodeId).append(" ")
                .append(currentNodeMessage).append("\n");
    }

    @Override
    public void visitPlotPath(List<CurrentState> path) {
        for (CurrentState currentState : path) {
            builder.append(currentState.toString()).append("\n");
        }
    }

    @Override
    public void visitException(Exception ex) {
        builder.append(ex.getMessage()).append("\n");
    }

    @Override
    public String buildString() {
        return builder.toString();
    }

}
