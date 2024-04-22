package com.github.br.gdx.simple.visual.novel.viz;

import com.github.br.gdx.simple.visual.novel.api.ElementId;
import com.github.br.gdx.simple.visual.novel.api.node.CompositeNode;
import com.github.br.gdx.simple.visual.novel.api.node.Node;
import com.github.br.gdx.simple.visual.novel.api.node.NodeType;
import com.github.br.gdx.simple.visual.novel.api.plot.DefaultSceneManager;
import com.github.br.gdx.simple.visual.novel.api.plot.Plot;
import com.github.br.gdx.simple.visual.novel.api.plot.PlotConfig;
import com.github.br.gdx.simple.visual.novel.api.plot.SceneSupplier;
import com.github.br.gdx.simple.visual.novel.viz.data.NodeElementType;
import com.github.br.gdx.simple.visual.novel.viz.data.NodeElementTypeId;
import com.github.br.gdx.simple.visual.novel.viz.impl.*;
import com.github.br.gdx.simple.visual.novel.viz.settings.DotVizSettings;
import com.github.br.gdx.simple.visual.novel.viz.settings.color.DotColorsSchema;
import com.github.br.gdx.simple.visual.novel.viz.settings.color.GraphvizColor;
import com.github.br.gdx.simple.visual.novel.viz.settings.painter.GraphvizShape;
import com.github.br.gdx.simple.visual.novel.api.scene.Scene;
import com.github.br.gdx.simple.visual.novel.api.scene.SceneBuilder;
import com.github.br.gdx.simple.visual.novel.api.scene.SceneConfig;
import com.github.br.gdx.simple.visual.novel.api.scene.SceneNodeBuilder;
import com.github.br.gdx.simple.visual.novel.viz.utils.Supplier;
import org.junit.Test;

import java.util.Arrays;

public class PlotDotVisitorTest {

    private static DotVizSettings createDotVizSettings() {
        return DotVizSettings.builder()
                .setRankDirType(DotVizSettings.RankDirType.LR) //TODO убрать это как режим и сделать простым параметром настройки вывода
                .setNodeInfoType(DotVizSettings.NodeInfoType.SHORT) //TODO убрать это как режим и сделать простым параметром настройки вывода
                .setShowLegend(true) //TODO убрать это как режим и сделать простым параметром настройки вывода
                .setColorsSchema(new Supplier<DotColorsSchema.Builder>() {
                    @Override
                    public void accept(DotColorsSchema.Builder builder) {
                        builder.addElementsTypes(Arrays.asList(
                                CustomTypeDeterminant.TYPE_A,
                                CustomTypeDeterminant.TYPE_B,
                                CustomTypeDeterminant.TYPE_C,
                                CustomTypeDeterminant.TYPE_D
                        ));
                        builder.setElementTypeDeterminant(new CustomTypeDeterminant());
                    }
                })
                .build();
    }

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

