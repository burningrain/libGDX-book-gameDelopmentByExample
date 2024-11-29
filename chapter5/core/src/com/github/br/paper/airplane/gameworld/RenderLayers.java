package com.github.br.paper.airplane.gameworld;

public enum RenderLayers {

    BACKGROUND((byte) 0),
    BACK2((byte) 1),
    BACK((byte) 2),
    DEFAULT((byte) 3),
    FORWARD((byte) 4),
    FORWARD2 ((byte) 5)
    ;

    private final byte layer;

    RenderLayers(byte layer) {
        this.layer = layer;
    }

    public byte getLayer() {
        return layer;
    }


}
