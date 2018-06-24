package Fnake;

import javafx.geometry.HPos;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;

import java.util.List;

class HighScoreDialog {
    static void createDialog() {
        Dialog dialog = new Dialog();
        dialog.setTitle("High Scores");

        int fontSize = 15;
        Label place = new Label("Place");
        place.setFont(Font.font(fontSize));
        Label nameHeader = new Label("Name");
        nameHeader.setFont(Font.font(fontSize));
        Label scoreHeader = new Label("Food");
        scoreHeader.setFont(Font.font(fontSize));
        Label secondsHeader = new Label("Time");
        secondsHeader.setFont(Font.font(fontSize));
        Label wallCollisionHeader = new Label("Wall collision");
        wallCollisionHeader.setFont(Font.font(fontSize));
        Label finalScoreHeader = new Label("Score");
        finalScoreHeader.setFont(Font.font(fontSize));


        GridPane grid = new GridPane();
        grid.setHgap(20);

        for (int i = 0; i <= 6; i++) {
            ColumnConstraints column = new ColumnConstraints();
            column.setHalignment(HPos.CENTER);
            grid.getColumnConstraints().add(column);
        }

        grid.add(place, 1, 1);
        grid.add(nameHeader, 2, 1);
        grid.add(scoreHeader, 3, 1);
        grid.add(secondsHeader, 4, 1);
        grid.add(wallCollisionHeader, 5, 1);
        grid.add(finalScoreHeader, 6, 1);
        grid.setVgap(10);
        dialog.getDialogPane().setContent(grid);

        Main.scores.sortScores();

        List<Score> scores = Main.scores.getScores();
        for (int i = 0; i < scores.size() || i == 9; i++) {
            Score score = scores.get(i);
            Label playerPlace = new Label(String.valueOf(i+1));
            grid.add(playerPlace, 1, i+2);
            Label name = new Label(score.getName() + "");
            grid.add(name, 2, i + 2);
            Label playerScore = new Label(String.valueOf(score.getScore()));
            grid.add(playerScore, 3, i + 2);
            Label seconds = new Label(String.valueOf(score.getSeconds()));
            grid.add(seconds, 4, i + 2);
            Label wallCollision;
            wallCollision = score.isWallCollision() ? new Label("ON") : new Label("OFF");
            grid.add(wallCollision, 5, i + 2);
            Label finalScore = new Label(String.valueOf(score.getFinalScore()));
            grid.add(finalScore, 6, i + 2);
        }

        ButtonType buttonTypeOk = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().add(buttonTypeOk);

        dialog.showAndWait();
    }
}