        DotVizSettings dotVizSettings = createDotVizSettings();
        Plot<Integer, TestUserContext, CustomNodeVisitor> plot =
                Plot.<Integer, TestUserContext, CustomNodeVisitor>builder(
                                PlotConfig.<Integer>builder().setGeneratorPlotId(new GeneratorTestPlotIdImpl()).build()
                        )
                        .setDefaultPlotVisitorFactory(new PlotVizVisitorFactory<CustomNodeVisitor>(dotVizSettings))
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
        System.out.println(plot.getPlotAsString(new PlotVizVisitorBuilder<CustomNodeVisitor>(dotVizSettings), 1));
        System.out.println("--------------------- SHORT INFO ---------------------");
        System.out.println(plot.getPlotAsString(
                new PlotVizVisitorBuilder<CustomNodeVisitor>(
                        dotVizSettings.copy().setNodeInfoType(DotVizSettings.NodeInfoType.SHORT).build()
                ), 1)
        );
    }

    private Node<TestUserContext, CustomNodeVisitor> createCompositeNode() {
        return new CompositeNode<>(new Node[]{
                new TestInnerNodeA<>(),
                new TestVizNode<>(TestVizNode.TYPE.B),
                new TestInnerNodeC<>(),
                new CompositeNode<>(
                        new Node[]{
                                new TestVizNode<>(TestVizNode.TYPE.D),
                                new TestVizNode<>(TestVizNode.TYPE.A),
                                new TestVizNode<>(TestVizNode.TYPE.C),
                                new CompositeNode<>(
                                        new Node[]{
                                                new TestInnerNodeA<>(),
                                                new TestVizNode<>(TestVizNode.TYPE.B),
                                        }),
                                new TestVizNode<>(TestVizNode.TYPE.D)
                        }
                ),
                new TestInnerNodeB<>()
        });
    }

    private Scene<TestUserContext, CustomNodeVisitor> createInnerScene() {
        final SceneBuilder<TestUserContext, CustomNodeVisitor> innerSceneBuilder = Scene.builder(
                SceneConfig.builder()
                        .setDefaultNodeType(NodeType.WAITING_INPUT)
                        .build()
        );
        ElementId one = innerSceneBuilder.registerNode(ElementId.of("1"), new TestVizNode<TestUserContext>(TestVizNode.TYPE.A));
        ElementId two = innerSceneBuilder.registerNode(ElementId.of("2"), new TestNode<TestUserContext>());
        ElementId three = innerSceneBuilder.registerNode(ElementId.of("3"), new TestVizNode<TestUserContext>(TestVizNode.TYPE.C));
        ElementId four = innerSceneBuilder.registerNode(ElementId.of("4"), new TestNode<TestUserContext>());

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
        ElementId one = innerSceneBuilder.registerNode(ElementId.of("i1"), new TestVizNode<TestUserContext>(TestVizNode.TYPE.A));
        ElementId two = innerSceneBuilder.registerNode(ElementId.of("i2"), new TestVizNode<TestUserContext>(TestVizNode.TYPE.B));
        ElementId three = innerSceneBuilder.registerNode(ElementId.of("i3"), new TestVizNode<TestUserContext>(TestVizNode.TYPE.C));
        ElementId four = innerSceneBuilder.registerNode(ElementId.of("i4"), new TestVizNode<TestUserContext>(TestVizNode.TYPE.D));
        ElementId five = innerSceneBuilder.registerSceneLink(innerSceneId);

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

    public static class CustomTypeDeterminant extends DefaultElementTypeDeterminant {

        public static final NodeElementType TYPE_A = NodeElementType.builder()
                .setElementId("TYPE_A")
                .setLabel("Type A")
                .setShortDataBuilder(new Supplier<NodeElementType.ShortViz.Builder>() {
                                         @Override
                                         public void accept(NodeElementType.ShortViz.Builder builder) {
                                             builder
                                                     .setShape(GraphvizShape.DIAMOND)
                                                     .setFillColor(GraphvizColor.VIOLET);
                                         }
                                     }
                )
                .setFullDataBuilder(new Supplier<NodeElementType.FullViz.Builder>() {
                                        @Override
                                        public void accept(NodeElementType.FullViz.Builder builder) {
                                            builder
                                                    .setHeaderColor(GraphvizColor.VIOLET);
                                            //.setColorSchema()
                                        }
                                    }
                )
                .build();

        public static final NodeElementType TYPE_B = NodeElementType.builder()
                .setElementId("TYPE_B")
                .setLabel("Type B")
                .setShortDataBuilder(new Supplier<NodeElementType.ShortViz.Builder>() {
                                         @Override
                                         public void accept(NodeElementType.ShortViz.Builder builder) {
                                             builder
                                                     .setShape(GraphvizShape.DIAMOND)
                                                     .setFillColor(GraphvizColor.AQUAMARINE);
                                         }
                                     }
                )
                .setFullDataBuilder(new Supplier<NodeElementType.FullViz.Builder>() {
                                        @Override
                                        public void accept(NodeElementType.FullViz.Builder builder) {
                                            builder
                                                    .setHeaderColor(GraphvizColor.AQUAMARINE);
                                            //.setColorSchema()
                                        }
                                    }
                )
                .build();

        public static final NodeElementType TYPE_C = NodeElementType.builder()
                .setElementId("TYPE_C")
                .setLabel("Type C")
                .setShortDataBuilder(new Supplier<NodeElementType.ShortViz.Builder>() {
                                         @Override
                                         public void accept(NodeElementType.ShortViz.Builder builder) {
                                             builder
                                                     .setShape(GraphvizShape.DIAMOND)
                                                     .setFillColor(GraphvizColor.LIGHT_GOLDEN_ROD_YELLOW);
                                         }
                                     }
                )
                .setFullDataBuilder(new Supplier<NodeElementType.FullViz.Builder>() {
                                        @Override
                                        public void accept(NodeElementType.FullViz.Builder builder) {
                                            builder
                                                    .setHeaderColor(GraphvizColor.LIGHT_GOLDEN_ROD_YELLOW);
                                            //.setColorSchema()
                                        }
                                    }
                )
                .build();

        public static final NodeElementType TYPE_D = NodeElementType.builder()
                .setElementId("TYPE_D")
                .setLabel("Type D")
                .setShortDataBuilder(new Supplier<NodeElementType.ShortViz.Builder>() {
                                         @Override
                                         public void accept(NodeElementType.ShortViz.Builder builder) {
                                             builder
                                                     .setShape(GraphvizShape.DIAMOND)
                                                     .setFillColor(GraphvizColor.MAROON);
                                         }
                                     }
                )
                .setFullDataBuilder(new Supplier<NodeElementType.FullViz.Builder>() {
                                        @Override
                                        public void accept(NodeElementType.FullViz.Builder builder) {
                                            builder
                                                    .setHeaderColor(GraphvizColor.MAROON)
                                            //.setColorSchema()
                                            ;
                                        }
                                    }
                )
                .build();

        @Override
        public NodeElementTypeId determineType(Node<?, ?> node) {
            if (node instanceof TestVizNode) {
                TestVizNode.TYPE type = ((TestVizNode<?>) node).getType();
                switch (type) {
                    case A:
                        return TYPE_A.getElementId();
                    case B:
                        return TYPE_B.getElementId();
                    case C:
                        return TYPE_C.getElementId();
                    case D:
                        return TYPE_D.getElementId();
                }
            }

            return super.determineType(node);
        }

    }

}
