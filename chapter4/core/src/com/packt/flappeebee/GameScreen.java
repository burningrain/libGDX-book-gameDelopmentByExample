package com.packt.flappeebee;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.packt.flappeebee.model.World;
import com.packt.flappeebee.render.RenderDispatcher;
import com.github.br.ecs.simple.utils.ViewHelper;


public class GameScreen extends ScreenAdapter {

    private RenderDispatcher renderDispatcher;
    private World world;

    @Override
    public void show() {
        world = new World();
        renderDispatcher = new RenderDispatcher(world);

        GamePublisher.self().changeState(GamePublisher.State.NEW_GAME);
    }

    @Override
    public void resize(int width, int height) {
        ViewHelper.viewport.update(width, height);
    }

    @Override
    public void render(float delta) {
        clearScreen();                   // очищаем экран
        // обработка клавиш теперь размазана по коду
        world.update(delta);             // обновление игрового мира (состояние и рендеринг)
        renderDispatcher.update(delta);  // дополнительный рендеринг (HUD или подобное)

        //TODO пробросить вывод runtime-ошибок на экран
    }

    private void clearScreen() {
        Gdx.gl.glClearColor(Color.BLACK.r, Color.BLACK.g, Color.BLACK.b, Color.BLACK.a);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    }

}
