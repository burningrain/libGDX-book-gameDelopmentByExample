package com.github.br.gdx.simple.visual.novel.viz;

import com.github.br.gdx.simple.visual.novel.api.node.NodeVisitor;
import com.github.br.gdx.simple.visual.novel.api.plot.visitor.PlotVisitor;
import com.github.br.gdx.simple.visual.novel.api.plot.visitor.PlotVisitorFactory;
import com.github.br.gdx.simple.visual.novel.utils.Utils;
import com.github.br.gdx.simple.visual.novel.viz.settings.DotVizSettings;

public class PlotVizVisitorFactory<V extends NodeVisitor> implements PlotVisitorFactory<V> {

    private final DotVizSettings settings;

    public PlotVizVisitorFactory(DotVizSettings settings) {
        this.settings = Utils.checkNotNull(settings, "settings");
    }

    @Override
    public PlotVisitor<V> createVisitor() {
        return new PlotVizVisitorBuilder<>(settings);
    }

}
