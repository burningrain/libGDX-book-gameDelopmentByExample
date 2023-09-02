package com.github.br.gdx.simple.visual.novel.api.scene;

import com.github.br.gdx.simple.visual.novel.Utils;
import com.github.br.gdx.simple.visual.novel.api.ElementId;
import com.github.br.gdx.simple.visual.novel.api.context.UserContext;
import com.github.br.gdx.simple.visual.novel.api.node.Node;
import com.github.br.gdx.simple.visual.novel.api.node.NodeType;
import com.github.br.gdx.simple.visual.novel.api.node.NodeVisitor;

public class SceneBuilder<UC extends UserContext, V extends NodeVisitor<?>> implements NodeRegistrationBuilder<UC, V> {

    private final SceneNodeBuilder<UC, V> sceneNodeBuilder;
    private final SceneConfig config;

    public SceneBuilder(SceneConfig config) {
        this.config = Utils.checkNotNull(config, "config");
        sceneNodeBuilder = new SceneNodeBuilder<>(config);
    }

    public ElementId registerNode(Node<UC, V> node) {
        return this.sceneNodeBuilder.addNode(node, config.getDefaultNodeType());
    }

    public ElementId registerNode(Node<UC, V> node, NodeType nodeType) {
        return this.sceneNodeBuilder.addNode(node, nodeType);
    }

    public ElementId registerNode(ElementId nodeId, Node<UC, V> node) {
        return this.sceneNodeBuilder.addNode(nodeId, node, config.getDefaultNodeType());
    }

    public ElementId registerNode(ElementId nodeId, Node<UC, V> node, NodeType nodeType) {
        return this.sceneNodeBuilder.addNode(nodeId, node, nodeType);
    }

    public ElementId registerSceneLink(ElementId sceneId) {
        return this.sceneNodeBuilder.addScene(sceneId, config.getDefaultNodeType());
    }

    public ElementId registerSceneLink(ElementId sceneId, NodeType nodeType) {
        return this.sceneNodeBuilder.addScene(sceneId, nodeType);
    }

    public ElementId registerSceneLink(ElementId nodeId, ElementId sceneId) {
        return this.sceneNodeBuilder.addScene(nodeId, sceneId, config.getDefaultNodeType());
    }

    public ElementId registerSceneLink(ElementId nodeId, ElementId sceneId, NodeType nodeType) {
        return this.sceneNodeBuilder.addScene(nodeId, sceneId, nodeType);
    }

    public SceneNodeBuilder<UC, V> graph(RegisterConsumer<UC, V> registerConsumer) {
        registerConsumer.consume(this);

        return graph();
    }

    public SceneNodeBuilder<UC, V> graph() {
        return sceneNodeBuilder;
    }

}
