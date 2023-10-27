package dictionary.Controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;

import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class WordleResult {
    public static boolean resetGame = false;

    public void display(String result, String targetWord) {
        try {
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.initStyle(StageStyle.UTILITY);
            stage.setMaxWidth(440);
            stage.setMaxHeight(400);

            VBox root = new VBox(15);
            root.setAlignment(Pos.CENTER);

            Label mainLabel = new Label();
            mainLabel.setText("    " + result + " \n The word was");
            mainLabel.getStyleClass().setAll("lead", "big-font");

            Label targetWordLabel = new Label(targetWord.toUpperCase());
            targetWordLabel.getStyleClass().setAll("h2", "strong");

            VBox buttonsVBox = new VBox(5);
            buttonsVBox.setAlignment(Pos.CENTER);

            Button retryButton = new Button("RETRY");
            retryButton.setOnAction(event -> {
                resetGame = true;
                stage.close();
            });

            Button quitButton = new Button("QUIT");
            quitButton.setOnAction(event -> {
                resetGame = false;
                stage.close();
            });

            buttonsVBox.getChildren().addAll(retryButton, quitButton);

            root.getChildren().addAll(mainLabel, targetWordLabel, buttonsVBox);
            Scene scene = new Scene(root, 300, 260);
            stage.setScene(scene);
            stage.showAndWait();
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
