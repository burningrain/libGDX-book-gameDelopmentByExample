package com.packt.snake.model.gameobject;

import com.badlogic.gdx.utils.Array;
import com.packt.snake.model.utils.GameHelper;
import com.packt.snake.model.utils.Vector;

/**
 * Created by user on 27.02.2017.
 */
public class Snake {

    private static final int SNAKE_MOVEMENT = 1;

    public static final int RIGHT = 0;
    public static final int LEFT = 1;
    public static final int UP = 2;
    public static final int DOWN = 3;
    private int snakeDirection;

    private Vector head = new Vector();
    private SnakeBody body = new SnakeBody(head);


    public static class SnakeBody {

        private Vector currentHead;
        private Vector prevHead = new Vector();

        private Array<Vector> segments = new Array<>(); // первый ближе к голове, последний - к хвосту

        public SnakeBody(Vector head) {
            this.currentHead = head;
            this.prevHead.x = head.x;
            this.prevHead.y = head.y;

            for (int i = 0; i < 3; i++) {
                segments.add(new Vector(head.x - i - 1, head.y));
            }
        }

        public void move() {
            if (currentHead.equals(prevHead))
                throw new IllegalStateException("Вы пытаетесь двинуть тело за головой, хотя положение головы не изменилось!");
            growUp();
            growDown();
        }

        private void updatePrevHead() {
            this.prevHead.x = this.currentHead.x;
            this.prevHead.y = this.currentHead.y;
        }

        public void growUp() {
            segments.insert(0, new Vector(prevHead.x, prevHead.y));
            updatePrevHead();
        }

        public Vector growDown() {
            return segments.removeIndex(segments.size - 1);
        }

        public Array<Vector> getSegments() {
            return segments;
        }
    }

    public void updateHead() {
        moveHead();
        this.head = GameHelper.checkForOutOfBounds(head);
    }


    private void moveHead() {
        switch (snakeDirection) {
            case RIGHT:
                head.x += SNAKE_MOVEMENT;
                break;
            case LEFT:
                head.x -= SNAKE_MOVEMENT;
                break;
            case UP:
                head.y += SNAKE_MOVEMENT;
                break;
            case DOWN:
                head.y -= SNAKE_MOVEMENT;
                break;
        }
    }

    public void setSnakeDirection(int snakeDirection) {
        this.snakeDirection = snakeDirection;
    }

    public int getSnakeDirection() {
        return snakeDirection;
    }

    public Vector getHead() {
        return head;
    }

    public SnakeBody getBody() {
        return body;
    }
}
