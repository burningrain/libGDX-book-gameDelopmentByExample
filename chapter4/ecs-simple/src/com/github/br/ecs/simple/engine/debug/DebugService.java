package com.github.br.ecs.simple.engine.debug;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.github.br.ecs.simple.engine.EcsSimple;
import com.github.br.ecs.simple.engine.IDebugSystem;
import com.github.br.ecs.simple.engine.IEcsSystem;
import com.github.br.ecs.simple.engine.debug.console.ConsoleService;
import com.github.br.ecs.simple.engine.debug.console.exception.ConsoleException;
import com.github.br.ecs.simple.engine.debug.data.DebugData;
import com.github.br.ecs.simple.engine.debug.drawobject.DebugDrawObject;
import com.github.br.ecs.simple.utils.ViewHelper;

import java.util.LinkedHashMap;
import java.util.Map;


/**
 * Сервис, выводящий на экран дебажную информацию.
 */
public class DebugService {

    private ConsoleService consoleService;
    private LinkedHashMap<Class<? extends IDebugSystem>, IDebugSystem> systems =
            new LinkedHashMap<Class<? extends IDebugSystem>, IDebugSystem>();

    private ShapeRenderer shapeRenderer = new ShapeRenderer();
    private SpriteBatch spriteBatch = new SpriteBatch();

    private Stage stage = new Stage();
    private Table windowTable = new Table(DebugDrawObject.DEFAULT_SKIN);
    private VerticalGroup stack = new VerticalGroup();
    private ScrollPane scrollPane = new ScrollPane(stack, DebugDrawObject.DEFAULT_SKIN);
    private VerticalGroup consoleGroup = new VerticalGroup();
    private TextField consoleTextField = new TextField("", DebugDrawObject.DEFAULT_SKIN);
    private Label textArea = new Label("ssssssssssss \n aaaaaa \n vvvvv \n rrrrr \n", DebugDrawObject.DEFAULT_SKIN);

    private LibGdxPanel libGdxPanel = new LibGdxPanel() {
        @Override
        public void add(Actor actor) {
            stack.addActor(actor);
        }
    };

    private Label timeLabel = new Label("TIME", DebugDrawObject.DEFAULT_SKIN);
    private Label fpsLabel = new Label("FPS", DebugDrawObject.DEFAULT_SKIN);

    public DebugService(final ConsoleService consoleService) {
        this.consoleService = consoleService;
        this.consoleService.setConsoleOutput(new ConsoleService.ConsoleOutput() {
            @Override
            public void message(String message) {
                textArea.setText(message);
            }
        });
        consoleTextField.setTextFieldListener(new TextField.TextFieldListener() {
            @Override
            public void keyTyped(TextField textField, char key) {
                if ((key == '\r' || key == '\n')){
                    try {
                        consoleService.interpretInput(consoleTextField.getText());
                    } catch (ConsoleException e) {
                        textArea.setText(e.getMessage());
                    }
                }
            }
        });

        windowTable.setFillParent(true);
        stage.setDebugAll(true);
        stage.addActor(windowTable);
        Gdx.input.setInputProcessor(stage);

        consoleGroup.addActor(textArea);
        consoleGroup.addActor(consoleTextField);
        consoleGroup.fill();

        windowTable.add(scrollPane).width(200f).fill();
        windowTable.add(timeLabel).top().expand();
        windowTable.add(fpsLabel).top().right().expand();
        windowTable.row();
        windowTable.add(consoleGroup).colspan(3).bottom().fill();

    }

    public void addSystem(IDebugSystem system) {
        systems.put(system.getClass(), system);
    }

    public void update(float delta) {
        ViewHelper.applyCameraAndViewPort(shapeRenderer);
        ViewHelper.applyCameraAndViewPort(spriteBatch);
        ViewHelper.applyCameraAndViewPort(stage.getBatch());

        stack.clear();
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        spriteBatch.begin();
        fpsLabel.setText("FPS: " + EcsSimple.ECS.fps());
        timeLabel.setText(prettyPrintTime());
        drawDebugData(shapeRenderer, spriteBatch, libGdxPanel);
        shapeRenderer.end();
        spriteBatch.end();
        stage.draw();
    }

    private void drawDebugData(final ShapeRenderer shapeRenderer, final SpriteBatch spriteBatch, final LibGdxPanel libGdxPanel) {
        for (Map.Entry<Class<? extends IDebugSystem>, IDebugSystem> entry : systems.entrySet()) {
            Class<? extends IEcsSystem> systemClazz = entry.getKey();
            if(isNeedDrawSystem(systemClazz)) {
                drawSystemName(systemClazz, libGdxPanel);

                DebugDataContainer debugDataContainer = entry.getValue().getDebugData();
                debugDataContainer.forEach(new DebugDataContainer.Callback() {
                    @Override
                    public void call(DebugData debugData) {
                        DebugRendererObjectFactory.getDebugDrawObject(debugData.getClass())
                                .draw(shapeRenderer, spriteBatch, libGdxPanel, debugData);
                    }
                });
            }
        }
    }

    private static void drawSystemName(Class<? extends IEcsSystem> systemClazz, LibGdxPanel libGdxPanel) {
        Label label = new Label(systemClazz.getSimpleName(), DebugDrawObject.DEFAULT_SKIN);
        label.setColor(Color.GOLD);
        libGdxPanel.add(label);
    }

    private static String prettyPrintTime() {
        int seconds = EcsSimple.ECS.TIMER.getSecs();
        return EcsSimple.ECS.TIMER.getMins() + ":" + ((seconds < 10)? "0" + seconds : seconds);
    }


    private boolean isNeedDrawSystem(Class<? extends IEcsSystem> key) {
        return systems.get(key).isDebugMode();
    }

}
