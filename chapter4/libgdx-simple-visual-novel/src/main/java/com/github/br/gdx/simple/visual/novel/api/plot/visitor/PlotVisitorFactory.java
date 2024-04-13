package com.github.br.gdx.simple.visual.novel.api.plot.visitor;

import com.github.br.gdx.simple.visual.novel.api.node.NodeVisitor;

public interface PlotVisitorFactory<V extends NodeVisitor> {

     PlotVisitor<V> createVisitor();

}
