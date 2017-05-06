package com.packt.snake.model.gameobject;

import com.packt.snake.model.utils.Vector;

/**
 * Created by user on 27.02.2017.
 */
public class Apple {

    private Vector coords = new Vector();
    private boolean appleAvailable = false;


    public void placeOnLocation(Vector newApplePlace){
            this.setAppleAvailable(true);
            coords.x = newApplePlace.x;
            coords.y = newApplePlace.y;
    }


    public Vector getCoords() {
        return coords;
    }

    public boolean isAppleAvailable() {
        return appleAvailable;
    }

    public void setAppleAvailable(boolean appleAvailable) {
        this.appleAvailable = appleAvailable;
    }
}
