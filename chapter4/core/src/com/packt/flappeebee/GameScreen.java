package com.packt.flappeebee;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.github.br.ecs.simple.utils.ViewHelper;
import com.packt.flappeebee.model.EcsWorld;


public class GameScreen extends ScreenAdapter {

    private EcsWorld ecsWorld;

    @Override
    public void show() {
        boolean npotSupported = Gdx.graphics.supportsExtension("GL_OES_texture_npot")
                || Gdx.graphics.supportsExtension("GL_ARB_texture_non_power_of_two");
        System.out.println(npotSupported);
        ecsWorld = new EcsWorld();
    }

    @Override
    public void resize(int width, int height) {
        ViewHelper.viewport.update(width, height, true);
    }

    @Override
    public void render(float delta) {
        clearScreen();                   // очищаем экран
        // обработка клавиш теперь размазана по коду
        ecsWorld.update(delta);             // обновление игрового мира (состояние и рендеринг)

        //TODO пробросить вывод runtime-ошибок на экран
    }

    private void clearScreen() {
        Gdx.gl.glClearColor(Color.BLACK.r, Color.BLACK.g, Color.BLACK.b, Color.BLACK.a);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    }

}
