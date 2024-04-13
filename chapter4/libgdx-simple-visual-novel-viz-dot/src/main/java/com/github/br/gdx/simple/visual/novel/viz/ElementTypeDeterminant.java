package com.github.br.gdx.simple.visual.novel.viz;

import com.github.br.gdx.simple.visual.novel.api.node.Node;
import com.github.br.gdx.simple.visual.novel.viz.data.NodeElementTypeId;

public interface ElementTypeDeterminant {

    NodeElementTypeId determineType(Node<?,?> node);

}
