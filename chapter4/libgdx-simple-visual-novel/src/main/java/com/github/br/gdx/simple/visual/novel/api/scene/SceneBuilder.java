package com.github.br.gdx.simple.visual.novel.api.scene;

import com.github.br.gdx.simple.visual.novel.api.ElementId;
import com.github.br.gdx.simple.visual.novel.api.context.UserContext;
import com.github.br.gdx.simple.visual.novel.api.node.Node;
import com.github.br.gdx.simple.visual.novel.api.node.NodeVisitor;

public class SceneBuilder<UC extends UserContext, V extends NodeVisitor<?>> implements NodeRegistrationBuilder<UC, V> {

    private final SceneNodeBuilder<UC, V> sceneNodeBuilder;

    public SceneBuilder(SceneConfig config) {
        sceneNodeBuilder = new SceneNodeBuilder<>(config);
    }

    public ElementId registerNode(Node<UC, V> node) {
        return this.sceneNodeBuilder.addNode(node);
    }

    public ElementId registerNode(ElementId nodeId, Node<UC, V> node) {
        return this.sceneNodeBuilder.addNode(nodeId, node);
    }

    public ElementId registerSceneLink(ElementId sceneId) {
        return this.sceneNodeBuilder.addScene(sceneId);
    }

    public ElementId registerSceneLink(ElementId nodeId, ElementId sceneId) {
        return this.sceneNodeBuilder.addScene(nodeId, sceneId);
    }

    public SceneNodeBuilder<UC, V> graph(RegisterConsumer<UC, V> registerConsumer) {
        registerConsumer.consume(this);

        return graph();
    }

    public SceneNodeBuilder<UC, V> graph() {
        return sceneNodeBuilder;
    }

}
