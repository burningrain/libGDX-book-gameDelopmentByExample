package com.github.br.libgdx.games.nutty.birds;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.physics.box2d.Body;

public class Pair {
    public Sprite sprite;
    public Body body;

    public Pair(Sprite sprite, Body body) {
        this.sprite = sprite;
        this.body = body;
    }

}
