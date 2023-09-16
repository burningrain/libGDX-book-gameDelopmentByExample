package com.github.br.gdx.simple.visual.novel;

import com.github.br.gdx.simple.visual.novel.api.ElementId;
import com.github.br.gdx.simple.visual.novel.api.context.PlotContext;
import com.github.br.gdx.simple.visual.novel.api.exception.PlotException;
import com.github.br.gdx.simple.visual.novel.api.exception.PlotExceptionHandler;
import com.github.br.gdx.simple.visual.novel.api.node.Node;
import com.github.br.gdx.simple.visual.novel.api.node.NodeResult;
import com.github.br.gdx.simple.visual.novel.api.node.NodeType;
import com.github.br.gdx.simple.visual.novel.api.plot.DefaultSceneManager;
import com.github.br.gdx.simple.visual.novel.api.plot.Plot;
import com.github.br.gdx.simple.visual.novel.api.plot.PlotConfig;
import com.github.br.gdx.simple.visual.novel.api.plot.SceneSupplier;
import com.github.br.gdx.simple.visual.novel.api.scene.Scene;
import com.github.br.gdx.simple.visual.novel.api.scene.SceneBuilder;
import com.github.br.gdx.simple.visual.novel.api.scene.SceneConfig;
import com.github.br.gdx.simple.visual.novel.impl.CustomNodeVisitor;
import com.github.br.gdx.simple.visual.novel.impl.GeneratorTestPlotIdImpl;
import com.github.br.gdx.simple.visual.novel.impl.TestNode;
import com.github.br.gdx.simple.visual.novel.impl.TestUserContext;
import org.junit.Assert;
import org.junit.Test;

import java.util.concurrent.atomic.AtomicBoolean;

public class ExceptionHandlerTest {

    @Test
    public void testThrowExceptionWhenExceptionHandlerIsNull() {
        TestUserContext userContext = new TestUserContext();
        final SceneBuilder<TestUserContext, CustomNodeVisitor> sceneBuilder = Scene.builder(
                SceneConfig.builder()
                        .setDefaultNodeType(NodeType.NOT_WAITING)
                        .build()
        );

        ElementId node1 = sceneBuilder.registerNode(new TestNode<TestUserContext>());
        ElementId node2 = sceneBuilder.registerNode(new TestNode<TestUserContext>());
        ElementId node3 = sceneBuilder.registerNode(new Node<TestUserContext, CustomNodeVisitor>() {
            @Override
            public NodeResult execute(PlotContext<?, TestUserContext> plotContext, boolean isVisited) {
                throw new RuntimeException();
            }

            @Override
            public void accept(ElementId sceneId, ElementId nodeId, CustomNodeVisitor visitor) {

            }
        });
        ElementId node4 = sceneBuilder.registerNode(new TestNode<TestUserContext>());

        final Scene<TestUserContext, CustomNodeVisitor> scene = sceneBuilder.graph()
                .node(node1).to()
                .node(node2).to()
                .node(node3).to()
                .node(node4).end()
                .build();

        ElementId one = ElementId.of("one");
        Plot<Integer, TestUserContext, CustomNodeVisitor> plot =
                Plot.<Integer, TestUserContext, CustomNodeVisitor>builder(
                                PlotConfig.<Integer>builder().setGeneratorPlotId(new GeneratorTestPlotIdImpl()).build()
                        )
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

        int plotId = 1;

        AtomicBoolean isHasEx = new AtomicBoolean(false);
        try {
            plot.execute(plotId, userContext);
        } catch (Exception e) {
            isHasEx.set(true);
            e.printStackTrace();
        }

        Assert.assertTrue(isHasEx.get());
    }

    @Test
    public void testExecuteExceptionHandlerWhenIsNotNull() {
        TestUserContext userContext = new TestUserContext();
        final SceneBuilder<TestUserContext, CustomNodeVisitor> sceneBuilder = Scene.builder(
                SceneConfig.builder()
                        .setDefaultNodeType(NodeType.NOT_WAITING)
                        .build()
        );

        ElementId node1 = sceneBuilder.registerNode(new TestNode<TestUserContext>());
        ElementId node2 = sceneBuilder.registerNode(new TestNode<TestUserContext>());
        ElementId node3 = sceneBuilder.registerNode(new Node<TestUserContext, CustomNodeVisitor>() {
            @Override
            public NodeResult execute(PlotContext<?, TestUserContext> plotContext, boolean isVisited) {
                throw new RuntimeException();
            }

            @Override
            public void accept(ElementId sceneId, ElementId nodeId, CustomNodeVisitor visitor) {

            }
        });
        ElementId node4 = sceneBuilder.registerNode(new TestNode<TestUserContext>());

        final Scene<TestUserContext, CustomNodeVisitor> scene = sceneBuilder.graph()
                .node(node1).to()
                .node(node2).to()
                .node(node3).to()
                .node(node4).end()
                .build();

        final AtomicBoolean isHandled = new AtomicBoolean(false);

        ElementId one = ElementId.of("one");
        Plot<Integer, TestUserContext, CustomNodeVisitor> plot =
                Plot.<Integer, TestUserContext, CustomNodeVisitor>builder(
                                PlotConfig.<Integer>builder()
                                        .setGeneratorPlotId(new GeneratorTestPlotIdImpl())
                                        .build()
                        )
                        .setSceneManager(new DefaultSceneManager<TestUserContext, CustomNodeVisitor>()
                                .addScene(one, new SceneSupplier<TestUserContext, CustomNodeVisitor>() {

                                            @Override
                                            public Scene<TestUserContext, CustomNodeVisitor> get() {
                                                return scene;
                                            }
                                        }
                                ))
                        .setBeginSceneId(one)
                        .setExceptionHandler(new PlotExceptionHandler<Integer, TestUserContext>() {
                            @Override
                            public void handle(Exception e, PlotContext<Integer, TestUserContext> plotContext) {
                                e.printStackTrace();
                                isHandled.set(true);
                            }
                        })
                        .build();

        int plotId = 1;
        plot.execute(plotId, userContext);

        Assert.assertTrue(isHandled.get());
    }

    //todo добавить название подпроцесса в ссылке на подроцесс

    //todo в отрисовке .dot бага! в случае N подсценариев одного типа будет отображен только 1.
    //todo то есть если в процессе условно N  SceneLink-ов на одну и ту же сцену, то будет не N, а 1. Это неверно.

    //todo добавить замену спецсимволов .dot при генерации идентификаторов. Иначе падать же будет.

}
