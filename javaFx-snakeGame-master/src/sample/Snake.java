package sample;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Snake extends Application {

    public static final int BLOCK_SIZE = 20;
    public static final int APP_W = 50 * BLOCK_SIZE;
    public static final int APP_H = 35 * BLOCK_SIZE;

    private Direction direction = Direction.RIGHT;
    private boolean isMoved = false;
    private boolean isRunning = false;
    private final Timeline timeline = new Timeline();
    private ObservableList<Node> snake;

    public Parent createContent() {

        Pane root = new Pane();
        root.setPrefSize(APP_W, APP_H);
        Group snakeBody = new Group();
        snake = snakeBody.getChildren();

        Rectangle food = new Rectangle(BLOCK_SIZE, BLOCK_SIZE);
        food.setFill(Paint.valueOf("#2196f3"));
        food.setTranslateX((int) (Math.random() * (APP_W - BLOCK_SIZE)) / BLOCK_SIZE * BLOCK_SIZE);
        food.setTranslateY((int) (Math.random() * (APP_H - BLOCK_SIZE)) / BLOCK_SIZE * BLOCK_SIZE);

        KeyFrame frame = new KeyFrame(Duration.seconds(0.1), e -> {

            if (!isRunning)
                return;
            boolean toRemove = snake.size() > 1;

            Node tail = toRemove ? snake.remove(snake.size() - 1) : snake.get(0);

            double tailX = tail.getTranslateX();
            double tailY = tail.getTranslateY();

            switch (direction) {
                case UP:
                    tail.setTranslateX(snake.get(0).getTranslateX());
                    tail.setTranslateY(snake.get(0).getTranslateY() - BLOCK_SIZE);
                    break;
                case DOWN:
                    tail.setTranslateX(snake.get(0).getTranslateX());
                    tail.setTranslateY(snake.get(0).getTranslateY() + BLOCK_SIZE);
                    break;
                case LEFT:
                    tail.setTranslateX(snake.get(0).getTranslateX() - BLOCK_SIZE);
                    tail.setTranslateY(snake.get(0).getTranslateY());
                    break;
                case RIGHT:
                    tail.setTranslateX(snake.get(0).getTranslateX() + BLOCK_SIZE);
                    tail.setTranslateY(snake.get(0).getTranslateY());
                    break;
            }

            isMoved = true;
            if (toRemove)
                snake.add(0, tail);

            for (Node node : snake) {
                if (node != tail && node.getTranslateX() == tail.getTranslateX() && node.getTranslateY() == tail.getTranslateY())
                    restartGame();
            }

            if (tail.getTranslateX() < 0 || tail.getTranslateX() > APP_W || tail.getTranslateY() < 0 || tail.getTranslateY() > APP_H)
                restartGame();

            if(tail.getTranslateX() == food.getTranslateX() && tail.getTranslateY() == food.getTranslateY()) {
                food.setTranslateX((int) (Math.random() * (APP_W - BLOCK_SIZE)) / BLOCK_SIZE * BLOCK_SIZE);
                food.setTranslateY((int) (Math.random() * (APP_H - BLOCK_SIZE)) / BLOCK_SIZE * BLOCK_SIZE);

                Rectangle newNode = new Rectangle(BLOCK_SIZE, BLOCK_SIZE);
                newNode.setTranslateX(tailX);
                newNode.setTranslateY(tailY);
                snake.add(newNode);
            }

        });

        timeline.getKeyFrames().add(frame);
        timeline.setCycleCount(Timeline.INDEFINITE);

        root.getChildren().addAll(food, snakeBody);
        return root;
    }

    private void restartGame() {
        stopGame();
        startGame();
    }

    private void startGame() {
        direction = Direction.RIGHT;
        Rectangle head = new Rectangle(BLOCK_SIZE, BLOCK_SIZE);
        snake.add(head);
        timeline.play();
        isRunning = true;
    }

    private void stopGame() {
        isRunning = false;
        timeline.stop();
        snake.clear();
    }

    public void pauseGame() {
        isRunning = false;
    }

    void continueGame() {
        isRunning = true;
    }


    @Override
    public void start(Stage stage) {
        Scene scene = new Scene(createContent());
        scene.setOnKeyPressed(e -> {
            if(!isMoved)
                return;

            switch (e.getCode()) {
                case W:
                    if(direction != Direction.DOWN)
                        direction = Direction.UP;
                    break;
                case S:
                    if(direction != Direction.UP)
                        direction = Direction.DOWN;
                    break;
                case A:
                    if(direction != Direction.RIGHT)
                        direction = Direction.LEFT;
                    break;
                case D:
                    if(direction != Direction.LEFT)
                        direction = Direction.RIGHT;
                    break;
                case Q:
                    pauseGame();
                    break;
                case E:
                    continueGame();
                    break;
            }
        });

        stage.setScene(scene);
        stage.setTitle("Snake Game");
        stage.setMaximized(true);
        stage.show();
        startGame();
    }

    public static void main(String[] args) {
        Application.launch(Snake.class);
    }
}