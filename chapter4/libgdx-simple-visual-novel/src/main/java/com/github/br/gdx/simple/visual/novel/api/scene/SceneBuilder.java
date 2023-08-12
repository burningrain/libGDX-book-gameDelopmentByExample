package com.github.br.gdx.simple.visual.novel.api.scene;

import com.github.br.gdx.simple.visual.novel.api.ElementId;
import com.github.br.gdx.simple.visual.novel.api.context.UserContext;
import com.github.br.gdx.simple.visual.novel.api.node.Node;
import com.github.br.gdx.simple.visual.novel.api.screen.ScreenManager;
import com.github.br.gdx.simple.visual.novel.api.screen.ScreenManagerConsumer;

public class SceneBuilder<UC extends UserContext, SM extends ScreenManager> implements NodeRegistrationBuilder<UC, SM> {

    private final SceneNodeBuilder<UC, SM> sceneNodeBuilder;

    public SceneBuilder(SceneConfig<SM> config) {
        sceneNodeBuilder = new SceneNodeBuilder<>(config);
    }

    public ElementId registerNode(Node<UC, SM> node) {
        return this.sceneNodeBuilder.addNode(node);
    }

    public ElementId registerNode(ElementId nodeId, Node<UC, SM> node) {
        return this.sceneNodeBuilder.addNode(nodeId, node);
    }

    public ElementId registerSceneLink(ElementId sceneId) {
        return this.sceneNodeBuilder.addScene(sceneId);
    }

    public ElementId registerSceneLink(ElementId nodeId, ElementId sceneId) {
        return this.sceneNodeBuilder.addScene(nodeId, sceneId);
    }

    public ElementId registerScreenNode(ScreenManagerConsumer<UC, SM> consumer) {
        return this.sceneNodeBuilder.addNode(consumer);
    }

    public ElementId registerScreenNode(ElementId nodeId, ScreenManagerConsumer<UC, SM> consumer) {
        return this.sceneNodeBuilder.addNode(nodeId, consumer);
    }

    public SceneNodeBuilder<UC, SM> graph(RegisterConsumer<UC, SM> registerConsumer) {
        registerConsumer.consume(this);

        return graph();
    }

    public SceneNodeBuilder<UC, SM> graph() {
        return sceneNodeBuilder;
    }

}
