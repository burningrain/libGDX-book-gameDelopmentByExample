package com.github.br.gdx.simple.visual.novel.api.node;

import com.github.br.gdx.simple.visual.novel.Utils;
import com.github.br.gdx.simple.visual.novel.api.screen.ScreenManager;
import com.github.br.gdx.simple.visual.novel.api.context.PlotContext;
import com.github.br.gdx.simple.visual.novel.api.context.UserContext;

public class CompositeNode<UC extends UserContext, SM extends ScreenManager> implements Node<UC, SM> {

    private final Node<UC, SM>[] nodes;

    public CompositeNode(Node<UC, SM>[] nodes) {
        this.nodes = Utils.checkNotNull(nodes, "nodes");
        if(this.nodes.length == 0) {
            throw new IllegalArgumentException("The array of nodes must not be empty");
        }
    }

    @Override
    public NodeResult execute(float delta, PlotContext<UC, SM> plotContext, boolean isVisited) {
        NodeResult stayResult = null;
        NodeResult nextResult = null;

        for (Node<UC, SM> node : nodes) {
            NodeResult nodeResult = node.execute(delta, plotContext, isVisited);
            if(NodeResultType.STAY.equals(nodeResult.getType())) {
                stayResult = nodeResult;
            }
            if(nodeResult.getNextId() != null) {
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
        return new NodeResult(null, NodeResultType.NEXT);
    }

}
