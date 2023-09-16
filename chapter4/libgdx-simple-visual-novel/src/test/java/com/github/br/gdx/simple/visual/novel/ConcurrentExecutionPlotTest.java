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
import com.github.br.gdx.simple.visual.novel.impl.GeneratorTestPlotIdImpl;
import com.github.br.gdx.simple.visual.novel.impl.TestNode;
import com.github.br.gdx.simple.visual.novel.impl.TestUserContext;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;

public class ConcurrentExecutionPlotTest {

    @Test
    public void testParallelExecutionBy_N_Users() throws InterruptedException {
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
        Plot<Integer, TestUserContext, CustomNodeVisitor> plot =
                Plot.<Integer, TestUserContext, CustomNodeVisitor>builder(
                        PlotConfig.<Integer>builder().setGeneratorPlotId(new GeneratorTestPlotIdImpl()).build())
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
        //

        ArrayList<Thread> threads = new ArrayList<>();
        for(int i = 0; i < 50; i++) {
            threads.add(createThread(plot, i));
        }

        for (Thread thread : threads) {
            thread.start();
        }
        for (Thread thread : threads) {
            thread.join();
        }


    }

    private Thread createThread(final Plot<Integer, TestUserContext, CustomNodeVisitor> plot, final int i) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                TestUserContext userContext = new TestUserContext();
                int plotId = i;
                ElementId elementId = ElementId.of(plotId + "");

                plot.execute(plotId, userContext);
                plot.execute(plotId);
                userContext.nextId = elementId;
                plot.execute(plotId);
                boolean result = plot.execute(plotId);

                Assert.assertTrue(result);
                Assert.assertEquals(elementId, userContext.nextId);
                System.out.println(Thread.currentThread().getName() + " has finished successfully");
            }
        });
        thread.setName("thread-" + i);
        thread.setUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(Thread t, Throwable e) {
                throw new RuntimeException(e);
            }
        });
        return thread;
    }

}
