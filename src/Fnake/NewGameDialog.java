package Fnake;

import javafx.collections.FXCollections;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;

import java.util.Optional;

class NewGameDialog {

    static Result createDialog() {
        Dialog dialog = new Dialog();
        dialog.setTitle("New Game");
        dialog.setHeaderText("Choose game settings:");

        Label nameLabel = new Label("Name:\t");
        TextField nameField = new TextField();
        nameField.setText("Anonymous");

        Label gameSizeLabel = new Label("Size:");
        ChoiceBox<GameSize> gameSize = new ChoiceBox<>(
                FXCollections.observableArrayList(
                        GameOptions.SMALL,
                        GameOptions.MEDIUM,
                        GameOptions.LARGE
                )
        );
        gameSize.getSelectionModel().select(1);

        Label gameSpeedLabel = new Label("Speed:");
        ChoiceBox<GameSpeed> gameSpeed = new ChoiceBox<>(
                FXCollections.observableArrayList(
                        GameOptions.SLOW,
                        GameOptions.NORMAL,
                        GameOptions.FAST
                )
        );
        gameSpeed.getSelectionModel().select(1);

        Label infiniteLabel = new Label("Wall collision: ");
        CheckBox infinite = new CheckBox();

        GridPane grid = new GridPane();
        grid.add(nameLabel, 1, 1);
        grid.add(nameField, 2, 1);
        grid.add(gameSizeLabel, 1, 2);
        grid.add(gameSize, 2, 2);
        grid.add(gameSpeedLabel, 1, 3);
        grid.add(gameSpeed, 2, 3);
        grid.add(infiniteLabel, 1, 4);
        grid.add(infinite, 2, 4);
        grid.setVgap(10);
        dialog.getDialogPane().setContent(grid);

        ButtonType buttonTypeOk = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().add(buttonTypeOk);

        Optional result = dialog.showAndWait();
        if (result.isPresent()) {
            return new Result(
                    nameField.getText(),
                    gameSize.getValue(),
                    gameSpeed.getValue(),
                    infinite.isSelected()
            );
        } else return null;

    }

}

class Result {
    String name;
    GameSize gameSize;
    GameSpeed speed;
    boolean infinite;

    Result(String name, GameSize gameSize, GameSpeed speed, boolean infinite) {
        this.name = name;
        this.gameSize = gameSize;
        this.speed = speed;
        this.infinite = infinite;
    }

}