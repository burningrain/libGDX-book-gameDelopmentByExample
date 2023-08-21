package com.github.br.gdx.simple.visual.novel.api.node;

import com.github.br.gdx.simple.visual.novel.Utils;
import com.github.br.gdx.simple.visual.novel.api.context.PlotContext;
import com.github.br.gdx.simple.visual.novel.api.context.UserContext;

public class CompositeNode<UC extends UserContext> implements Node<UC> {

    private final Node<UC>[] nodes;

    public CompositeNode(Node<UC>[] nodes) {
        this.nodes = Utils.checkNotNull(nodes, "nodes");
        if(this.nodes.length == 0) {
            throw new IllegalArgumentException("The array of nodes must not be empty");
        }
    }

    @Override
    public NodeResult execute(PlotContext<UC> plotContext, boolean isVisited) {
        NodeResult stayResult = null;
        NodeResult nextResult = null;

        for (Node<UC> node : nodes) {
            NodeResult nodeResult = node.execute(plotContext, isVisited);
            if(NodeResultType.STAY.equals(nodeResult.getType())) {
                stayResult = nodeResult;
            }
            if(nodeResult.getSceneTitle() != null) {
                nextResult = nodeResult;
            }
        }

        // хоть одна нода ждет - мы ждем
        if(stayResult != null) {
            return stayResult;
        }

        // ноды готовы двигаться дальше.
        // берем ту, которая знает куда ей дальше идти
        if(nextResult != null) {
            return nextResult;
        }

        // нет нод, которые знают, куда им двигаться дальше. Просто идем дальше
        return new NodeResult(NodeResultType.NEXT);
    }

}
