package com.github.br.gdx.simple.visual.novel.api.scene;

import com.github.br.gdx.simple.visual.novel.utils.Utils;
import com.github.br.gdx.simple.visual.novel.api.node.NodeResult;
import com.github.br.gdx.simple.visual.novel.api.node.NodeType;

public class SceneResult {

    private final NodeResult nodeResult;
    private final NodeType nodeType;      // null когда следующую ноду не знаем или ее нет (конец сценария/процесса)

    public SceneResult(NodeResult nodeResult, NodeType nodeType) {
        this.nodeResult = Utils.checkNotNull(nodeResult, "nodeResult");
        this.nodeType = nodeType;
    }

    public NodeResult getNodeResult() {
        return nodeResult;
    }

    public NodeType getNodeType() {
        return nodeType;
    }

}
