package com.github.br.gdx.simple.visual.novel;

import com.github.br.gdx.simple.visual.novel.api.ElementId;
import com.github.br.gdx.simple.visual.novel.api.Pair;
import com.github.br.gdx.simple.visual.novel.api.context.PlotContext;
import com.github.br.gdx.simple.visual.novel.api.edge.Predicate;
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


    @Test
    public void testInnerScene() {
        TestUserContext userContext = new TestUserContext();
        TestScreenManager testScreenManager = new TestScreenManager();
        final SceneBuilder<TestUserContext, TestScreenManager> mainSceneBuilder = Scene.builder(
                SceneConfig.<TestScreenManager>builder()
                        .setScreenManager(testScreenManager)
                        .build()
        );

        // внутренняя сцена
        final SceneBuilder<TestUserContext, TestScreenManager> innerSceneBuilder = Scene.builder(
                SceneConfig.<TestScreenManager>builder()
                        .setScreenManager(testScreenManager)
                        .build()
        );

        ElementId one = innerSceneBuilder.registerNode(ElementId.of("1"), new TestNode<TestUserContext, TestScreenManager>());
        ElementId two = innerSceneBuilder.registerNode(ElementId.of("2"), new TestNode<TestUserContext, TestScreenManager>());
        ElementId three = innerSceneBuilder.registerNode(ElementId.of("3"), new TestNode<TestUserContext, TestScreenManager>());
        ElementId four = innerSceneBuilder.registerNode(ElementId.of("4"), new TestNode<TestUserContext, TestScreenManager>());
        innerSceneBuilder.registerNode(one, new TestNode<TestUserContext, TestScreenManager>());
        innerSceneBuilder.registerNode(two, new TestNode<TestUserContext, TestScreenManager>());
        innerSceneBuilder.registerNode(three, new TestNode<TestUserContext, TestScreenManager>());
        innerSceneBuilder.registerNode(four, new TestNode<TestUserContext, TestScreenManager>());

        // внутренний сценарий
        ElementId innerSceneId = ElementId.of("inner_scene");
        final Scene<TestUserContext, TestScreenManager> innerScene = innerSceneBuilder.graph()
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
        ElementId a = mainSceneBuilder.registerNode(ElementId.of("a"), new TestNode<TestUserContext, TestScreenManager>());
        ElementId b = mainSceneBuilder.registerSceneLink(innerSceneId);
        ElementId c = mainSceneBuilder.registerNode(ElementId.of("c"), new TestNode<TestUserContext, TestScreenManager>());
        SceneNodeBuilder<TestUserContext, TestScreenManager> mainGraphSceneBuilder = mainSceneBuilder.graph();
        mainGraphSceneBuilder
                .node(a)
                .to()
                .node(b)
                .to()
                .node(c).end();
        final Scene<TestUserContext, TestScreenManager> scene = mainGraphSceneBuilder.build();
        System.out.println("main scene: " + scene.toString());

        Plot<TestUserContext, TestScreenManager> plot = Plot.<TestUserContext, TestScreenManager>builder(PlotConfig.builder().build())
                .setUserContext(userContext)
                .setSceneManager(new DefaultSceneManager<TestUserContext, TestScreenManager>()
                        .addScene(mainSceneId, new SceneSupplier<TestUserContext, TestScreenManager>() {
                                    @Override
                                    public Scene<TestUserContext, TestScreenManager> get() {
                                        return scene;
                                    }
                                }
                        )
                        .addScene(innerSceneId, new SceneSupplier<TestUserContext, TestScreenManager>() {
                                    @Override
                                    public Scene<TestUserContext, TestScreenManager> get() {
                                        return innerScene;
                                    }
                                }
                        )
                )
                .setBeginSceneId(mainSceneId)
                .build();

        plot.execute(1f); // a
        plot.execute(1f); // 1
        plot.execute(1f); // 2
        plot.execute(1f); // 3
        plot.execute(1f); // 4
        plot.execute(1f); // c
        Assert.assertTrue(plot.execute(1f));
        Assert.assertTrue(plot.execute(1f)); // проверка, что остается в оконченном состоянии
    }

    /**
     * digraph graphname {2;4;1;3;3 -> 1;1 -> 2;2 -> 4;2 -> 3;} - внутренний
     * digraph graphname {a;c;node_1;e;d;d -> node_1;c -> d;c -> e;node_1 -> c;a -> node_1;} - основной
     */
    @Test
    public void testJumpBackToInnerScene() {
        final TestUserContext userContext = new TestUserContext();
        TestScreenManager testScreenManager = new TestScreenManager();
        final SceneBuilder<TestUserContext, TestScreenManager> mainSceneBuilder = Scene.builder(
                SceneConfig.<TestScreenManager>builder()
                        .setScreenManager(testScreenManager)
                        .build()
        );

        // внутренняя сцена
        final SceneBuilder<TestUserContext, TestScreenManager> innerSceneBuilder = Scene.builder(
                SceneConfig.<TestScreenManager>builder()
                        .setScreenManager(testScreenManager)
                        .build()
        );

        ElementId one = innerSceneBuilder.registerNode(ElementId.of("1"), new TestNode<TestUserContext, TestScreenManager>());
        ElementId two = innerSceneBuilder.registerNode(ElementId.of("2"), new TestNode<TestUserContext, TestScreenManager>());
        final ElementId three = innerSceneBuilder.registerNode(ElementId.of("3"), new TestNode<TestUserContext, TestScreenManager>());
        ElementId four = innerSceneBuilder.registerNode(ElementId.of("4"), new TestNode<TestUserContext, TestScreenManager>());
        innerSceneBuilder.registerNode(one, new TestNode<TestUserContext, TestScreenManager>());
        innerSceneBuilder.registerNode(two, new TestNode<TestUserContext, TestScreenManager>());
        innerSceneBuilder.registerNode(three, new TestNode<TestUserContext, TestScreenManager>());
        innerSceneBuilder.registerNode(four, new TestNode<TestUserContext, TestScreenManager>());

        // внутренний сценарий
        ElementId innerSceneId = ElementId.of("inner_scene");
        SceneNodeBuilder<TestUserContext, TestScreenManager> innerBuilder = innerSceneBuilder.graph()
                .node(one)
                .to()
                .node(two)
                .to()
                .node(four).end();
        // петля назад
        innerBuilder
                .node(two)
                .to(three, new Predicate<TestUserContext, TestScreenManager>() {
                    @Override
                    public boolean test(PlotContext<TestUserContext, TestScreenManager> context) {
                        return userContext.nextId == three;
                    }
                })
                .node(three)
                .to(one)
                .endBranch()
        ;

        final Scene<TestUserContext, TestScreenManager> innerScene = innerBuilder.build();

        System.out.println("inner scene: " + innerScene.toString());
        // внутренний сценарий

        // основной сценарий
        ElementId mainSceneId = ElementId.of("main_scene");
        ElementId a = mainSceneBuilder.registerNode(ElementId.of("a"), new TestNode<TestUserContext, TestScreenManager>());
        ElementId b = mainSceneBuilder.registerSceneLink(innerSceneId);
        ElementId c = mainSceneBuilder.registerNode(ElementId.of("c"), new TestNode<TestUserContext, TestScreenManager>());
        final ElementId d = mainSceneBuilder.registerNode(ElementId.of("d"), new TestNode<TestUserContext, TestScreenManager>());
        ElementId e = mainSceneBuilder.registerNode(ElementId.of("e"), new TestNode<TestUserContext, TestScreenManager>());
        SceneNodeBuilder<TestUserContext, TestScreenManager> mainGraphSceneBuilder = mainSceneBuilder.graph();
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
                .to(d, new Predicate<TestUserContext, TestScreenManager>() {
                    @Override
                    public boolean test(PlotContext<TestUserContext, TestScreenManager> context) {
                        return userContext.nextId == d;
                    }
                })
                .node(d)
                .to(b)
                .endBranch();

        final Scene<TestUserContext, TestScreenManager> scene = mainGraphSceneBuilder.build();
        System.out.println("main scene: " + scene.toString());

        Plot<TestUserContext, TestScreenManager> plot = Plot.<TestUserContext, TestScreenManager>builder(PlotConfig.builder().build())
                .setUserContext(userContext)
                .setSceneManager(new DefaultSceneManager<TestUserContext, TestScreenManager>()
                        .addScene(mainSceneId, new SceneSupplier<TestUserContext, TestScreenManager>() {
                                    @Override
                                    public Scene<TestUserContext, TestScreenManager> get() {
                                        return scene;
                                    }
                                }
                        )
                        .addScene(innerSceneId, new SceneSupplier<TestUserContext, TestScreenManager>() {
                                    @Override
                                    public Scene<TestUserContext, TestScreenManager> get() {
                                        return innerScene;
                                    }
                                }
                        )
                )
                .setBeginSceneId(mainSceneId)
                .build();

        plot.execute(1f); // a
        plot.execute(1f); // 1
        userContext.nextId = three;
        plot.execute(1f); // 2
        plot.execute(1f); // 3
        plot.execute(1f); // 1
        userContext.nextId = four;
        plot.execute(1f); // 2
        plot.execute(1f); // 4
        userContext.nextId = d;
        plot.execute(1f); // c
        plot.execute(1f); // d
        plot.execute(1f); // 1
        userContext.nextId = four;
        plot.execute(1f); // 2
        plot.execute(1f); // 4
        userContext.nextId = e;
        plot.execute(1f); // c
        plot.execute(1f); // e

        Assert.assertTrue(plot.execute(1f));
    }

    /**
     * digraph graphname {2;1;3;2 -> 3;1 -> 3;1 -> 2;} - внутренний
     * digraph graphname {a;c;b;d;c -> d;b -> c;a -> b;a -> c;} - основной
     */
    @Test
    public void testJumpForwardToInnerScene() {
        TestUserContext userContext = new TestUserContext();
        TestScreenManager testScreenManager = new TestScreenManager();
        final SceneBuilder<TestUserContext, TestScreenManager> mainSceneBuilder = Scene.builder(
                SceneConfig.<TestScreenManager>builder()
                        .setScreenManager(testScreenManager)
                        .build()
        );

        // внутренняя сцена
        final SceneBuilder<TestUserContext, TestScreenManager> innerSceneBuilder = Scene.builder(
                SceneConfig.<TestScreenManager>builder()
                        .setScreenManager(testScreenManager)
                        .build()
        );

        ElementId one = innerSceneBuilder.registerNode(ElementId.of("1"), new TestNode<TestUserContext, TestScreenManager>());
        ElementId two = innerSceneBuilder.registerNode(ElementId.of("2"), new TestNode<TestUserContext, TestScreenManager>());
        ElementId three = innerSceneBuilder.registerNode(ElementId.of("3"), new TestNode<TestUserContext, TestScreenManager>());
        innerSceneBuilder.registerNode(one, new TestNode<TestUserContext, TestScreenManager>());
        innerSceneBuilder.registerNode(two, new TestNode<TestUserContext, TestScreenManager>());
        innerSceneBuilder.registerNode(three, new TestNode<TestUserContext, TestScreenManager>());

        // внутренний сценарий
        ElementId innerSceneId = ElementId.of("inner_scene");
        SceneNodeBuilder<TestUserContext, TestScreenManager> innerBuilder = innerSceneBuilder.graph()
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

        final Scene<TestUserContext, TestScreenManager> innerScene = innerBuilder.build();

        System.out.println("inner scene: " + innerScene.toString());
        // внутренний сценарий

        // основной сценарий
        ElementId mainSceneId = ElementId.of("main_scene");
        ElementId a = mainSceneBuilder.registerNode(ElementId.of("a"), new TestNode<TestUserContext, TestScreenManager>());
        ElementId b = mainSceneBuilder.registerNode(ElementId.of("b"), new TestNode<TestUserContext, TestScreenManager>());
        ElementId c = ElementId.of("c");
        mainSceneBuilder.registerSceneLink(c, innerSceneId);
        ElementId d = mainSceneBuilder.registerNode(ElementId.of("d"), new TestNode<TestUserContext, TestScreenManager>());
        SceneNodeBuilder<TestUserContext, TestScreenManager> mainGraphSceneBuilder = mainSceneBuilder.graph();
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

        final Scene<TestUserContext, TestScreenManager> scene = mainGraphSceneBuilder.build();
        System.out.println("main scene: " + scene.toString());

        Plot<TestUserContext, TestScreenManager> plot = Plot.<TestUserContext, TestScreenManager>builder(PlotConfig.builder().build())
                .setUserContext(userContext)
                .setSceneManager(new DefaultSceneManager<TestUserContext, TestScreenManager>()
                        .addScene(mainSceneId, new SceneSupplier<TestUserContext, TestScreenManager>() {
                                    @Override
                                    public Scene<TestUserContext, TestScreenManager> get() {
                                        return scene;
                                    }
                                }
                        )
                        .addScene(innerSceneId, new SceneSupplier<TestUserContext, TestScreenManager>() {
                                    @Override
                                    public Scene<TestUserContext, TestScreenManager> get() {
                                        return innerScene;
                                    }
                                }
                        )
                )
                .setBeginSceneId(mainSceneId)
                .build();

        userContext.nextId = c;
        plot.execute(1f); // a

        userContext.nextId = three;
        plot.execute(1f); // 1
        plot.execute(1f); // 3
        plot.execute(1f); // d

        Assert.assertTrue(plot.execute(1f));
    }

    /**
     * digraph graphname {1;} - внутренний
     * digraph graphname {a;b;a -> b;} - основной
     */
    @Test
    public void testInnerSceneAsEndNode() {
        TestUserContext userContext = new TestUserContext();
        TestScreenManager testScreenManager = new TestScreenManager();
        final SceneBuilder<TestUserContext, TestScreenManager> mainSceneBuilder = Scene.builder(
                SceneConfig.<TestScreenManager>builder()
                        .setScreenManager(testScreenManager)
                        .build()
        );

        // внутренняя сцена
        final SceneBuilder<TestUserContext, TestScreenManager> innerSceneBuilder = Scene.builder(
                SceneConfig.<TestScreenManager>builder()
                        .setScreenManager(testScreenManager)
                        .build()
        );

        ElementId one = innerSceneBuilder.registerNode(ElementId.of("1"), new TestNode<TestUserContext, TestScreenManager>());
        innerSceneBuilder.registerNode(one, new TestNode<TestUserContext, TestScreenManager>());

        // внутренний сценарий
        ElementId innerSceneId = ElementId.of("inner_scene");
        SceneNodeBuilder<TestUserContext, TestScreenManager> innerBuilder = innerSceneBuilder.graph()
                .node(one)
                .end();

        final Scene<TestUserContext, TestScreenManager> innerScene = innerBuilder.build();
        System.out.println("inner scene: " + innerScene.toString());
        // внутренний сценарий

        // основной сценарий
        ElementId mainSceneId = ElementId.of("main_scene");
        ElementId a = mainSceneBuilder.registerNode(ElementId.of("a"), new TestNode<TestUserContext, TestScreenManager>());
        ElementId b = mainSceneBuilder.registerSceneLink(ElementId.of("b"), innerSceneId);
        SceneNodeBuilder<TestUserContext, TestScreenManager> mainGraphSceneBuilder = mainSceneBuilder.graph();
        mainGraphSceneBuilder
                .node(a)
                .to()
                .node(b)
                .end();

        final Scene<TestUserContext, TestScreenManager> scene = mainGraphSceneBuilder.build();
        System.out.println("main scene: " + scene.toString());

        Plot<TestUserContext, TestScreenManager> plot = Plot.<TestUserContext, TestScreenManager>builder(PlotConfig.builder().build())
                .setUserContext(userContext)
                .setSceneManager(new DefaultSceneManager<TestUserContext, TestScreenManager>()
                        .addScene(mainSceneId, new SceneSupplier<TestUserContext, TestScreenManager>() {
                                    @Override
                                    public Scene<TestUserContext, TestScreenManager> get() {
                                        return scene;
                                    }
                                }
                        )
                        .addScene(innerSceneId, new SceneSupplier<TestUserContext, TestScreenManager>() {
                                    @Override
                                    public Scene<TestUserContext, TestScreenManager> get() {
                                        return innerScene;
                                    }
                                }
                        )
                )
                .setBeginSceneId(mainSceneId)
                .build();

        plot.execute(1f); // a
        plot.execute(1f); // 1

        Assert.assertTrue(plot.execute(1f));
    }

    /**
     * digraph graphname {2;1;3;1 -> 2;1 -> 3;} - внутренний
     * digraph graphname {a;c;b;a -> c;a -> b;} - основной
     */
    @Test
    public void testInnerSceneAsStartNode() {
        TestUserMapContext<ElementId, Boolean> userContext = new TestUserMapContext<>();
        TestScreenManager testScreenManager = new TestScreenManager();
        final SceneBuilder<TestUserMapContext<ElementId, Boolean>, TestScreenManager> mainSceneBuilder = Scene.builder(
                SceneConfig.<TestScreenManager>builder()
                        .setScreenManager(testScreenManager)
                        .build()
        );

        // внутренняя сцена
        final SceneBuilder<TestUserMapContext<ElementId, Boolean>, TestScreenManager> innerSceneBuilder = Scene.builder(
                SceneConfig.<TestScreenManager>builder()
                        .setScreenManager(testScreenManager)
                        .build()
        );

        ElementId one = innerSceneBuilder.registerNode(ElementId.of("1"), new TestNode<TestUserMapContext<ElementId, Boolean>, TestScreenManager>());
        final ElementId two = innerSceneBuilder.registerNode(ElementId.of("2"), new TestNode<TestUserMapContext<ElementId, Boolean>, TestScreenManager>());
        final ElementId three = innerSceneBuilder.registerNode(ElementId.of("3"), new TestNode<TestUserMapContext<ElementId, Boolean>, TestScreenManager>());

        // внутренний сценарий
        ElementId innerSceneId = ElementId.of("inner_scene");
        SceneNodeBuilder<TestUserMapContext<ElementId, Boolean>, TestScreenManager> innerBuilder = innerSceneBuilder.graph()
                .node(one)
                .to(new Pair<ElementId, Predicate<TestUserMapContext<ElementId, Boolean>, TestScreenManager>>(two, new Predicate<TestUserMapContext<ElementId, Boolean>, TestScreenManager>() {
                            @Override
                            public boolean test(PlotContext<TestUserMapContext<ElementId, Boolean>, TestScreenManager> context) {
                                return context.getUserContext().getNextId() == two;
                            }
                        }),
                        new Pair<ElementId, Predicate<TestUserMapContext<ElementId, Boolean>, TestScreenManager>>(three, new Predicate<TestUserMapContext<ElementId, Boolean>, TestScreenManager>() {
                            @Override
                            public boolean test(PlotContext<TestUserMapContext<ElementId, Boolean>, TestScreenManager> context) {
                                return context.getUserContext().getNextId() == three;
                            }
                        })
                )
                .endBranch();

        final Scene<TestUserMapContext<ElementId, Boolean>, TestScreenManager> innerScene = innerBuilder.build();
        System.out.println("inner scene: " + innerScene.toString());
        // внутренний сценарий

        // основной сценарий
        ElementId mainSceneId = ElementId.of("main_scene");
        ElementId a = mainSceneBuilder.registerSceneLink(ElementId.of("a"), innerSceneId);
        final ElementId b = mainSceneBuilder.registerNode(ElementId.of("b"), new TestNode<TestUserMapContext<ElementId, Boolean>, TestScreenManager>());
        final ElementId c = mainSceneBuilder.registerNode(ElementId.of("c"), new TestNode<TestUserMapContext<ElementId, Boolean>, TestScreenManager>());
        SceneNodeBuilder<TestUserMapContext<ElementId, Boolean>, TestScreenManager> mainGraphSceneBuilder = mainSceneBuilder.graph();
        mainGraphSceneBuilder
                .node(a)
                .to(
                        new Pair<ElementId, Predicate<TestUserMapContext<ElementId, Boolean>, TestScreenManager>>(b, new Predicate<TestUserMapContext<ElementId, Boolean>, TestScreenManager>() {
                            @Override
                            public boolean test(PlotContext<TestUserMapContext<ElementId, Boolean>, TestScreenManager> context) {
                                return context.getUserContext().getNextId() == b;
                            }
                        }),
                        new Pair<ElementId, Predicate<TestUserMapContext<ElementId, Boolean>, TestScreenManager>>(c, new Predicate<TestUserMapContext<ElementId, Boolean>, TestScreenManager>() {
                            @Override
                            public boolean test(PlotContext<TestUserMapContext<ElementId, Boolean>, TestScreenManager> context) {
                                return context.getUserContext().getNextId() == c;
                            }
                        })
                )
                .endBranch();

        final Scene<TestUserMapContext<ElementId, Boolean>, TestScreenManager> scene = mainGraphSceneBuilder.build();
        System.out.println("main scene: " + scene.toString());

        Plot<TestUserMapContext<ElementId, Boolean>, TestScreenManager> plot = Plot.<TestUserMapContext<ElementId, Boolean>, TestScreenManager>builder(PlotConfig.builder().build())
                .setUserContext(userContext)
                .setSceneManager(new DefaultSceneManager<TestUserMapContext<ElementId, Boolean>, TestScreenManager>()
                        .addScene(mainSceneId, new SceneSupplier<TestUserMapContext<ElementId, Boolean>, TestScreenManager>() {
                                    @Override
                                    public Scene<TestUserMapContext<ElementId, Boolean>, TestScreenManager> get() {
                                        return scene;
                                    }
                                }
                        )
                        .addScene(innerSceneId, new SceneSupplier<TestUserMapContext<ElementId, Boolean>, TestScreenManager>() {
                                    @Override
                                    public Scene<TestUserMapContext<ElementId, Boolean>, TestScreenManager> get() {
                                        return innerScene;
                                    }
                                }
                        )
                )
                .setBeginSceneId(mainSceneId)
                .build();

        userContext.setNextId(three);
        plot.execute(1f); // 1
        userContext.setNextId(c);
        plot.execute(1f); // 3
        Assert.assertTrue(plot.execute(1f)); // c
    }


}
