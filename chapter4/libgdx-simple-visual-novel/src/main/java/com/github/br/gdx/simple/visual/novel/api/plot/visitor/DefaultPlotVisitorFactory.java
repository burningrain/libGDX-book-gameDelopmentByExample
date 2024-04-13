package com.github.br.gdx.simple.visual.novel.api.plot.visitor;

import com.github.br.gdx.simple.visual.novel.api.node.NodeVisitor;

public class DefaultPlotVisitorFactory<V extends NodeVisitor> implements PlotVisitorFactory<V> {

    @Override
    public PlotVisitor<V> createVisitor() {
        return new DefaultPlotVisitor<>();
    }

}
