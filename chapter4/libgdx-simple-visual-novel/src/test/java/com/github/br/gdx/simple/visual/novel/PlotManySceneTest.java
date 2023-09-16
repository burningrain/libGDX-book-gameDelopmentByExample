package com.github.br.gdx.simple.visual.novel;

import com.github.br.gdx.simple.visual.novel.api.ElementId;
import com.github.br.gdx.simple.visual.novel.api.Pair;
import com.github.br.gdx.simple.visual.novel.api.context.PlotContext;
import com.github.br.gdx.simple.visual.novel.api.edge.Predicate;
import com.github.br.gdx.simple.visual.novel.api.node.NodeType;
import com.github.br.gdx.simple.visual.novel.api.plot.DefaultSceneManager;
import com.github.br.gdx.simple.visual.novel.api.plot.Plot;
import com.github.br.gdx.simple.visual.novel.api.plot.PlotConfig;
import com.github.br.gdx.simple.visual.novel.api.plot.SceneSupplier;
import com.github.br.gdx.simple.visual.novel.api.scene.Scene;
import com.github.br.gdx.simple.visual.novel.api.scene.SceneBuilder;
import com.github.br.gdx.simple.visual.novel.api.scene.SceneConfig;
import com.github.br.gdx.simple.visual.novel.api.scene.SceneNodeBuilder;
import com.github.br.gdx.simple.visual.novel.impl.*;
import org.junit.Assert;
import org.junit.Test;

public class PlotManySceneTest {


