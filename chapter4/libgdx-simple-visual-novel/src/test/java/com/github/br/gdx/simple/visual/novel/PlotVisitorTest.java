package com.github.br.gdx.simple.visual.novel;

import com.github.br.gdx.simple.visual.novel.api.ElementId;
import com.github.br.gdx.simple.visual.novel.api.Pair;
import com.github.br.gdx.simple.visual.novel.api.context.PlotContext;
import com.github.br.gdx.simple.visual.novel.api.edge.Predicate;
import com.github.br.gdx.simple.visual.novel.api.node.Node;
import com.github.br.gdx.simple.visual.novel.api.plot.*;
import com.github.br.gdx.simple.visual.novel.api.scene.*;
import com.github.br.gdx.simple.visual.novel.graph.GraphElementId;
import com.github.br.gdx.simple.visual.novel.impl.*;
import org.junit.Test;

public class PlotVisitorTest {


    @Test
    public void plotVisitorTest() {
        // ОПРЕДЕЛЕНИЕ ПРОЦЕССА
        final SceneBuilder<TestUserMapContext<ElementId, Boolean>, CustomNodeVisitor> mainSceneBuilder = Scene.builder(
                SceneConfig.builder()
                        .build()
        );

        // внутренняя сцена
        final SceneBuilder<TestUserMapContext<ElementId, Boolean>, CustomNodeVisitor> innerSceneBuilder = Scene.builder(
                SceneConfig.builder()
                        .build()
        );

        ElementId one = innerSceneBuilder.registerNode(ElementId.of("1"), new TestNode<TestUserMapContext<ElementId, Boolean>>());
        final ElementId two = innerSceneBuilder.registerNode(ElementId.of("2"), new TestNode2<TestUserMapContext<ElementId, Boolean>>());
        final ElementId three = innerSceneBuilder.registerNode(ElementId.of("3"), new TestNode3<TestUserMapContext<ElementId, Boolean>>());

        // внутренний сценарий
        ElementId innerSceneId = ElementId.of("inner_scene");
        SceneNodeBuilder<TestUserMapContext<ElementId, Boolean>, CustomNodeVisitor> innerBuilder = innerSceneBuilder.graph()
                .node(one)
                .to(new Pair<ElementId, Predicate<TestUserMapContext<ElementId, Boolean>>>(two, new Predicate<TestUserMapContext<ElementId, Boolean>>() {
                            @Override
                            public boolean test(PlotContext<TestUserMapContext<ElementId, Boolean>> context) {
                                return context.getUserContext().getNextId() == two;
                            }
                        }),
                        new Pair<ElementId, Predicate<TestUserMapContext<ElementId, Boolean>>>(three, new Predicate<TestUserMapContext<ElementId, Boolean>>() {
                            @Override
                            public boolean test(PlotContext<TestUserMapContext<ElementId, Boolean>> context) {
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
        final ElementId c = mainSceneBuilder.registerNode(ElementId.of("c"), new TestNode2<TestUserMapContext<ElementId, Boolean>>());
        SceneNodeBuilder<TestUserMapContext<ElementId, Boolean>, CustomNodeVisitor> mainGraphSceneBuilder = mainSceneBuilder.graph();
        mainGraphSceneBuilder
                .node(a)
                .to(
                        new Pair<ElementId, Predicate<TestUserMapContext<ElementId, Boolean>>>(b, new Predicate<TestUserMapContext<ElementId, Boolean>>() {
                            @Override
                            public boolean test(PlotContext<TestUserMapContext<ElementId, Boolean>> context) {
                                return context.getUserContext().getNextId() == b;
                            }
                        }),
                        new Pair<ElementId, Predicate<TestUserMapContext<ElementId, Boolean>>>(c, new Predicate<TestUserMapContext<ElementId, Boolean>>() {
                            @Override
                            public boolean test(PlotContext<TestUserMapContext<ElementId, Boolean>> context) {
                                return context.getUserContext().getNextId() == c;
                            }
                        })
                )
                .endBranch();

        final Scene<TestUserMapContext<ElementId, Boolean>, CustomNodeVisitor> scene = mainGraphSceneBuilder.build();
        System.out.println("main scene: " + scene.toString());

        Plot<TestUserMapContext<ElementId, Boolean>, CustomNodeVisitor> plot = Plot.<TestUserMapContext<ElementId, Boolean>, CustomNodeVisitor>builder(PlotConfig.builder().build())
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
        // ОПРЕДЕЛЕНИЕ ПРОЦЕССА

        final CustomNodeVisitor customNodeVisitor = new CustomNodeVisitor();
        plot.accept(new PlotVisitor<CustomNodeVisitor>() {

            @Override
            public void visitNode(ElementId sceneId, ElementId nodeId, Node<?, CustomNodeVisitor> node) {
                node.accept(sceneId, nodeId, customNodeVisitor);
                //todo прокинуть nodeId в посетителя
            }

            @Override
            public void visitEdge(ElementId sceneId, ElementId nodeId, Edge<?> edge) {
                System.out.println("scene node: " + sceneId + "\nedge: " + nodeId + "\nfrom: " + edge.getSourceId() + "\nto: " + edge.getDestId() + "\n");
            }

            @Override
            public void visitBeginNodeId(ElementId sceneId, GraphElementId beginNodeId) {
                System.out.println("scene node: " + sceneId + "\nbegin node: " + beginNodeId + "\n");
            }

            @Override
            public void visitBeginSceneId(ElementId sceneId) {
                System.out.println("begin scene: " + sceneId + "\n");
            }
        });

    }



}
