package Fnake;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.VPos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class FnakeBoard extends Application {
    private int tileSize = 10;

    private Score player;

    private int width;
    private int height;
    private int[][] matrix;

    private List<Tuple> snake = new ArrayList<>();
    private List<Tuple> empties = new ArrayList<>();

    private Tuple food;
    private int score = 0;

    private final int BOARD = 0;
    private final int SNAKE = 1;
    private final int FOOD = 2;
    private final int BONUS = 3;
    private final int COLLISION = 4;

    private final int NORTH = 0;
    private final int SOUTH = 1;
    private final int WEST = 2;
    private final int EAST = 3;

    private int speed;
    private int direction = NORTH;
    private boolean pause = true;
    private boolean infinite;
    private long epoch;


    FnakeBoard(int columns, int rows, int speed, String name, boolean infinite) {
        this.width = columns * this.tileSize;
        this.height = rows * this.tileSize + 40;
        this.speed = speed;
        this.matrix = new int[columns][rows];
        this.snake.add(new Tuple(columns / 2, rows - 1));
        this.matrix[columns / 2][rows - 1] = 1;
        this.infinite = !infinite;

        this.player = new Score(name, 0, 0, infinite);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("SnakeFX");
        Group root = new Group();
        Canvas canvas = new Canvas(this.width, this.height);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setTextBaseline(VPos.CENTER);
        root.getChildren().addAll(canvas);
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.show();


        scene.setOnKeyPressed(event -> {
            switch (event.getCode()) {
                case ENTER:
                    pause = false;
                case UP:
                    direction = (direction == SOUTH) ? SOUTH : NORTH;
                    break;
                case DOWN:
                    direction = (direction == NORTH) ? NORTH : SOUTH;
                    break;
                case LEFT:
                    direction = (direction == EAST) ? EAST : WEST;
                    break;
                case RIGHT:
                    direction = (direction == WEST) ? WEST : EAST;
                    break;
            }
        });

        scene.addEventFilter(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
            final KeyCombination keyComb = new KeyCodeCombination(
                    KeyCode.C,
                    KeyCombination.CONTROL_DOWN
            );

            public void handle(KeyEvent ke) {
                if (keyComb.match(ke)) {
                    if (pause) {
                        endGame(primaryStage);
                    }
                    pause = true;
                }
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

        drawBoard(gc);
        startScreen(gc);
        while (pause) {
            try {
                TimeUnit.MILLISECONDS.sleep(this.speed);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        this.epoch = Instant.now().getEpochSecond();
        while (!pause) {
            gameLoop();
            this.player.setSeconds((int) (Instant.now().getEpochSecond() - this.epoch));
            drawBoard(gc);
            try {
                TimeUnit.MILLISECONDS.sleep(this.speed);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }

        endScreen(gc);

    }


    private void startScreen(GraphicsContext gc) {
        gc.setTextAlign(TextAlignment.CENTER);
        gc.fillText(
                "Use arrow keys ←↑↓→ to turn.",
                Math.round(width / 2),
                Math.round(height / 2 - 30)
        );
        gc.fillText(
                "Press [Enter] to start",
                Math.round(width / 2),
                Math.round(height / 2)
        );
        gc.fillText(
                "or [Ctrl] + [C] to exit.",
                Math.round(width / 2),
                Math.round(height / 2 + 15)
        );
    }

    private void drawBoard(GraphicsContext gc) {
        for (int x = 0; x < this.matrix.length; x++) {
            for (int y = 0; y < this.matrix[0].length; y++) {
                switch (this.matrix[x][y]) {
                    case SNAKE:
                        gc.setFill(Color.BLACK);
                        break;
                    case FOOD:
                        gc.setFill(Color.GREEN);
                        break;
                    case BONUS:
                        gc.setFill(Color.DARKBLUE);
                        break;
                    case COLLISION:
                        gc.setFill(Color.RED);
                        break;
                    default:
                        gc.setFill(Color.WHITE);
                }
                gc.fillRect(
                        x * this.tileSize,
                        y * this.tileSize + 20,
                        this.tileSize,
                        this.tileSize
                );
            }
        }

        if (!infinite) {
            gc.setLineWidth(2);
            gc.setStroke(Color.DARKRED);
            gc.strokeRect(0, 20, width, height - 40);
        }

        gc.setFill(Color.LIGHTGRAY);
        gc.fillRect(0, 0, width, 20);
        gc.fillRect(0, height - 20, width, 20);
        gc.setFill(Color.BLACK);
        gc.setTextAlign(TextAlignment.LEFT);
        gc.fillText("Score: " + String.valueOf(this.player.getScore()), 5, height - 10);
        gc.fillText("Length: " + String.valueOf(this.snake.size()), 5, 10);
        gc.setTextAlign(TextAlignment.RIGHT);
        int minutes = this.player.getSeconds() / 60;
        int seconds = this.player.getSeconds() % 60;
        gc.fillText("Time: " + minutes + ":" + seconds, width - 5, 10);
        gc.fillText("Press [Ctrl]+[C] to exit", width - 5, height - 10);
    }

    private void endScreen(GraphicsContext gc) {
        gc.setStroke(Color.DARKRED);
        gc.setLineWidth(4);
        gc.strokeLine(0, 20, width, height - 20);
        gc.strokeLine(0, height - 20, width, 20);
    }

    private Tuple nextTile(Tuple currentTile) {
        switch (this.direction) {
            case SOUTH:
                if (currentTile.getY() < this.matrix[0].length - 1) {
                    return new Tuple(currentTile.getX(), currentTile.getY() + 1);
                } else {
                    if (infinite) {
                        return new Tuple(currentTile.getX(), 0);
                    } else {
                        pause = true;
                        return currentTile;
                    }
                }
            case EAST:
                if (currentTile.getX() < this.matrix.length - 1) {
                    return new Tuple(currentTile.getX() + 1, currentTile.getY());
                } else {
                    if (infinite) {
                        return new Tuple(0, currentTile.getY());
                    } else {
                        pause = true;
                        return currentTile;
                    }
                }
            case WEST:
                if (currentTile.getX() > 0) {
                    return new Tuple(currentTile.getX() - 1, currentTile.getY());
                } else {
                    if (infinite) {
                        return new Tuple(this.matrix.length - 1, currentTile.getY());
                    } else {
                        pause = true;
                        return currentTile;
                    }
                }
            default: // NORTH
                if (currentTile.getY() > 0) {
                    return new Tuple(currentTile.getX(), currentTile.getY() - 1);
                } else {
                    if (infinite) {
                        return new Tuple(currentTile.getX(), this.matrix[0].length - 1);
                    } else {
                        pause = true;
                        return currentTile;
                    }
                }

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

    private void getEmptyTiles() {
        this.empties.clear();
        for (int x = 0; x < this.matrix.length; x++) {
            for (int y = 0; y < this.matrix[0].length; y++) {
                if (this.matrix[x][y] == BOARD) {
                    this.empties.add(new Tuple(x, y));
                }
            }
        }
    }

    private void generateFood() {
        getEmptyTiles();
        this.food = this.empties.get((int) (Math.random() * this.empties.size()));
        this.matrix[this.food.getX()][this.food.getY()] = FOOD;

        if ((int) (Math.random() * 10) == 7) {
            Tuple bonus = this.empties.get((int) (Math.random() * this.empties.size()));
            this.matrix[bonus.getX()][bonus.getY()] = BONUS;
        }
    }

    private void checkNextTile(Tuple tuple) {
        switch (this.matrix[tuple.getX()][tuple.getY()]) {
            case SNAKE:
                this.pause = true;
                this.matrix[tuple.getX()][tuple.getY()] = 4;
                break;
            case FOOD:
                extendSnake(tuple);
                generateFood();
                this.player.addScore();
                break;
            case BONUS:
                if (this.snake.size() > 1) {
                    Tuple toRemove = this.snake.get(0);
                    this.matrix[toRemove.getX()][toRemove.getY()] = 0;
                    this.snake.remove(0);
                }
                moveSnake(tuple);
                this.player.addScore();
            default:
                moveSnake(tuple);
        }
    }

    private void gameLoop() {
        if (this.food == null) {
            generateFood();
        }
        Tuple nextTile = nextTile(this.snake.get(this.snake.size() - 1));
        checkNextTile(nextTile);
    }

    private void endGame(Stage stage) {
        stage.close();
    }


}
