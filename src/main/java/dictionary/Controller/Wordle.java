package dictionary.Controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;
import java.util.stream.Stream;
import java.util.zip.GZIPInputStream;

import javafx.animation.Animation;
import javafx.animation.FadeTransition;
import javafx.animation.Timeline;
import javafx.animation.ScaleTransition;
import javafx.animation.SequentialTransition;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.util.Duration;
import javafx.scene.control.*;

public class Wordle {

    private final String[] firstRowKeys = { "Q", "W", "E", "R", "T", "Y", "U", "I", "O", "P" };
    private final String[] secondRowKeys = { "A", "S", "D", "F", "G", "H", "J", "K", "L" };
    private final String[] thirdRowKeys = { "Z", "X", "C", "V", "B", "N", "M" };

    private final int MAX_ROW = 5;
    private final int MAX_COLUMN = 4;

    private int currentRow;
    private int currentColumn;

    public Wordle(GridPane gridPane, GridPane firstRowKeyboard,
            GridPane secondRowKeyboard, GridPane thirdRowKeyboard) {
        initGridPane(gridPane);
        initKeyboard(firstRowKeyboard, secondRowKeyboard, thirdRowKeyboard);
    }

    private void initGridPane(GridPane gridPane) {
        for (int i = 0; i <= MAX_ROW; i++) {
            for (int j = 0; j <= MAX_COLUMN; j++) {
                Label label = new Label();
                label.getStyleClass().add("default-tile");
                gridPane.add(label, j, i);
            }
        }
        currentRow = 0;
        currentColumn = 0;
    }

    private void initKeyboard(GridPane firstRowKeyboard,
            GridPane secondRowKeyboard, GridPane thirdRowKeyboard) {
        for (int i = 0; i < firstRowKeys.length; i++) {
            Label label = new Label(firstRowKeys[i]);
            label.getStyleClass().add("keyboardTile");
            firstRowKeyboard.add(label, i, 0);
        }
        for (int i = 0; i < secondRowKeys.length; i++) {
            Label label = new Label(secondRowKeys[i]);
            label.getStyleClass().add("keyboardTile");
            secondRowKeyboard.add(label, i, 1);
        }
        for (int i = 0; i < thirdRowKeys.length; i++) {
            Label label = new Label(thirdRowKeys[i]);
            label.getStyleClass().add("keyboardTile");
            thirdRowKeyboard.add(label, i, 2);
        }
    }

    private Label getLabel(GridPane gridPane, final int row, final int column) {
        for (Node child : gridPane.getChildren()) {
            if (GridPane.getRowIndex(child) == row && GridPane.getColumnIndex(child) == column) {
                return (Label) child;
            }
        }
        return null;
    }

    private void setLabelText(GridPane gridPane, int row, int column, String newText) {
        Label label = getLabel(gridPane, row, column);
        if (label != null)
            label.setText(newText.toUpperCase());
    }

    private String getLabelText(GridPane gridPane, int row, int column) {
        Label label = getLabel(gridPane, row, column);
        if (label != null) {
            return label.getText().toLowerCase();
        }
        return null;
    }

    public String getRowText(GridPane gridPane) {
        String text = new String();
        for (int i = 0; i <= MAX_COLUMN; i++) {
            text += getLabelText(gridPane, currentRow, i);
        }
        return text;
    }

    private void setLabelStyleClass(GridPane gridPane, int row, int column, String styleclass) {
        Label label = getLabel(gridPane, row, column);
        if (label != null) {
            label.getStyleClass().add(styleclass);
        }
    }

    private void clearLabelStyleClass(GridPane gridPane, int row, int column) {
        Label label = getLabel(gridPane, row, column);
        if (label != null) {
            label.getStyleClass().clear();
        }
    }

    public void onBackspacePressed(GridPane gridPane) {
        if (Objects.equals(getLabelText(gridPane, currentRow, currentColumn), "") && currentColumn > 0) {
            currentColumn--;
        }

        if (!Objects.equals(getLabelText(gridPane, currentRow, currentColumn), "")) {
            setLabelText(gridPane, currentRow, currentColumn, "");
            clearLabelStyleClass(gridPane, currentRow, currentColumn);
            setLabelStyleClass(gridPane, currentRow, currentColumn, "default-tile");
        }
    }

    public void onLetterPressed(GridPane gridPane, KeyEvent keyEvent) {
        if (Objects.equals(getLabelText(gridPane, currentRow, currentColumn), "")) {
            setLabelText(gridPane, currentRow, currentColumn, keyEvent.getText());
            Label label = getLabel(gridPane, currentRow, currentColumn);
            playScaleEffect(label);
            setLabelStyleClass(gridPane, currentRow, currentColumn, "tile-with-letter");

            if (currentColumn < MAX_COLUMN) {
                currentColumn++;
            }
        }
    }

    public boolean checkEnterPressed(GridPane gridPane) {
        if (currentColumn == MAX_COLUMN && !Objects.equals(getLabelText(gridPane, currentRow, currentColumn), "")) {
            return true;
        }
        return false;
    }

    public void onEnterPressed(GridPane gridPane, GridPane firstRowKeyboard,
            GridPane secondRowKeyboard, GridPane thirdRowKeyboard, int[] state) {
        changeRowColor(gridPane, state);
        if (currentRow < MAX_ROW) {
            System.out.println("test test");
            currentRow++;
            currentColumn = 0;
        }
    }

    private void changeRowColor(GridPane gridPane, int[] state) {
        String[] tiles = { "wrong-letter", "present-letter", "correct-letter" };
        for (int i = 0; i <= MAX_COLUMN; i++) {
            Label label = getLabel(gridPane, currentRow, currentColumn);
            setLabelStyleClass(gridPane, currentRow, i, tiles[state[i]]);
        }
    }

    private void playScaleEffect(Label label) {
        ScaleTransition firstScaleTransition = new ScaleTransition(Duration.millis(100), label);
        firstScaleTransition.fromXProperty().setValue(1);
        firstScaleTransition.toXProperty().setValue(1.1);
        firstScaleTransition.fromYProperty().setValue(1);
        firstScaleTransition.toYProperty().setValue(1.1);

        ScaleTransition secondScaleTransition = new ScaleTransition(Duration.millis(100), label);
        secondScaleTransition.fromXProperty().setValue(1.1);
        secondScaleTransition.toXProperty().setValue(1);
        secondScaleTransition.fromYProperty().setValue(1.1);
        secondScaleTransition.toYProperty().setValue(1);

        SequentialTransition sequentialTransition = new SequentialTransition(firstScaleTransition,
                secondScaleTransition);
        sequentialTransition.play();
    }
}