    @Test(expected = Exception.class)
    public void testInnerScene() {
        TestUserContext userContext = new TestUserContext();
        final SceneBuilder<TestUserContext, CustomNodeVisitor> mainSceneBuilder = Scene.builder(
                SceneConfig.builder()
                        .setDefaultNodeType(NodeType.WAITING_INPUT)
                        .build()
        );

        // внутренняя сцена
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

        // внутренний сценарий
        ElementId innerSceneId = ElementId.of("inner_scene");
        final Scene<TestUserContext, CustomNodeVisitor> innerScene = innerSceneBuilder.graph()
                .node(one)
                .to()
                .node(two)
                .to()
                .node(three)
                .to()
                .node(four).end().build();
        System.out.println("inner scene: " + innerScene.toString());
        // внутренний сценарий

        // основной сценарий
        ElementId mainSceneId = ElementId.of("main_scene");
        ElementId a = mainSceneBuilder.registerNode(ElementId.of("a"), new TestNode<TestUserContext>());
        ElementId b = mainSceneBuilder.registerSceneLink(innerSceneId);
        ElementId c = mainSceneBuilder.registerNode(ElementId.of("c"), new TestNode<TestUserContext>());
        SceneNodeBuilder<TestUserContext, CustomNodeVisitor> mainGraphSceneBuilder = mainSceneBuilder.graph();
        mainGraphSceneBuilder
                .node(a)
                .to()
                .node(b)
                .to()
                .node(c).end();
        final Scene<TestUserContext, CustomNodeVisitor> scene = mainGraphSceneBuilder.build();
        System.out.println("main scene: " + scene.toString());

        Plot<Integer, TestUserContext, CustomNodeVisitor> plot =
                Plot.<Integer, TestUserContext, CustomNodeVisitor>builder(PlotConfig.<Integer>builder().build())
                        .setSceneManager(new DefaultSceneManager<TestUserContext, CustomNodeVisitor>()
                                .addScene(mainSceneId, new SceneSupplier<TestUserContext, CustomNodeVisitor>() {
                                            @Override
                                            public Scene<TestUserContext, CustomNodeVisitor> get() {
                                                return scene;
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
                        )
                        .setBeginSceneId(mainSceneId)
                        .build();

        int plotId = 1;
        plot.execute(plotId, userContext); // a
        plot.execute(plotId); // 1
        plot.execute(plotId); // 2
        plot.execute(plotId); // 3
        plot.execute(plotId); // 4
        Assert.assertTrue(plot.execute(plotId)); // c
        plot.execute(plotId); // исключение, так как процесс завершен
    }

    /**
     * digraph graphname {2;4;1;3;3 -> 1;1 -> 2;2 -> 4;2 -> 3;} - внутренний
     * digraph graphname {a;c;node_1;e;d;d -> node_1;c -> d;c -> e;node_1 -> c;a -> node_1;} - основной
     */
    @Test
    public void testJumpBackToInnerScene() {
        final TestUserContext userContext = new TestUserContext();
        final SceneBuilder<TestUserContext, CustomNodeVisitor> mainSceneBuilder = Scene.builder(
                SceneConfig.builder()
                        .setDefaultNodeType(NodeType.WAITING_INPUT)
                        .build()
        );

        // внутренняя сцена
        final SceneBuilder<TestUserContext, CustomNodeVisitor> innerSceneBuilder = Scene.builder(
                SceneConfig.builder()
                        .setDefaultNodeType(NodeType.WAITING_INPUT)
                        .build()
        );

        ElementId one = innerSceneBuilder.registerNode(ElementId.of("1"), new TestNode<TestUserContext>());
        ElementId two = innerSceneBuilder.registerNode(ElementId.of("2"), new TestNode<TestUserContext>());
        final ElementId three = innerSceneBuilder.registerNode(ElementId.of("3"), new TestNode<TestUserContext>());
        ElementId four = innerSceneBuilder.registerNode(ElementId.of("4"), new TestNode<TestUserContext>());
        innerSceneBuilder.registerNode(one, new TestNode<TestUserContext>());
        innerSceneBuilder.registerNode(two, new TestNode<TestUserContext>());
        innerSceneBuilder.registerNode(three, new TestNode<TestUserContext>());
        innerSceneBuilder.registerNode(four, new TestNode<TestUserContext>());

        // внутренний сценарий
        ElementId innerSceneId = ElementId.of("inner_scene");
        SceneNodeBuilder<TestUserContext, CustomNodeVisitor> innerBuilder = innerSceneBuilder.graph()
                .node(one)
                .to()
                .node(two)
                .to()
                .node(four).end();
        // петля назад
        innerBuilder
                .node(two)
                .to(three, new Predicate<TestUserContext>() {
                    @Override
                    public boolean test(PlotContext<?, TestUserContext> context) {
                        return userContext.nextId == three;
                    }
                })
                .node(three)
                .to(one)
                .endBranch()
        ;

        final Scene<TestUserContext, CustomNodeVisitor> innerScene = innerBuilder.build();

        System.out.println("inner scene: " + innerScene.toString());
        // внутренний сценарий

        // основной сценарий
        ElementId mainSceneId = ElementId.of("main_scene");
        ElementId a = mainSceneBuilder.registerNode(ElementId.of("a"), new TestNode<TestUserContext>());
        ElementId b = mainSceneBuilder.registerSceneLink(innerSceneId);
        ElementId c = mainSceneBuilder.registerNode(ElementId.of("c"), new TestNode<TestUserContext>());
        final ElementId d = mainSceneBuilder.registerNode(ElementId.of("d"), new TestNode<TestUserContext>());
        ElementId e = mainSceneBuilder.registerNode(ElementId.of("e"), new TestNode<TestUserContext>());
        SceneNodeBuilder<TestUserContext, CustomNodeVisitor> mainGraphSceneBuilder = mainSceneBuilder.graph();
        mainGraphSceneBuilder
                .node(a)
                .to()
                .node(b)
                .to()
                .node(c)
                .to()
                .node(e)
                .end();

        // заворот на внутреннюю сцену
        mainGraphSceneBuilder
                .node(c)
                .to(d, new Predicate<TestUserContext>() {
                    @Override
                    public boolean test(PlotContext<?, TestUserContext> context) {
                        return userContext.nextId == d;
                    }
                })
                .node(d)
                .to(b)
                .endBranch();

        final Scene<TestUserContext, CustomNodeVisitor> scene = mainGraphSceneBuilder.build();
        System.out.println("main scene: " + scene.toString());

        Plot<Integer, TestUserContext, CustomNodeVisitor> plot =
                Plot.<Integer, TestUserContext, CustomNodeVisitor>builder(
                                PlotConfig.<Integer>builder().setGeneratorPlotId(new GeneratorTestPlotIdImpl()).build()
                        )
                        .setSceneManager(new DefaultSceneManager<TestUserContext, CustomNodeVisitor>()
                                .addScene(mainSceneId, new SceneSupplier<TestUserContext, CustomNodeVisitor>() {
                                            @Override
                                            public Scene<TestUserContext, CustomNodeVisitor> get() {
                                                return scene;
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
                        )
                        .setBeginSceneId(mainSceneId)
                        .build();

        int plotId = 1;
        plot.execute(plotId, userContext); // a
        plot.execute(plotId); // 1
        userContext.nextId = three;
        plot.execute(plotId); // 2
        plot.execute(plotId); // 3
        plot.execute(plotId); // 1
        userContext.nextId = four;
        plot.execute(plotId); // 2
        plot.execute(plotId); // 4
        userContext.nextId = d;
        plot.execute(plotId); // c
        plot.execute(plotId); // d
        plot.execute(plotId); // 1
        userContext.nextId = four;
        plot.execute(plotId); // 2
        plot.execute(plotId); // 4
        userContext.nextId = e;
        plot.execute(plotId); // c
        Assert.assertTrue(plot.execute(plotId)); // e
    }

    /**
     * digraph graphname {2;1;3;2 -> 3;1 -> 3;1 -> 2;} - внутренний
     * digraph graphname {a;c;b;d;c -> d;b -> c;a -> b;a -> c;} - основной
     */
    @Test
    public void testJumpForwardToInnerScene() {
        TestUserContext userContext = new TestUserContext();
        final SceneBuilder<TestUserContext, CustomNodeVisitor> mainSceneBuilder = Scene.builder(
                SceneConfig.builder()
                        .setDefaultNodeType(NodeType.WAITING_INPUT)
                        .build()
        );

        // внутренняя сцена
        final SceneBuilder<TestUserContext, CustomNodeVisitor> innerSceneBuilder = Scene.builder(
                SceneConfig.builder()
                        .setDefaultNodeType(NodeType.WAITING_INPUT)
                        .build()
        );

        ElementId one = innerSceneBuilder.registerNode(ElementId.of("1"), new TestNode<TestUserContext>());
        ElementId two = innerSceneBuilder.registerNode(ElementId.of("2"), new TestNode<TestUserContext>());
        ElementId three = innerSceneBuilder.registerNode(ElementId.of("3"), new TestNode<TestUserContext>());
        innerSceneBuilder.registerNode(one, new TestNode<TestUserContext>());
        innerSceneBuilder.registerNode(two, new TestNode<TestUserContext>());
        innerSceneBuilder.registerNode(three, new TestNode<TestUserContext>());

        // внутренний сценарий
        ElementId innerSceneId = ElementId.of("inner_scene");
        SceneNodeBuilder<TestUserContext, CustomNodeVisitor> innerBuilder = innerSceneBuilder.graph()
                .node(one)
                .to()
                .node(two)
                .to()
                .node(three).end();
        // петля вперед
        innerBuilder
                .node(one)
                .to(three)
                .endBranch()
        ;

        final Scene<TestUserContext, CustomNodeVisitor> innerScene = innerBuilder.build();

        System.out.println("inner scene: " + innerScene.toString());
        // внутренний сценарий

        // основной сценарий
        ElementId mainSceneId = ElementId.of("main_scene");
        ElementId a = mainSceneBuilder.registerNode(ElementId.of("a"), new TestNode<TestUserContext>());
        ElementId b = mainSceneBuilder.registerNode(ElementId.of("b"), new TestNode<TestUserContext>());
        ElementId c = ElementId.of("c");
        mainSceneBuilder.registerSceneLink(c, innerSceneId);
        ElementId d = mainSceneBuilder.registerNode(ElementId.of("d"), new TestNode<TestUserContext>());
        SceneNodeBuilder<TestUserContext, CustomNodeVisitor> mainGraphSceneBuilder = mainSceneBuilder.graph();
        mainGraphSceneBuilder
                .node(a)
                .to()
                .node(b)
                .to()
                .node(c)
                .to()
                .node(d)
                .end();

        // заворот вперед в основной сцене
        mainGraphSceneBuilder
                .node(a)
                .to(c)
                .endBranch();

        final Scene<TestUserContext, CustomNodeVisitor> scene = mainGraphSceneBuilder.build();
        System.out.println("main scene: " + scene.toString());

        Plot<Integer, TestUserContext, CustomNodeVisitor> plot =
                Plot.<Integer, TestUserContext, CustomNodeVisitor>builder(
                                PlotConfig.<Integer>builder().setGeneratorPlotId(new GeneratorTestPlotIdImpl()).build()
                        )
                        .setSceneManager(new DefaultSceneManager<TestUserContext, CustomNodeVisitor>()
                                .addScene(mainSceneId, new SceneSupplier<TestUserContext, CustomNodeVisitor>() {
                                            @Override
                                            public Scene<TestUserContext, CustomNodeVisitor> get() {
                                                return scene;
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
                        )
                        .setBeginSceneId(mainSceneId)
                        .build();

        userContext.nextId = c;

        int plotId = 1;
        plot.execute(plotId, userContext); // a

        userContext.nextId = three;
        plot.execute(plotId); // 1
        plot.execute(plotId); // 3
        Assert.assertTrue(plot.execute(plotId)); // d
    }

    /**
     * digraph graphname {1;} - внутренний
     * digraph graphname {a;b;a -> b;} - основной
     */
    @Test
    public void testInnerSceneAsEndNode() {
        TestUserContext userContext = new TestUserContext();
        final SceneBuilder<TestUserContext, CustomNodeVisitor> mainSceneBuilder = Scene.builder(
                SceneConfig.builder()
                        .setDefaultNodeType(NodeType.WAITING_INPUT)
                        .build()
        );

        // внутренняя сцена
        final SceneBuilder<TestUserContext, CustomNodeVisitor> innerSceneBuilder = Scene.builder(
                SceneConfig.builder()
                        .setDefaultNodeType(NodeType.WAITING_INPUT)
                        .build()
        );

        ElementId one = innerSceneBuilder.registerNode(ElementId.of("1"), new TestNode<TestUserContext>());
        innerSceneBuilder.registerNode(one, new TestNode<TestUserContext>());

        // внутренний сценарий
        ElementId innerSceneId = ElementId.of("inner_scene");
        SceneNodeBuilder<TestUserContext, CustomNodeVisitor> innerBuilder = innerSceneBuilder.graph()
                .node(one)
                .end();

        final Scene<TestUserContext, CustomNodeVisitor> innerScene = innerBuilder.build();
        System.out.println("inner scene: " + innerScene.toString());
        // внутренний сценарий

        // основной сценарий
        ElementId mainSceneId = ElementId.of("main_scene");
        ElementId a = mainSceneBuilder.registerNode(ElementId.of("a"), new TestNode<TestUserContext>());
        ElementId b = mainSceneBuilder.registerSceneLink(ElementId.of("b"), innerSceneId);
        SceneNodeBuilder<TestUserContext, CustomNodeVisitor> mainGraphSceneBuilder = mainSceneBuilder.graph();
        mainGraphSceneBuilder
                .node(a)
                .to()
                .node(b)
                .end();

        final Scene<TestUserContext, CustomNodeVisitor> scene = mainGraphSceneBuilder.build();
        System.out.println("main scene: " + scene.toString());

        Plot<Integer, TestUserContext, CustomNodeVisitor> plot =
                Plot.<Integer, TestUserContext, CustomNodeVisitor>builder(
                                PlotConfig.<Integer>builder().setGeneratorPlotId(new GeneratorTestPlotIdImpl()).build()
                        )
                        .setSceneManager(new DefaultSceneManager<TestUserContext, CustomNodeVisitor>()
                                .addScene(mainSceneId, new SceneSupplier<TestUserContext, CustomNodeVisitor>() {
                                            @Override
                                            public Scene<TestUserContext, CustomNodeVisitor> get() {
                                                return scene;
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
                        )
                        .setBeginSceneId(mainSceneId)
                        .build();

        int plotId = 1;
        plot.execute(plotId, userContext); // a
        Assert.assertTrue(plot.execute(plotId)); // 1
    }

    /**
     * digraph graphname {2;1;3;1 -> 2;1 -> 3;} - внутренний
     * digraph graphname {a;c;b;a -> c;a -> b;} - основной
     */
    @Test
    public void testInnerSceneAsStartNode() {
        TestUserMapContext<ElementId, Boolean> userContext = new TestUserMapContext<>();
        final SceneBuilder<TestUserMapContext<ElementId, Boolean>, CustomNodeVisitor> mainSceneBuilder = Scene.builder(
                SceneConfig.builder()
                        .setDefaultNodeType(NodeType.WAITING_INPUT)
                        .build()
        );

        // внутренняя сцена
        final SceneBuilder<TestUserMapContext<ElementId, Boolean>, CustomNodeVisitor> innerSceneBuilder = Scene.builder(
                SceneConfig.builder()
                        .setDefaultNodeType(NodeType.WAITING_INPUT)
                        .build()
        );

        ElementId one = innerSceneBuilder.registerNode(ElementId.of("1"), new TestNode<TestUserMapContext<ElementId, Boolean>>());
        final ElementId two = innerSceneBuilder.registerNode(ElementId.of("2"), new TestNode<TestUserMapContext<ElementId, Boolean>>());
        final ElementId three = innerSceneBuilder.registerNode(ElementId.of("3"), new TestNode<TestUserMapContext<ElementId, Boolean>>());

        // внутренний сценарий
        ElementId innerSceneId = ElementId.of("inner_scene");
        SceneNodeBuilder<TestUserMapContext<ElementId, Boolean>, CustomNodeVisitor> innerBuilder = innerSceneBuilder.graph()
                .node(one)
                .to(new Pair<ElementId, Predicate<TestUserMapContext<ElementId, Boolean>>>(two, new Predicate<TestUserMapContext<ElementId, Boolean>>() {
                            @Override
                            public boolean test(PlotContext<?, TestUserMapContext<ElementId, Boolean>> context) {
                                return context.getUserContext().getNextId() == two;
                            }
                        }),
                        new Pair<ElementId, Predicate<TestUserMapContext<ElementId, Boolean>>>(three, new Predicate<TestUserMapContext<ElementId, Boolean>>() {
                            @Override
                            public boolean test(PlotContext<?, TestUserMapContext<ElementId, Boolean>> context) {
                                return context.getUserContext().getNextId() == three;
                            }
                        })
                )
                .endBranch();

        final Scene<TestUserMapContext<ElementId, Boolean>, CustomNodeVisitor> innerScene = innerBuilder.build();
        System.out.println("inner scene: " + innerScene.toString());
        // внутренний сценарий

        // основной сценарий
        ElementId mainSceneId = ElementId.of("main_scene");
        ElementId a = mainSceneBuilder.registerSceneLink(ElementId.of("a"), innerSceneId);
        final ElementId b = mainSceneBuilder.registerNode(ElementId.of("b"), new TestNode<TestUserMapContext<ElementId, Boolean>>());
        final ElementId c = mainSceneBuilder.registerNode(ElementId.of("c"), new TestNode<TestUserMapContext<ElementId, Boolean>>());
        SceneNodeBuilder<TestUserMapContext<ElementId, Boolean>, CustomNodeVisitor> mainGraphSceneBuilder = mainSceneBuilder.graph();
        mainGraphSceneBuilder
                .node(a)
                .to(
                        new Pair<ElementId, Predicate<TestUserMapContext<ElementId, Boolean>>>(b, new Predicate<TestUserMapContext<ElementId, Boolean>>() {
                            @Override
                            public boolean test(PlotContext<?, TestUserMapContext<ElementId, Boolean>> context) {
                                return context.getUserContext().getNextId() == b;
                            }
                        }),
                        new Pair<ElementId, Predicate<TestUserMapContext<ElementId, Boolean>>>(c, new Predicate<TestUserMapContext<ElementId, Boolean>>() {
                            @Override
                            public boolean test(PlotContext<?, TestUserMapContext<ElementId, Boolean>> context) {
                                return context.getUserContext().getNextId() == c;
                            }
                        })
                )
                .endBranch();

        final Scene<TestUserMapContext<ElementId, Boolean>, CustomNodeVisitor> scene = mainGraphSceneBuilder.build();
        System.out.println("main scene: " + scene.toString());

        Plot<Integer, TestUserMapContext<ElementId, Boolean>, CustomNodeVisitor> plot =
                Plot.<Integer, TestUserMapContext<ElementId, Boolean>, CustomNodeVisitor>builder(
                                PlotConfig.<Integer>builder().setGeneratorPlotId(new GeneratorTestPlotIdImpl()).build()
                        )
                        .setSceneManager(new DefaultSceneManager<TestUserMapContext<ElementId, Boolean>, CustomNodeVisitor>()
                                .addScene(mainSceneId, new SceneSupplier<TestUserMapContext<ElementId, Boolean>, CustomNodeVisitor>() {
                                            @Override
                                            public Scene<TestUserMapContext<ElementId, Boolean>, CustomNodeVisitor> get() {
                                                return scene;
                                            }
                                        }
                                )
                                .addScene(innerSceneId, new SceneSupplier<TestUserMapContext<ElementId, Boolean>, CustomNodeVisitor>() {
                                            @Override
                                            public Scene<TestUserMapContext<ElementId, Boolean>, CustomNodeVisitor> get() {
                                                return innerScene;
                                            }
                                        }
                                )
                        )
                        .setBeginSceneId(mainSceneId)
                        .build();

        userContext.setNextId(three);

        int plotId = 1;
        plot.execute(plotId, userContext); // 1
        userContext.setNextId(c);
        plot.execute(plotId); // 3
        Assert.assertTrue(plot.execute(plotId)); // c
    }


}
