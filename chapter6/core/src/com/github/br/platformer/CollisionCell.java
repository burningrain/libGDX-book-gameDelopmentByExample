package com.github.br.platformer;

import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;

public class CollisionCell {

    public final TiledMapTileLayer.Cell cell;
    public final int cellX;
    public final int cellY;

    public CollisionCell(TiledMapTileLayer.Cell cell, int cellX, int cellY) {
        this.cell = cell;
        this.cellX = cellX;
        this.cellY = cellY;
    }

    public boolean isEmpty() {
        return cell == null;
    }

}
