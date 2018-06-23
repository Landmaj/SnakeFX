package Fnake;

import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        FnakeBoard board = new FnakeBoard(40, 30);
        board.start(new Stage());
    }
}
