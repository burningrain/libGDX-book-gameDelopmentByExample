package com.github.br.gdx.simple.visual.novel;

import com.github.br.gdx.simple.visual.novel.api.ElementId;
import com.github.br.gdx.simple.visual.novel.api.node.NodeType;
import com.github.br.gdx.simple.visual.novel.api.plot.DefaultSceneManager;
import com.github.br.gdx.simple.visual.novel.api.plot.Plot;
import com.github.br.gdx.simple.visual.novel.api.plot.PlotConfig;
import com.github.br.gdx.simple.visual.novel.api.plot.SceneSupplier;
import com.github.br.gdx.simple.visual.novel.api.scene.Scene;
import com.github.br.gdx.simple.visual.novel.api.scene.SceneBuilder;
import com.github.br.gdx.simple.visual.novel.api.scene.SceneConfig;
import com.github.br.gdx.simple.visual.novel.impl.CustomNodeVisitor;
import com.github.br.gdx.simple.visual.novel.impl.TestNode;
import com.github.br.gdx.simple.visual.novel.impl.TestUserContext;
import org.junit.Assert;
import org.junit.Test;

public class NodeTypeTest {

    @Test
    public void testWaitingInputScene() {
        TestUserContext userContext = new TestUserContext();
        final SceneBuilder<TestUserContext, CustomNodeVisitor> sceneBuilder = Scene.builder(
                SceneConfig.builder()
                        .setDefaultNodeType(NodeType.WAITING_INPUT)
                        .build()
        );

        ElementId node1 = sceneBuilder.registerNode(new TestNode<TestUserContext>());
        ElementId node2 = sceneBuilder.registerNode(new TestNode<TestUserContext>());
        ElementId node3 = sceneBuilder.registerNode(new TestNode<TestUserContext>());
        ElementId node4 = sceneBuilder.registerNode(new TestNode<TestUserContext>());

        final Scene<TestUserContext, CustomNodeVisitor> scene = sceneBuilder.graph()
                .node(node1).to()
                .node(node2).to()
                .node(node3).to()
                .node(node4).end()
                .build();

        System.out.println(scene.toString());

        ElementId one = ElementId.of("one");
        Plot<TestUserContext, CustomNodeVisitor> plot = Plot.<TestUserContext, CustomNodeVisitor>builder(PlotConfig.builder().build())
                .setSceneManager(new DefaultSceneManager<TestUserContext, CustomNodeVisitor>()
                        .addScene(one, new SceneSupplier<TestUserContext, CustomNodeVisitor>() {

                                    @Override
                                    public Scene<TestUserContext, CustomNodeVisitor> get() {
                                        return scene;
                                    }
                                }
                        ))
                .setBeginSceneId(one)
                .build();

        plot.execute(userContext);
        plot.execute(userContext);
        plot.execute(userContext);
        boolean result = plot.execute(userContext);
        Assert.assertTrue(result);
    }

    @Test
    public void testNotWaitingScene() {
        TestUserContext userContext = new TestUserContext();
        final SceneBuilder<TestUserContext, CustomNodeVisitor> sceneBuilder = Scene.builder(
                SceneConfig.builder()
                        .setDefaultNodeType(NodeType.NOT_WAITING)
                        .build()
        );

        ElementId node1 = sceneBuilder.registerNode(new TestNode<TestUserContext>());
        ElementId node2 = sceneBuilder.registerNode(new TestNode<TestUserContext>());
        ElementId node3 = sceneBuilder.registerNode(new TestNode<TestUserContext>());
        ElementId node4 = sceneBuilder.registerNode(new TestNode<TestUserContext>());

        final Scene<TestUserContext, CustomNodeVisitor> scene = sceneBuilder.graph()
                .node(node1).to()
                .node(node2).to()
                .node(node3).to()
                .node(node4).end()
                .build();

        System.out.println(scene.toString());

        ElementId one = ElementId.of("one");
        Plot<TestUserContext, CustomNodeVisitor> plot = Plot.<TestUserContext, CustomNodeVisitor>builder(PlotConfig.builder().build())
                .setSceneManager(new DefaultSceneManager<TestUserContext, CustomNodeVisitor>()
                        .addScene(one, new SceneSupplier<TestUserContext, CustomNodeVisitor>() {

                                    @Override
                                    public Scene<TestUserContext, CustomNodeVisitor> get() {
                                        return scene;
                                    }
                                }
                        ))
                .setBeginSceneId(one)
                .build();

        boolean result = plot.execute(userContext);
        Assert.assertTrue(result);
    }

    @Test
    public void testMixedNodesScene() {
        TestUserContext userContext = new TestUserContext();
        final SceneBuilder<TestUserContext, CustomNodeVisitor> sceneBuilder = Scene.builder(
                SceneConfig.builder()
                        .build()
        );

        // статус первой ноды (node1) будет проигнорирован, так как она возвращает NEXT
        ElementId node1 = sceneBuilder.registerNode(new TestNode<TestUserContext>(), NodeType.WAITING_INPUT);
        ElementId node2 = sceneBuilder.registerNode(new TestNode<TestUserContext>(), NodeType.NOT_WAITING);
        ElementId node3 = sceneBuilder.registerNode(new TestNode<TestUserContext>(),  NodeType.WAITING_INPUT);
        ElementId node4 = sceneBuilder.registerNode(new TestNode<TestUserContext>(), NodeType.NOT_WAITING);

        final Scene<TestUserContext, CustomNodeVisitor> scene = sceneBuilder.graph()
                .node(node1).to()
                .node(node2).to()
                .node(node3).to()
                .node(node4).end()
                .build();

        System.out.println(scene.toString());

        ElementId one = ElementId.of("one");
        Plot<TestUserContext, CustomNodeVisitor> plot = Plot.<TestUserContext, CustomNodeVisitor>builder(PlotConfig.builder().build())
                .setSceneManager(new DefaultSceneManager<TestUserContext, CustomNodeVisitor>()
                        .addScene(one, new SceneSupplier<TestUserContext, CustomNodeVisitor>() {

                                    @Override
                                    public Scene<TestUserContext, CustomNodeVisitor> get() {
                                        return scene;
                                    }
                                }
                        ))
                .setBeginSceneId(one)
                .build();

        plot.execute(userContext);
        boolean result = plot.execute(userContext);
        Assert.assertTrue(result);
    }


}
