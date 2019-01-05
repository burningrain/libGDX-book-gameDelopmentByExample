package com.github.br.gdx.simple.console;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.github.br.gdx.simple.console.exception.ConsoleException;

/**
 * Created by user on 03.01.2019.
 */
public class ConsoleUI implements ConsoleService.ConsoleOutput {

    public static final String FPS = "FPS: ";

    private Viewport viewport;

    private Stage stage;
    private Table windowTable;
    private VerticalGroup consoleGroup;

    private TextField consoleTextField;
    private Label textArea;

    private Label fpsLabel;

    private ConsoleService consoleService;

    public ConsoleUI(ConsoleService consoleService, Skin skin, Viewport viewport) {
        this.viewport = viewport;
        initUI(skin, viewport);
        this.consoleService = consoleService;
        this.consoleService.setConsoleOutput(this);
    }

    @Override
    public void message(String message) {
        textArea.setText(message);
    }

    public void update(float delta) {
        stage.getBatch().setProjectionMatrix(viewport.getCamera().projection);
        stage.getBatch().setTransformMatrix(viewport.getCamera().view);

        fpsLabel.setText(FPS + Gdx.graphics.getFramesPerSecond());
        stage.draw();
    }

    public InputProcessor getInputProcessor() {
        return stage;
    }

    private void initUI(Skin skin, Viewport viewport) {
        stage = new Stage(viewport);
        windowTable = new Table(skin);
        consoleGroup = new VerticalGroup();
        consoleTextField = new TextField("", skin);
        textArea = new Label(" ", skin);
        fpsLabel = new Label(FPS, skin);

        consoleGroup.addActor(textArea);
        consoleGroup.addActor(consoleTextField);
        consoleGroup.fill();

        windowTable.add().top().expand();
        windowTable.add(fpsLabel).top().right().expand();
        windowTable.row();
        windowTable.add(consoleGroup).colspan(2).bottom().fill();

        windowTable.setFillParent(true);
        //stage.setDebugAll(true);
        stage.addActor(windowTable);
        stage.setKeyboardFocus(consoleTextField);
        //show the keyboard
        consoleTextField.getOnscreenKeyboard().show(true);

        //setTransparentBackground(textArea, new Color(0, 0, 0, 0.1f));
        setTransparentBackground(consoleTextField, new Color(0, 0, 0, 0.2f));
        consoleTextField.setTextFieldListener(new TextField.TextFieldListener() {
            @Override
            public void keyTyped(TextField textField, char key) {
                if ((key == '\r' || key == '\n')){
                    try {
                        consoleService.interpretInput(consoleTextField.getText());
                    } catch (ConsoleException e) {
                        e.printStackTrace();
                        textArea.setText(e.getMessage());
                    }
                }
            }
        });
    }

    private static void setTransparentBackground(TextField textField, Color color) {
        Image image = getBackgroundImage((int)textField.getWidth(), (int)textField.getHeight(), color);

        TextField.TextFieldStyle oldStyle = textField.getStyle();
        TextField.TextFieldStyle newStyle = new TextField.TextFieldStyle(oldStyle);
        newStyle.background = image.getDrawable();
        textField.setStyle(newStyle);
    }

    private static void setTransparentBackground(Label label, Color color) {
        Image image = getBackgroundImage((int)label.getWidth(), (int)label.getHeight(), color);

        Label.LabelStyle oldStyle = label.getStyle();
        Label.LabelStyle newStyle = new Label.LabelStyle(oldStyle);
        newStyle.background = image.getDrawable();
        label.setStyle(newStyle);
    }

    private static Image getBackgroundImage(int width, int height, Color color) {
        Pixmap labelColor = new Pixmap(width, height, Pixmap.Format.RGBA8888);
        labelColor.setColor(color);
        labelColor.fill();
        Image image = new Image(new Texture(labelColor));
        labelColor.dispose();
        return image;
    }

}
