package com.packt.snake.model;

import com.packt.snake.GamePublisher;
import com.packt.snake.InputManager;
import com.packt.snake.ScreenManager;
import com.packt.snake.model.gameobject.Apple;
import com.packt.snake.model.gameobject.Snake;
import com.packt.snake.model.utils.GameHelper;
import com.packt.snake.model.utils.Vector;

import java.util.ArrayList;

/**
 * Created by user on 28.02.2017.
 */
public class WorldModel implements ScreenManager, GamePublisher.Subscriber {

    private InputManager inputManager;
    private SnakeInputHandler snakeInputHandler;
    private NewGameInputHandler newGameInputHandler;

    private static final float MOVE_TIME = 0.25F;
    private float timer = MOVE_TIME;

    private Snake snake;
    private Apple apple;

    private int score = 0;


    public WorldModel(InputManager inputManager) {
        this.inputManager = inputManager;
        GamePublisher.self().addListener(GamePublisher.State.NEW_GAME, this);
    }

    @Override
    public void handleGameState(GamePublisher.State state) {
        startNewGame();
        GamePublisher.self().changeState(GamePublisher.State.PLAYING);
    }

    public void startNewGame() {
        snake = new Snake();
        apple = new Apple();
        score = 0;

        timer = MOVE_TIME;
        unbindIputHandlers();
        bindInputHandlers();
    }

    private void unbindIputHandlers() {
        if (snakeInputHandler != null) {
            this.removeListener(snakeInputHandler);
            inputManager.removeHandler(snakeInputHandler);
        }
        if (newGameInputHandler != null) {
            GamePublisher.self().removeListener(GamePublisher.State.GAME_OVER, newGameInputHandler);
            inputManager.removeHandler(newGameInputHandler);
        }
    }

    private void bindInputHandlers() {
        snakeInputHandler = new SnakeInputHandler(snake);
        this.addListener(snakeInputHandler);
        inputManager.addHandler(snakeInputHandler);

        newGameInputHandler = new NewGameInputHandler();
        inputManager.addHandler(newGameInputHandler);
        GamePublisher.self().addListener(GamePublisher.State.GAME_OVER, newGameInputHandler);
    }

    @Override
    public void render(float delta) {
        timer -= delta;
        if (timer <= 0 && GamePublisher.self().getCurrentState() == GamePublisher.State.PLAYING) {
            timer = MOVE_TIME;
            updateWorld(delta);
            notifyListeners();
        }
    }

    private void updateWorld(float delta) {
        snake.updateHead();
        if (GameHelper.checkAppleCollision(apple, snake)) {
            apple.setAppleAvailable(false);
            snake.getBody().growUp();
            score++;
        } else {
            snake.getBody().move();
        }
        if (!apple.isAppleAvailable()) {
            Vector newApplePlace = GameHelper.newApplePlace(snake);
            apple.placeOnLocation(newApplePlace);
        }
        if (GameHelper.checkSnakeBodyCollision(snake.getBody(), snake.getHead())) {
            GamePublisher.self().changeState(GamePublisher.State.GAME_OVER);
        }
    }


    private ArrayList<Listener> listeners = new ArrayList<>();

    public void addListener(Listener listener) {
        listeners.add(listener);
    }

    public void removeListener(Listener listener) {
        listeners.remove(listener);
    }

    private void notifyListeners() {
        WorldEvent event = new WorldEvent(new WorldState(snake, apple), score);
        for (Listener listener : listeners) {
            listener.handleEvent(event);
        }
    }


    public interface Listener {
        void handleEvent(WorldEvent event);
    }


    public static class WorldEvent {

        private WorldState state;
        private int score;

        public WorldEvent(WorldState state, int score) {
            this.state = state;
            this.score = score;
        }

        public WorldState getState() {
            return state;
        }

        public int getScore() {
            return score;
        }
    }

    public static class WorldState {

        private Snake snake;
        private Apple apple;

        public WorldState(Snake snake, Apple apple) {
            this.snake = snake;
            this.apple = apple;
        }

        public Snake getSnake() {
            return snake;
        }

        public Apple getApple() {
            return apple;
        }

    }

}
