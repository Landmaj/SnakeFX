package Fnake;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.util.Optional;

public class Main extends Application {


    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        StackPane root = new StackPane();
        Scene scene = new Scene(root);

        Label gameTitle = new Label("SnakeFX");
        gameTitle.setFont(Font.font("Arial", 32));
        Label sNumber = new Label("s16442");
        sNumber.setFont(Font.font("Arial", 20));
        Button newGame = new Button("New Game");
        newGame.setPrefSize(200, 50);
        Button highScores = new Button("High Scores");
        highScores.setPrefSize(200, 50);
        Button exitGame = new Button("Exit");
        exitGame.setPrefSize(200, 50);

        VBox vbox = new VBox();
        vbox.setSpacing(10);
        root.getChildren().add(vbox);
        vbox.getChildren().addAll(
                gameTitle,
                sNumber,
                newGame,
                highScores,
                exitGame
        );

        vbox.setAlignment(Pos.CENTER);

        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.setTitle("SnakeFX");
        primaryStage.setHeight(400);
        primaryStage.setWidth(500);
        primaryStage.show();

        newGame.setOnAction(event -> {
            Result result = NewGameDialog.createDialog();
            if (result != null) {
                FnakeBoard board = new FnakeBoard(
                        result.gameSize.getX(),
                        result.gameSize.getY(),
                        result.speed.getSpeed(),
                        result.name
                );
                board.start(new Stage());
            }
        });

        exitGame.setOnAction(event -> System.exit(0));
    }
}
