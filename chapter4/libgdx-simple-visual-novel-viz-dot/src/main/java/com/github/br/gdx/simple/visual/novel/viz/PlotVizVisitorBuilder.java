package com.github.br.gdx.simple.visual.novel.viz;

import com.github.br.gdx.simple.visual.novel.api.ElementId;
import com.github.br.gdx.simple.visual.novel.api.context.CurrentState;
import com.github.br.gdx.simple.visual.novel.api.context.UserContext;
import com.github.br.gdx.simple.visual.novel.api.node.Node;
import com.github.br.gdx.simple.visual.novel.api.node.NodeVisitor;
import com.github.br.gdx.simple.visual.novel.api.plot.visitor.PlotVisitor;
import com.github.br.gdx.simple.visual.novel.utils.Utils;
import com.github.br.gdx.simple.visual.novel.viz.settings.DotVizSettings;
import com.github.br.gdx.simple.visual.novel.api.scene.Edge;

import java.util.List;

public class PlotVizVisitorBuilder<T extends NodeVisitor> implements PlotVisitor<T> {

    private final PLotViz<T> pLotViz = new PLotViz<>();
    private final VizConverter converter;
    private final DotVizSettings settings;

    public PlotVizVisitorBuilder(DotVizSettings settings) {
        this(new DotVizConverter(), settings);
    }

    public PlotVizVisitorBuilder(VizConverter converter, DotVizSettings settings) {
        this.converter = Utils.checkNotNull(converter, "converter");
        this.settings = Utils.checkNotNull(settings, "settings");
    }

    @Override
    public void visitNode(ElementId sceneId, ElementId nodeId, Node<?, T> node) {
        pLotViz.addNode(sceneId, nodeId, node);
    }

    @Override
    public void visitEdge(ElementId sceneId, ElementId nodeId, Edge<?> edge) {
        pLotViz.addEdge(sceneId, nodeId, edge);
    }

    @Override
    public void visitBeginNodeId(ElementId sceneId, ElementId beginNodeId) {
        pLotViz.addBeginNode(sceneId, beginNodeId);
    }

    @Override
    public void visitBeginSceneId(ElementId sceneId) {
        pLotViz.setBeginScene(sceneId);
    }

    @Override
    public void visitCurrentNodeId(ElementId sceneId, ElementId nodeId, String currentNodeMessage) {
        pLotViz.setCurrentNode(sceneId, nodeId, currentNodeMessage);
    }

    @Override
    public void visitPlotPath(List<CurrentState> path) {
        pLotViz.setPlotPath(path);
    }

    @Override
    public void visitException(Exception ex) {
        pLotViz.setException(ex);
    }

    @Override
    public void setUserContext(UserContext userContext) {
        pLotViz.setUserContext(userContext);
    }

    @Override
    public String buildString() {
        return converter.convert(pLotViz, settings);
    }

}
