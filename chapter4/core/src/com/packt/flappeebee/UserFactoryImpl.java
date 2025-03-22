package com.packt.flappeebee;

import com.github.br.gdx.simple.structure.GameManager;
import com.github.br.gdx.simple.structure.UserFactory;

public class UserFactoryImpl implements UserFactory {

    private GameManager gameManager;

    @Override
    public void init(GameManager gameManager) {
        this.gameManager = gameManager;
    }

}
