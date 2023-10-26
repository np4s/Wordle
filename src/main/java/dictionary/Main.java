package dictionary;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Objects;
import java.util.Optional;

import dictionary.Controller.WordleController;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.AnchorPane;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;

public class Main extends javafx.application.Application {
   

    /** Start Application. */
    @Override
    public void start(Stage primaryStage) throws IOException {
        primaryStage.setTitle("Wordle");
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/View/wordle.fxml"));
            Parent root = fxmlLoader.load();

            // Access the existing WordleController instance from the FXML loader
            WordleController wordleController = fxmlLoader.getController();

            Scene scene = new Scene(root);

            // Set focus on the scene
            scene.setOnKeyPressed(event -> wordleController.onKeyPressed(event));

            primaryStage.setScene(scene);
            primaryStage.setResizable(false);
            primaryStage.show();

            primaryStage.setOnCloseRequest(e -> {
                Platform.exit();
                System.exit(0);
            });
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }
}