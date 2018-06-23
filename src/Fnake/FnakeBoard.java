package Fnake;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class FnakeBoard extends Application {
    private int tileSize = 10;
    private int speed = 200;

    private int width;
    private int height;
    private int[][] matrix;

    private List<Tuple> snake = new ArrayList<>();

    private Tuple food;
    private Tuple bonus;
    private int score = 0;

    private final int BOARD = 0;
    private final int SNAKE = 1;
    private final int FOOD = 2;
    private final int BONUS = 3;

    private final int NORTH = 0;
    private final int SOUTH = 1;
    private final int WEST = 2;
    private final int EAST = 3;

    private int direction = NORTH;


    FnakeBoard(int columns, int rows) {
        this.width = columns * this.tileSize;
        this.height = rows * this.tileSize;

        this.matrix = new int[columns][rows];

        this.snake.add(new Tuple(columns / 2, rows - 1));
        this.matrix[columns / 2][rows - 1] = 1;
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Fnake Game");
        Group root = new Group();
        Canvas canvas = new Canvas(this.width, this.height);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        root.getChildren().add(canvas);
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.show();

        scene.setOnKeyPressed(event -> {
            switch (event.getCode()) {
                case UP:
                    direction = NORTH;
                    break;
                case DOWN:
                    direction = SOUTH;
                    break;
                case LEFT:
                    direction = WEST;
                    break;
                case RIGHT:
                    direction = EAST;
                    break;
            }
        });

        startTask(gc);
    }

    private void startTask(GraphicsContext gc) {
        Runnable task = () -> runTask(gc);

        Thread backgroudThread = new Thread(task);
        backgroudThread.setDaemon(true);
        backgroudThread.start();

    }

    private void runTask(GraphicsContext gc) {
        //noinspection InfiniteLoopStatement
        while (true) {
            Tuple nextTile = nextTile(this.snake.get(this.snake.size() - 1));
            moveSnake(nextTile);
            drawBoard(gc);
            try {
                TimeUnit.MILLISECONDS.sleep(this.speed);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }

    }

    private void drawBoard(GraphicsContext gc) {
        for (int x = 0; x < this.matrix.length; x++) {
            for (int y = 0; y < this.matrix[0].length; y++) {
                if (this.matrix[x][y] == BOARD) {
                    gc.setFill(Color.WHITE);
                }
                if (this.matrix[x][y] == SNAKE) {
                    gc.setFill(Color.RED);
                }
                if (this.matrix[x][y] == FOOD) {
                    gc.setFill(Color.GREEN);
                }
                if (this.matrix[x][y] == BONUS) {
                    gc.setFill(Color.DARKGREEN);
                }
                gc.fillRect(
                        x * this.tileSize,
                        y * this.tileSize,
                        this.tileSize,
                        this.tileSize
                );
            }
        }
    }

    private Tuple nextTile(Tuple currentTile) {
        switch (this.direction) {
            case SOUTH:
                int var = this.matrix[0].length;
                if (currentTile.getY() < this.matrix[0].length - 1) {
                    return new Tuple(currentTile.getX(), currentTile.getY() + 1);
                } else return new Tuple(currentTile.getX(), 0);
            case EAST:
                if (currentTile.getX() < this.matrix.length - 1) {
                    return new Tuple(currentTile.getX() + 1, currentTile.getY());
                } else return new Tuple(0, currentTile.getY());
            case WEST:
                if (currentTile.getX() > 0) {
                    return new Tuple(currentTile.getX() - 1, currentTile.getY());
                } else return new Tuple(this.matrix.length - 1, currentTile.getY());
            default: // NORTH
                if (currentTile.getY() > 0) {
                    return new Tuple(currentTile.getX(), currentTile.getY() - 1);
                } else return new Tuple(currentTile.getX(), this.matrix[0].length - 1);

        }
    }

    private void extendSnake(Tuple tuple) {
        this.snake.add(tuple);
        this.matrix[tuple.getX()][tuple.getY()] = 1;
    }

    private void moveSnake(Tuple tuple) {
        this.snake.add(tuple);
        this.matrix[tuple.getX()][tuple.getY()] = 1;
        Tuple toRemove = this.snake.get(0);
        this.matrix[toRemove.getX()][toRemove.getY()] = 0;
        this.snake.remove(0);
    }

}