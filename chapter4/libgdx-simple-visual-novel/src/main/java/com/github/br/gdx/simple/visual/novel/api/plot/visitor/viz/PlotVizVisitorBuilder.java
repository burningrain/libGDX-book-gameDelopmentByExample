package com.github.br.gdx.simple.visual.novel.api.plot.visitor.viz;

import com.github.br.gdx.simple.visual.novel.api.ElementId;
import com.github.br.gdx.simple.visual.novel.api.context.CurrentState;
import com.github.br.gdx.simple.visual.novel.api.node.Node;
import com.github.br.gdx.simple.visual.novel.api.node.NodeVisitor;
import com.github.br.gdx.simple.visual.novel.api.plot.visitor.PlotVisitor;
import com.github.br.gdx.simple.visual.novel.api.plot.visitor.viz.settings.DotVizSettings;
import com.github.br.gdx.simple.visual.novel.api.scene.Edge;

import java.util.List;

public class PlotVizVisitorBuilder<T extends NodeVisitor> implements PlotVisitor<T> {

    private final PLotViz<T> pLotViz = new PLotViz<>();
    private final VizConverter converter;

    public PlotVizVisitorBuilder(VizConverter converter) {
        this.converter = converter;
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

    public void visitCurrentNodeId(ElementId sceneId, ElementId nodeId, String currentNodeMessage) {
        pLotViz.setCurrentNode(sceneId, nodeId, currentNodeMessage);
    }

    public void visitPlotPath(List<CurrentState> path) {
        pLotViz.setPlotPath(path);
    }

    public void visitException(Exception ex) {
        pLotViz.setException(ex);
    }

    public String build(DotVizSettings settings) {
        return converter.convert(pLotViz, settings);
    }

}
