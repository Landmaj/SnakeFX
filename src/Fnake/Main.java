package Fnake;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.*;


public class Main extends Application {

    static HighScore scores;
    private String saveName = "highScore.save";

    public static void main(String[] args) {
        launch(args);
    }

    private HighScore loadHighScores() {
        File f = new File(saveName);
        if (f.exists() && !f.isDirectory()) {
            FileInputStream fins;
            try {
                fins = new FileInputStream(saveName);
                ObjectInputStream oos = new ObjectInputStream(fins);
                return (HighScore) oos.readObject();
            } catch (IOException | ClassNotFoundException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("High scores could not be loaded.");
                alert.setContentText(e.toString());

                alert.showAndWait();
                return new HighScore();
            }
        } else {
            return new HighScore();
        }
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

        scores = loadHighScores();

        newGame.setOnAction(event -> {
            Result result = NewGameDialog.createDialog();
            if (result != null) {
                FnakeBoard board = new FnakeBoard(
                        result.gameSize,
                        result.speed.getSpeed(),
                        result.name,
                        result.infinite
                );
                board.start(new Stage());
            }
        });

        highScores.setOnAction(event -> {
            HighScoreDialog.createDialog();
        });

        exitGame.setOnAction(event -> {

            FileOutputStream fout;
            try {
                fout = new FileOutputStream(saveName);
                ObjectOutputStream oos = new ObjectOutputStream(fout);
                oos.writeObject(scores);
            } catch (IOException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("High scores could not be saved.");
                alert.setContentText(e.toString());

                alert.showAndWait();
            }
            System.exit(0);
        });
    }
}
