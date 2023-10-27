package dictionary.Controller;

import java.util.Objects;

import javafx.animation.ScaleTransition;
import javafx.animation.SequentialTransition;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.util.Duration;

public class Wordle {

    private final String[] firstRowKeys = { "Q", "W", "E", "R", "T", "Y", "U", "I", "O", "P" };
    private final String[] secondRowKeys = { "A", "S", "D", "F", "G", "H", "J", "K", "L" };
    private final String[] thirdRowKeys = { "Z", "X", "C", "V", "B", "N", "M" };

    private final String[] tiles = { "wrong-letter", "present-letter", "correct-letter" };
    private final String[] keyboards = { "wrong-keyboard", "present-keyboard", "correct-keyboard" };

    private final int MAX_ROW = 5;
    private final int MAX_COLUMN = 4;

    private int currentRow;
    private int currentColumn;

    public Wordle(GridPane gridPane, GridPane firstRowKeyboard,
            GridPane secondRowKeyboard, GridPane thirdRowKeyboard) {
        initGridPane(gridPane);
        initKeyboard(firstRowKeyboard, secondRowKeyboard, thirdRowKeyboard);
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

    public void reset(GridPane gridPane, GridPane firstRowKeyboard,
            GridPane secondRowKeyboard, GridPane thirdRowKeyboard) {

        Label label;
        for (Node child : gridPane.getChildren())
            if (child instanceof Label) {
                label = (Label) child;
                label.getStyleClass().clear();
                label.setText("");
                label.getStyleClass().add("default-tile");
            }

        for (Node child : firstRowKeyboard.getChildren())
            if (child instanceof Label) {
                label = (Label) child;
                label.getStyleClass().clear();
                label.getStyleClass().add("keyboardTile");
            }
        for (Node child : secondRowKeyboard.getChildren())
            if (child instanceof Label) {
                label = (Label) child;
                label.getStyleClass().clear();
                label.getStyleClass().add("keyboardTile");
            }
        for (Node child : thirdRowKeyboard.getChildren())
            if (child instanceof Label) {
                label = (Label) child;
                label.getStyleClass().clear();
                label.getStyleClass().add("keyboardTile");
            }

        currentColumn = 0;
        currentRow = 0;
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

    public boolean endOfBoard() {
        return currentColumn == MAX_COLUMN && currentRow == MAX_ROW;
    }

    private boolean contains(String[] keyboard, String letter) {
        for (String keyChar : keyboard) {
            if (keyChar.equalsIgnoreCase(letter)) {
                return true;
            }
        }
        return false;
    }

    private Label getLabel(GridPane gridPane, final int row, final int column) {
        for (Node child : gridPane.getChildren()) {
            if (GridPane.getRowIndex(child) == row && GridPane.getColumnIndex(child) == column) {
                return (Label) child;
            }
        }
        return null;
    }

    private Label getLabel(GridPane gridPane, String letter) {
        for (Node child : gridPane.getChildren()) {
            if (child instanceof Label) {
                Label label = (Label) child;
                if (letter.equalsIgnoreCase(label.getText())) {
                    return label;
                }
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
            GridPane secondRowKeyboard, GridPane thirdRowKeyboard, int[] state, String guessWord) {
        changeRowColor(gridPane, state);
        for (int i = 0; i <= MAX_COLUMN; i++) {
            changeKeyboarColor(firstRowKeyboard, secondRowKeyboard, thirdRowKeyboard,
                    String.valueOf(guessWord.charAt(i)), state[i]);
        }

        if (currentRow < MAX_ROW) {
            currentRow++;
            currentColumn = 0;
        }
    }

    private void changeRowColor(GridPane gridPane, int[] state) {
        for (int i = 0; i <= MAX_COLUMN; i++) {
            setLabelStyleClass(gridPane, currentRow, i, tiles[state[i]]);
        }
    }

    private void changeKeyboarColor(GridPane firstRowKeyboard,
            GridPane secondRowKeyboard, GridPane thirdRowKeyboard, String letter, int newStyleId) {
        Label label;
        String newStyle = keyboards[newStyleId];

        if (contains(firstRowKeys, letter)) {
            label = getLabel(firstRowKeyboard, letter);
        } else if (contains(secondRowKeys, letter)) {
            label = getLabel(secondRowKeyboard, letter);
        } else {
            label = getLabel(thirdRowKeyboard, letter);
        }

        if (label == null) {
            return;
        }

        String currentStyle = label.getStyleClass().toString();
        if (currentStyle.equals(keyboards[2])) {
            return;
        }
        if (currentStyle.equals(keyboards[1]) && newStyle.equals(keyboards[0])) {
            return;
        }
        label.getStyleClass().clear();
        label.getStyleClass().add(newStyle);
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
