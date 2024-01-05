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
import com.github.br.gdx.simple.visual.novel.api.scene.SceneNodeBuilder;
import com.github.br.gdx.simple.visual.novel.impl.CustomNodeVisitor;
import com.github.br.gdx.simple.visual.novel.impl.GeneratorTestPlotIdImpl;
import com.github.br.gdx.simple.visual.novel.impl.TestNode;
import com.github.br.gdx.simple.visual.novel.impl.TestUserContext;
import org.junit.Test;

public class PlotDotVisitorTest {


    @Test
    public void testDotPlotVisitor() {
        final TestUserContext userContext = new TestUserContext();

        // внутренняя сцена
        final Scene<TestUserContext, CustomNodeVisitor> innerScene = createInnerScene();
        ElementId innerSceneId = ElementId.of("innerScene");

        // основная сцена
        final SceneBuilder<TestUserContext, CustomNodeVisitor> mainSceneBuilder = Scene.builder(
                SceneConfig.builder()
                        .setDefaultNodeType(NodeType.WAITING_INPUT)
                        .build()
        );

        ElementId mainSceneId = ElementId.of("main_scene");
        ElementId a = mainSceneBuilder.registerSceneLink(ElementId.of("a"), innerSceneId);
        ElementId b = mainSceneBuilder.registerSceneLink(ElementId.of("b"), innerSceneId);
        ElementId c = mainSceneBuilder.registerSceneLink(ElementId.of("c"), innerSceneId);
        ElementId d = mainSceneBuilder.registerSceneLink(ElementId.of("d"), innerSceneId);
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

        final Scene<TestUserContext, CustomNodeVisitor> mainScene = mainGraphSceneBuilder.build();

        Plot<Integer, TestUserContext, CustomNodeVisitor> plot =
                Plot.<Integer, TestUserContext, CustomNodeVisitor>builder(
                                PlotConfig.<Integer>builder().setGeneratorPlotId(new GeneratorTestPlotIdImpl()).build()
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
                        )
                        .setBeginSceneId(mainSceneId)
                        .build();

        System.out.println(plot.getPlotAsDot());
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

}
