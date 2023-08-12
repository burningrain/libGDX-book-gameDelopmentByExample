package com.github.br.gdx.simple.visual.novel.api.context;

import com.github.br.gdx.simple.visual.novel.Utils;
import com.github.br.gdx.simple.visual.novel.api.screen.ScreenManager;

public class ServiceContext<SM extends ScreenManager> {

    private SM currentScreenManager;

    public SM getCurrentScreenManager() {
        return currentScreenManager;
    }

    public void setCurrentScreenManager(SM currentScreenManager) {
        this.currentScreenManager = Utils.checkNotNull(currentScreenManager, "screenManager");
    }

}
