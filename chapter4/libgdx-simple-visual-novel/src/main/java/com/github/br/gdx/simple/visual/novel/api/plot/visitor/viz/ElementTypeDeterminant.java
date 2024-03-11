package com.github.br.gdx.simple.visual.novel.api.plot.visitor.viz;

import com.github.br.gdx.simple.visual.novel.api.node.Node;
import com.github.br.gdx.simple.visual.novel.api.plot.visitor.viz.data.NodeElementTypeId;

public interface ElementTypeDeterminant {

    NodeElementTypeId determineType(Node<?,?> node);

}
