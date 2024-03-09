package com.github.br.gdx.simple.visual.novel;

import com.github.br.gdx.simple.visual.novel.api.ElementId;
import com.github.br.gdx.simple.visual.novel.api.node.CompositeNode;
import com.github.br.gdx.simple.visual.novel.api.node.Node;
import com.github.br.gdx.simple.visual.novel.api.node.NodeType;
import com.github.br.gdx.simple.visual.novel.api.plot.DefaultSceneManager;
import com.github.br.gdx.simple.visual.novel.api.plot.Plot;
import com.github.br.gdx.simple.visual.novel.api.plot.PlotConfig;
import com.github.br.gdx.simple.visual.novel.api.plot.SceneSupplier;
import com.github.br.gdx.simple.visual.novel.api.plot.visitor.viz.settings.DotVizSettings;
import com.github.br.gdx.simple.visual.novel.api.scene.Scene;
import com.github.br.gdx.simple.visual.novel.api.scene.SceneBuilder;
import com.github.br.gdx.simple.visual.novel.api.scene.SceneConfig;
import com.github.br.gdx.simple.visual.novel.api.scene.SceneNodeBuilder;
import com.github.br.gdx.simple.visual.novel.impl.*;
import org.junit.Test;

public class PlotDotVisitorTest {


    @Test
    public void testDotPlotVisitor() {
        // внутренняя сцена
        final Scene<TestUserContext, CustomNodeVisitor> innerScene = createInnerScene();
        ElementId innerSceneId = ElementId.of("innerScene");
        // внутренняя сцена c внутренней сценой
        ElementId innerSceneWithInnerSceneId = ElementId.of("innerSceneWithInnerScene");
        final Scene<TestUserContext, CustomNodeVisitor> innerSceneWithInnerScene = createInnerSceneWithInnerScene(innerSceneId);
        // внутренняя сцена c ошибочной нодой
        final Scene<TestUserContext, CustomNodeVisitor> innerErrorScene = createInnerErrorScene();
        ElementId innerErrorSceneId = ElementId.of("innerErrorScene");

        // основная сцена
        final SceneBuilder<TestUserContext, CustomNodeVisitor> mainSceneBuilder = Scene.builder(
                SceneConfig.builder()
                        .setDefaultNodeType(NodeType.WAITING_INPUT)
                        .build()
        );

        ElementId mainSceneId = ElementId.of("main_scene");
        ElementId zero = mainSceneBuilder.registerSceneLink(ElementId.of("zero"), innerSceneWithInnerSceneId);
        ElementId a = mainSceneBuilder.registerNode(ElementId.of("a"), createCompositeNode());
        ElementId b = mainSceneBuilder.registerSceneLink(ElementId.of("b"), innerSceneId);
        ElementId c = mainSceneBuilder.registerSceneLink(ElementId.of("c"), innerSceneId);
        ElementId d = mainSceneBuilder.registerSceneLink(ElementId.of("d"), innerErrorSceneId);
        SceneNodeBuilder<TestUserContext, CustomNodeVisitor> mainGraphSceneBuilder = mainSceneBuilder.graph();
        mainGraphSceneBuilder
                .node(zero)
                .to()
                .node(a)
                .to()
                .node(b)
                .to()
                .node(c)
                .to()
                .node(d)
                .end();

        final Scene<TestUserContext, CustomNodeVisitor> mainScene = mainGraphSceneBuilder.build();

        Plot<Integer, TestUserContext, CustomNodeVisitor> plot =
                Plot.<Integer, TestUserContext, CustomNodeVisitor>builder(
                                PlotConfig.<Integer>builder().setGeneratorPlotId(new GeneratorTestPlotIdImpl()).build()
                        )
                        .setDotVizSettings(
                                DotVizSettings.builder()
                                        .setRankDirType(DotVizSettings.RankDirType.LR)
                                        .setNodeInfoType(DotVizSettings.NodeInfoType.FULL)
                                        .setShowLegend(true)
                                        .build()
                        )
                        .setSceneManager(new DefaultSceneManager<TestUserContext, CustomNodeVisitor>()
                                .addScene(mainSceneId, new SceneSupplier<TestUserContext, CustomNodeVisitor>() {
                                            @Override
                                            public Scene<TestUserContext, CustomNodeVisitor> get() {
                                                return mainScene;
                                            }
                                        }
                                )
                                .addScene(innerSceneId, new SceneSupplier<TestUserContext, CustomNodeVisitor>() {
                                            @Override
                                            public Scene<TestUserContext, CustomNodeVisitor> get() {
                                                return innerScene;
                                            }
                                        }
                                )
                                .addScene(innerSceneWithInnerSceneId, new SceneSupplier<TestUserContext, CustomNodeVisitor>() {
                                    @Override
                                    public Scene<TestUserContext, CustomNodeVisitor> get() {
                                        return innerSceneWithInnerScene;
                                    }
                                })
                                .addScene(innerErrorSceneId, new SceneSupplier<TestUserContext, CustomNodeVisitor>() {
                                    @Override
                                    public Scene<TestUserContext, CustomNodeVisitor> get() {
                                        return innerErrorScene;
                                    }
                                })
                        )
                        .setBeginSceneId(mainSceneId)
                        .build();

        final TestUserContext userContext = new TestUserContext();
        plot.execute(1, userContext); // i1
        plot.execute(1);              // i2
        plot.execute(1);              // i3
        plot.execute(1);              // i4
        plot.execute(1);              //  node_1
        plot.execute(1);              //  1
        plot.execute(1);              //  2
        plot.execute(1);              //  3
        plot.execute(1);              //  4
        plot.execute(1);
        plot.execute(1);
        plot.execute(1);
        plot.execute(1);
        plot.execute(1);
        plot.execute(1);
        plot.execute(1);
        plot.execute(1);

        plot.execute(1); // d_1

        System.out.println("--------------------- FULL INFO ---------------------");
        System.out.println(plot.getPlotAsDot(
                DotVizSettings.builder().setNodeInfoType(DotVizSettings.NodeInfoType.FULL).build(),
                1)
        );
        System.out.println("--------------------- SHORT INFO ---------------------");
        System.out.println(plot.getPlotAsDot(
                DotVizSettings.builder().setNodeInfoType(DotVizSettings.NodeInfoType.SHORT).build(),
                1)
        );
    }

