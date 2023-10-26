package dictionary.Controller;

import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;

import dictionary.Model.WordleDict;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.control.*;

public class WordleController implements Initializable {

    @FXML
    public GridPane guessBoard;
    @FXML
    public GridPane firstRowKeyboard;
    @FXML
    public GridPane secondRowKeyboard;
    @FXML
    public GridPane thirdRowKeyboard;

    Wordle wordle;
    WordleDict wordleDict;

    private static final int MAX_ROW = 5;
    private static final int MAX_COLUMN = 4;

    public void initialize(URL url, ResourceBundle resourceBundle) {
        wordle = new Wordle(guessBoard, firstRowKeyboard, secondRowKeyboard, thirdRowKeyboard);
        wordleDict = new WordleDict();
        guessBoard.setOnKeyPressed(e -> onKeyPressed(e));
        guessBoard.requestFocus();
    }

    @FXML
    public void onKeyPressed(KeyEvent keyEvent) {
        // System.out.println("called");
        if (keyEvent.getCode() == KeyCode.BACK_SPACE) {
            wordle.onBackspacePressed(guessBoard);
        } else if (keyEvent.getCode().isLetterKey()) {
            wordle.onLetterPressed(guessBoard, keyEvent);
        } else if (keyEvent.getCode() == KeyCode.ENTER && wordle.checkEnterPressed(guessBoard)) {
            String guessWord = wordle.getRowText(guessBoard);
            if (wordleDict.isValid(guessWord)) {
                wordle.onEnterPressed(guessBoard, firstRowKeyboard, secondRowKeyboard,
                        thirdRowKeyboard, getState(guessWord), guessWord);
            }
        }
    }

    private int[] getState(String guessWord) {
        int[] state = { 0, 0, 0, 0, 0 };
        String targetWord = wordleDict.getTargetWord();

        for (int i = 0; i <= MAX_COLUMN; i++) {
            if (guessWord.charAt(i) == targetWord.charAt(i)) {
                state[i] = 2;
            }
        }
        for (int i = 0; i <= MAX_COLUMN; i++) {
            for (int j = 0; j <= MAX_COLUMN; j++) {
                if (state[j] != 2 && guessWord.charAt(i) == targetWord.charAt(j)) {
                    state[i] = 1;
                }
            }
        }
        return state;
    }
}
