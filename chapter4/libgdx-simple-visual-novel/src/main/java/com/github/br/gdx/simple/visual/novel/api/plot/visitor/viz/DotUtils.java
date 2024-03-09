package com.github.br.gdx.simple.visual.novel.api.plot.visitor.viz;

import com.github.br.gdx.simple.visual.novel.api.ElementId;
import com.github.br.gdx.simple.visual.novel.api.node.Node;
import com.github.br.gdx.simple.visual.novel.inner.SceneLinkNode;

public final class DotUtils {

    private DotUtils() {
    }

    public static String repeatString(String symbol, int count) {
        if (symbol == null) {
            return null;
        }

        if (count < 0) {
            throw new IllegalArgumentException("varibale 'count' must not be lower than 0");
        }

        if (count == 0) {
            return "";
        }

        StringBuilder result = new StringBuilder();
        for (int i = 0; i < count; i++) {
            result.append(symbol);
        }

        return result.toString();
    }

    public static boolean isSceneLink(Node<?, ?> node) {
        return node instanceof SceneLinkNode;
    }

    public static ElementId extractSceneLinkId(Node<?, ?> node) {
        return ((SceneLinkNode<?,?>)node).getSceneTitle();
    }

}