    private Node<TestUserContext, CustomNodeVisitor> createCompositeNode() {
        return new CompositeNode<>(new Node[]{
                new TestInnerNodeA<>(),
                new TestInnerNodeB<>(),
                new TestInnerNodeC<>(),
                new TestInnerNodeD<>()
        });
    }

    private Scene<TestUserContext, CustomNodeVisitor> createInnerScene() {
        final SceneBuilder<TestUserContext, CustomNodeVisitor> innerSceneBuilder = Scene.builder(
                SceneConfig.builder()
                        .setDefaultNodeType(NodeType.WAITING_INPUT)
                        .build()
        );
        ElementId one = innerSceneBuilder.registerNode(ElementId.of("1"), new TestNode<TestUserContext>());
        ElementId two = innerSceneBuilder.registerNode(ElementId.of("2"), new TestNode<TestUserContext>());
        ElementId three = innerSceneBuilder.registerNode(ElementId.of("3"), new TestNode<TestUserContext>());
        ElementId four = innerSceneBuilder.registerNode(ElementId.of("4"), new TestNode<TestUserContext>());

        innerSceneBuilder.registerNode(one, new TestNode<TestUserContext>());
        innerSceneBuilder.registerNode(two, new TestNode<TestUserContext>());
        innerSceneBuilder.registerNode(three, new TestNode<TestUserContext>());
        innerSceneBuilder.registerNode(four, new TestNode<TestUserContext>());

        SceneNodeBuilder<TestUserContext, CustomNodeVisitor> innerBuilder = innerSceneBuilder.graph()
                .node(one)
                .to()
                .node(two)
                .to()
                .node(three)
                .to()
                .node(four)
                .end();
        return innerBuilder.build();
    }

    private Scene<TestUserContext, CustomNodeVisitor> createInnerSceneWithInnerScene(ElementId innerSceneId) {
        final SceneBuilder<TestUserContext, CustomNodeVisitor> innerSceneBuilder = Scene.builder(
                SceneConfig.builder()
                        .setDefaultNodeType(NodeType.WAITING_INPUT)
                        .build()
        );
        ElementId one = innerSceneBuilder.registerNode(ElementId.of("i1"), new TestNode<TestUserContext>());
        ElementId two = innerSceneBuilder.registerNode(ElementId.of("i2"), new TestNode<TestUserContext>());
        ElementId three = innerSceneBuilder.registerNode(ElementId.of("i3"), new TestNode<TestUserContext>());
        ElementId four = innerSceneBuilder.registerNode(ElementId.of("i4"), new TestNode<TestUserContext>());
        ElementId five = innerSceneBuilder.registerSceneLink(innerSceneId);

        innerSceneBuilder.registerNode(one, new TestNode<TestUserContext>());
        innerSceneBuilder.registerNode(two, new TestNode<TestUserContext>());
        innerSceneBuilder.registerNode(three, new TestNode<TestUserContext>());
        innerSceneBuilder.registerNode(four, new TestNode<TestUserContext>());

        SceneNodeBuilder<TestUserContext, CustomNodeVisitor> innerBuilder = innerSceneBuilder.graph()
                .node(one)
                .to()
                .node(two)
                .to()
                .node(three)
                .to()
                .node(four)
                .to()
                .node(five)
                .end();
        return innerBuilder.build();
    }

    private Scene<TestUserContext, CustomNodeVisitor> createInnerErrorScene() {
        final SceneBuilder<TestUserContext, CustomNodeVisitor> errorSceneBuilder = Scene.builder(
                SceneConfig.builder()
                        .setDefaultNodeType(NodeType.WAITING_INPUT)
                        .build()
        );

        ElementId one = errorSceneBuilder.registerNode(ElementId.of("1"), new TestErrorNode<TestUserContext>());
        ElementId two = errorSceneBuilder.registerNode(ElementId.of("2"), new TestNode<TestUserContext>());

        SceneNodeBuilder<TestUserContext, CustomNodeVisitor> errorBuilder = errorSceneBuilder.graph()
                .node(one)
                .to()
                .node(two)
                .end();
        return errorBuilder.build();
    }

}
