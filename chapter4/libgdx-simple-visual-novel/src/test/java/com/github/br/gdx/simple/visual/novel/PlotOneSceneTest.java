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
import com.github.br.gdx.simple.visual.novel.impl.TestNode;
import com.github.br.gdx.simple.visual.novel.impl.TestUserContext;
import org.junit.Assert;
import org.junit.Test;

public class PlotOneSceneTest {


    @Test
    public void testPlotWithOneSceneOneNode() {
        TestUserContext userContext = new TestUserContext();
        final SceneBuilder<TestUserContext> sceneBuilder = Scene.builder(
                SceneConfig.builder()
                        .build()
        );

        ElementId node1 = sceneBuilder.registerNode(new TestNode<TestUserContext>());
        final Scene<TestUserContext> scene = sceneBuilder.graph()
                .node(node1).end()
                .build();

        System.out.println(scene.toString());

        ElementId one = ElementId.of("one");
        Plot<TestUserContext> plot = Plot.<TestUserContext>builder(PlotConfig.builder().build())
                .setSceneManager(new DefaultSceneManager<TestUserContext>()
                        .addScene(one, new SceneSupplier<TestUserContext>() {

                                    @Override
                                    public Scene<TestUserContext> get() {
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
    public void testPlotWithOneSceneOneNodeTwoExecute() {
        TestUserContext userContext = new TestUserContext();
        final SceneBuilder<TestUserContext> sceneBuilder = Scene.builder(
                SceneConfig.builder()
                        .build()
        );

        ElementId node1 = sceneBuilder.registerNode(new TestNode<TestUserContext>());

        final Scene<TestUserContext> scene = sceneBuilder.graph()
                .node(node1).end()
                .build();

        System.out.println(scene.toString());

        ElementId one = ElementId.of("one");
        Plot<TestUserContext> plot = Plot.<TestUserContext>builder(PlotConfig.builder().build())
                .setSceneManager(new DefaultSceneManager<TestUserContext>()
                        .addScene(one, new SceneSupplier<TestUserContext>() {

                                    @Override
                                    public Scene<TestUserContext> get() {
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

    @Test
    public void testPlotWithOneScene_N_Nodes() {
        TestUserContext userContext = new TestUserContext();
        final SceneBuilder<TestUserContext> sceneBuilder = Scene.builder(
                SceneConfig.builder()
                        .build()
        );

        ElementId node1 = sceneBuilder.registerNode(new TestNode<TestUserContext>());
        ElementId node2 = sceneBuilder.registerNode(new TestNode<TestUserContext>());
        ElementId node3 = sceneBuilder.registerNode(new TestNode<TestUserContext>());
        ElementId node4 = sceneBuilder.registerNode(new TestNode<TestUserContext>());

        final Scene<TestUserContext> scene = sceneBuilder.graph()
                .node(node1).to()
                .node(node2).to()
                .node(node3).to()
                .node(node4).end()
                .build();

        System.out.println(scene.toString());

        ElementId one = ElementId.of("one");
        Plot<TestUserContext> plot = Plot.<TestUserContext>builder(PlotConfig.builder().build())
                .setSceneManager(new DefaultSceneManager<TestUserContext>()
                        .addScene(one, new SceneSupplier<TestUserContext>() {

                                    @Override
                                    public Scene<TestUserContext> get() {
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
    public void testPlotWithOneScene_N_Nodes_FSM_is_not_Finished() {
        TestUserContext userContext = new TestUserContext();
        final SceneBuilder<TestUserContext> sceneBuilder = Scene.builder(
                SceneConfig.builder()
                        .build()
        );

        ElementId node1 = sceneBuilder.registerNode(new TestNode<TestUserContext>());
        ElementId node2 = sceneBuilder.registerNode(new TestNode<TestUserContext>());
        ElementId node3 = sceneBuilder.registerNode(new TestNode<TestUserContext>());
        ElementId node4 = sceneBuilder.registerNode(new TestNode<TestUserContext>());

        final Scene<TestUserContext> scene = sceneBuilder.graph()
                .node(node1).to()
                .node(node2).to()
                .node(node3).to()
                .node(node4).end()
                .build();

        System.out.println(scene.toString());

        ElementId one = ElementId.of("one");
        Plot<TestUserContext> plot = Plot.<TestUserContext>builder(PlotConfig.builder().build())
                .setSceneManager(new DefaultSceneManager<TestUserContext>()
                        .addScene(one, new SceneSupplier<TestUserContext>() {

                                    @Override
                                    public Scene<TestUserContext> get() {
                                        return scene;
                                    }
                                }
                        ))
                .setBeginSceneId(one)
                .build();

        plot.execute(userContext);
        plot.execute(userContext);
        boolean result = plot.execute(userContext);
        Assert.assertFalse(result);
    }

    /**
     * digraph graphname {
     * a;
     * b;
     * c;
     * d;
     * a -> b;
     * b -> c;
     * b -> d;
     * c -> a;
     * }
     */
    @Test
    public void testPlotWithCycleJumpToPast() {
        final TestUserContext userContext = new TestUserContext();
        final SceneBuilder<TestUserContext> sceneBuilder = Scene.builder(
                SceneConfig.builder()
                        .build()
        );

        ElementId a = sceneBuilder.registerNode(ElementId.of("a"), new TestNode<TestUserContext>());
        ElementId b = sceneBuilder.registerNode(ElementId.of("b"), new TestNode<TestUserContext>());
        final ElementId c = sceneBuilder.registerNode(ElementId.of("c"), new TestNode<TestUserContext>());
        final ElementId d = sceneBuilder.registerNode(ElementId.of("d"), new TestNode<TestUserContext>());

        final Scene<TestUserContext> scene = sceneBuilder.graph()
                .node(a).to()
                .node(b)
                .to(
                        new Pair<ElementId, Predicate<TestUserContext>>(c, new Predicate<TestUserContext>() {
                            @Override
                            public boolean test(PlotContext<TestUserContext> context) {
                                return c == context.getUserContext().nextId;
                            }
                        }),
                        new Pair<ElementId, Predicate<TestUserContext>>(d, new Predicate<TestUserContext>() {
                            @Override
                            public boolean test(PlotContext<TestUserContext> context) {
                                return d == context.getUserContext().nextId;
                            }
                        })
                )
                .node(c).to(a).endBranch()
                .node(d).end()
                .build();
        System.out.println(scene.toString());

        ElementId one = ElementId.of("one");
        Plot<TestUserContext> plot = Plot.<TestUserContext>builder(PlotConfig.builder().build())
                .setSceneManager(new DefaultSceneManager<TestUserContext>()
                        .addScene(one, new SceneSupplier<TestUserContext>() {
                                    @Override
                                    public Scene<TestUserContext> get() {
                                        return scene;
                                    }
                                }
                        ))
                .setBeginSceneId(one)
                .build();

        plot.execute(userContext); // a
        userContext.nextId = c;
        plot.execute(userContext); // b
        plot.execute(userContext); // c
        plot.execute(userContext); // a
        userContext.nextId = d;
        plot.execute(userContext); // b
        boolean result = plot.execute(userContext); // d
        Assert.assertTrue(result);
    }

    /**
     * digraph graphname {
     * a;
     * b;
     * c;
     * d;
     * a -> b;
     * b -> c;
     * b -> d;
     * c -> a;
     * }
     */
    @Test
    public void testPlotWithCycleJumpToFuture() {
        final TestUserContext userContext = new TestUserContext();

        final SceneBuilder<TestUserContext> sceneBuilder = Scene.builder(
                SceneConfig.builder()
                        .build()
        );

        ElementId a = sceneBuilder.registerNode(ElementId.of("a"), new TestNode<TestUserContext>());
        ElementId b = sceneBuilder.registerNode(ElementId.of("b"), new TestNode<TestUserContext>());
        ElementId c = sceneBuilder.registerNode(ElementId.of("c"), new TestNode<TestUserContext>());
        final ElementId d = sceneBuilder.registerNode(ElementId.of("d"), new TestNode<TestUserContext>());

        SceneNodeBuilder<TestUserContext> graphBuilder = sceneBuilder.graph();
//                .node(a)
//                    .to(b, d)
//                .node(b)
//                    .to(c, d)
//                .node(c).to(a).endBranch()
//                .node(d).end()
//                .build();

        // основной сценарий
        graphBuilder.node(a).to().node(b).to().node(d).end();

        // второстепенные сюжетные завороты
        graphBuilder.node(a).to(d, new Predicate<TestUserContext>() {
            @Override
            public boolean test(PlotContext<TestUserContext> context) {
                return (userContext.nextId == d);
            }
        }).endBranch(); // заворот вперед
        graphBuilder.node(b).to().node(c).to(a).endBranch(); // заворот назад

        final Scene<TestUserContext> scene = graphBuilder.build();
        System.out.println(scene.toString());

        ElementId one = ElementId.of("one");
        Plot<TestUserContext> plot = Plot.<TestUserContext>builder(PlotConfig.builder().build())
                .setSceneManager(new DefaultSceneManager<TestUserContext>()
                        .addScene(one, new SceneSupplier<TestUserContext>() {
                                    @Override
                                    public Scene<TestUserContext> get() {
                                        return scene;
                                    }
                                }
                        ))
                .setBeginSceneId(one)
                .build();

        userContext.nextId = d;
        plot.execute(userContext); // a
        boolean result = plot.execute(userContext); // d
        Assert.assertTrue(result);
    }

}
