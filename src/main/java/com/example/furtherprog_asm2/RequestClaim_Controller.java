package com.example.furtherprog_asm2;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class RequestClaim_Controller {
    @FXML
    private TextField rid;
    @FXML
    private TextField id;
    @FXML
    private TextArea note;
    @FXML
    private Button requestButton;

    @FXML
    private void processRequest() {
        String ridText = rid.getText();

        // Check if the rid matches the format of r-10 numbers
        if (!ridText.matches("r-\\d{10}")) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Invalid rid. It must start with 'r-' followed by 10 digits.");
            alert.showAndWait();

            // Clear the user input
            rid.clear();
            id.clear();
            note.clear();

            return;
        }

        // Create a new Request object from the user input
        Request request = new Request(
                ridText,
                id.getText(),
                note.getText()
        );

        // Use the RequestDAO to store the new Request in the database
        RequestDAO requestDAO = new RequestDAO();
        boolean success = requestDAO.create(request);

        // Clear the user input
        rid.clear();
        id.clear();
        note.clear();

        // Show a confirmation message
        Alert alert = new Alert(success ? Alert.AlertType.INFORMATION : Alert.AlertType.ERROR);
        alert.setTitle(success ? "Success" : "Error");
        alert.setHeaderText(null);
        alert.setContentText(success ? "Request successfully" : "Request failed");
        alert.showAndWait();

        // If the request was successfully stored, render back to propose-search-claim-navbar.fxml
        if (success) {
            try {
                // Load the FXML file for the new scene
                Parent newSceneParent = FXMLLoader.load(getClass().getResource("Propose-Search-Claim-Navbar.fxml"));

                // Create a new scene
                Scene newScene = new Scene(newSceneParent);

                // Get the current stage
                Stage currentStage = (Stage) requestButton.getScene().getWindow();

                // Set the new scene on the current stage
                currentStage.setScene(newScene);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
