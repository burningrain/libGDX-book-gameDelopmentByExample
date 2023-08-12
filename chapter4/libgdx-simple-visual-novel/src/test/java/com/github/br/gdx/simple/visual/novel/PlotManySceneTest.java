package com.github.br.gdx.simple.visual.novel;

import com.github.br.gdx.simple.visual.novel.api.ElementId;
import com.github.br.gdx.simple.visual.novel.api.plot.DefaultSceneManager;
import com.github.br.gdx.simple.visual.novel.api.plot.Plot;
import com.github.br.gdx.simple.visual.novel.api.plot.PlotConfig;
import com.github.br.gdx.simple.visual.novel.api.plot.SceneSupplier;
import com.github.br.gdx.simple.visual.novel.api.scene.Scene;
import com.github.br.gdx.simple.visual.novel.api.scene.SceneBuilder;
import com.github.br.gdx.simple.visual.novel.api.scene.SceneConfig;
import com.github.br.gdx.simple.visual.novel.api.scene.SceneNodeBuilder;
import com.github.br.gdx.simple.visual.novel.impl.TestChooseNode;
import com.github.br.gdx.simple.visual.novel.impl.TestNode;
import com.github.br.gdx.simple.visual.novel.impl.TestScreenManager;
import com.github.br.gdx.simple.visual.novel.impl.TestUserContext;
import org.junit.Assert;
import org.junit.Ignore;
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

        ElementId one = innerSceneBuilder.registerNode(ElementId.of("1"), new TestNode());
        ElementId two = innerSceneBuilder.registerNode(ElementId.of("2"), new TestNode());
        ElementId three = innerSceneBuilder.registerNode(ElementId.of("3"), new TestNode());
        ElementId four = innerSceneBuilder.registerNode(ElementId.of("4"), new TestNode());
        innerSceneBuilder.registerNode(one, new TestNode());
        innerSceneBuilder.registerNode(two, new TestNode());
        innerSceneBuilder.registerNode(three, new TestNode());
        innerSceneBuilder.registerNode(four, new TestNode());

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
        ElementId a = mainSceneBuilder.registerNode(ElementId.of("a"), new TestNode());
        ElementId b = mainSceneBuilder.registerSceneLink(innerSceneId);
        ElementId c = mainSceneBuilder.registerNode(ElementId.of("c"), new TestNode());
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

        ElementId one = innerSceneBuilder.registerNode(ElementId.of("1"), new TestNode());
        ElementId two = innerSceneBuilder.registerNode(ElementId.of("2"), new TestNode());
        ElementId three = innerSceneBuilder.registerNode(ElementId.of("3"), new TestNode());
        ElementId four = innerSceneBuilder.registerNode(ElementId.of("4"), new TestNode());
        innerSceneBuilder.registerNode(one, new TestNode());
        innerSceneBuilder.registerNode(two, new TestChooseNode());
        innerSceneBuilder.registerNode(three, new TestNode());
        innerSceneBuilder.registerNode(four, new TestNode());

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
                .to()
                .node(three)
                .to(one)
                .endBranch()
        ;

        final Scene<TestUserContext, TestScreenManager> innerScene = innerBuilder.build();

        System.out.println("inner scene: " + innerScene.toString());
        // внутренний сценарий

        // основной сценарий
        ElementId mainSceneId = ElementId.of("main_scene");
        ElementId a = mainSceneBuilder.registerNode(ElementId.of("a"), new TestNode());
        ElementId b = mainSceneBuilder.registerSceneLink(innerSceneId);
        ElementId c = mainSceneBuilder.registerNode(ElementId.of("c"), new TestChooseNode());
        ElementId d = mainSceneBuilder.registerNode(ElementId.of("d"), new TestNode());
        ElementId e = mainSceneBuilder.registerNode(ElementId.of("e"), new TestNode());
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
                .to()
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

        ElementId one = innerSceneBuilder.registerNode(ElementId.of("1"), new TestNode());
        ElementId two = innerSceneBuilder.registerNode(ElementId.of("2"), new TestNode());
        ElementId three = innerSceneBuilder.registerNode(ElementId.of("3"), new TestNode());
        innerSceneBuilder.registerNode(one, new TestChooseNode());
        innerSceneBuilder.registerNode(two, new TestNode());
        innerSceneBuilder.registerNode(three, new TestNode());

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
        ElementId a = mainSceneBuilder.registerNode(ElementId.of("a"), new TestChooseNode());
        ElementId b = mainSceneBuilder.registerNode(ElementId.of("b"), new TestNode());
        ElementId c = ElementId.of("c");
        mainSceneBuilder.registerSceneLink(c, innerSceneId);
        ElementId d = mainSceneBuilder.registerNode(ElementId.of("d"), new TestNode());
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

        ElementId one = innerSceneBuilder.registerNode(ElementId.of("1"), new TestNode());
        innerSceneBuilder.registerNode(one, new TestNode());

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
        ElementId a = mainSceneBuilder.registerNode(ElementId.of("a"), new TestNode());
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
    @Ignore("Сейчас кейс не поддержан. Исправить после добавления условий на ребра графа и выбор перехода по условию ребра")
    @Test
    public void testInnerSceneAsStartNode() {
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

        ElementId one = innerSceneBuilder.registerNode(ElementId.of("1"), new TestChooseNode());
        ElementId two = innerSceneBuilder.registerNode(ElementId.of("2"), new TestNode());
        ElementId three = innerSceneBuilder.registerNode(ElementId.of("3"), new TestChooseNode());

        // внутренний сценарий
        ElementId innerSceneId = ElementId.of("inner_scene");
        SceneNodeBuilder<TestUserContext, TestScreenManager> innerBuilder = innerSceneBuilder.graph()
                .node(one)
                .to(two, three)
                .endBranch();

        final Scene<TestUserContext, TestScreenManager> innerScene = innerBuilder.build();
        System.out.println("inner scene: " + innerScene.toString());
        // внутренний сценарий

        // основной сценарий
        ElementId mainSceneId = ElementId.of("main_scene");
        ElementId a = mainSceneBuilder.registerSceneLink(ElementId.of("a"), innerSceneId);
        ElementId b = mainSceneBuilder.registerNode(ElementId.of("b"), new TestNode());
        ElementId c = mainSceneBuilder.registerNode(ElementId.of("c"), new TestNode());
        SceneNodeBuilder<TestUserContext, TestScreenManager> mainGraphSceneBuilder = mainSceneBuilder.graph();
        mainGraphSceneBuilder
                .node(a)
                .to(b, c)
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

        userContext.nextId = three;
        plot.execute(1f); // 1
        userContext.nextId = c; // FIXME !!! это чисто логическая ошибка. Не должен подсценарий знать ноды основного
        plot.execute(1f); // 3

        Assert.assertTrue(plot.execute(1f)); // c
    }


}
