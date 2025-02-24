package com.example.furtherprog_asm2;

import com.example.furtherprog_asm2.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.util.Optional;

public class Status_SearchClaimController {
    private ClaimService claimService;

    @FXML
    private TextField Claim_ID_Box;
    @FXML
    private Button search_button;

    private String claimIdData;
    public Status_SearchClaimController() {
        Db_function dbFunction = new Db_function();
        Connection connection = dbFunction.connect_to_db();
        this.claimService = new ClaimService(new ClaimDAO(connection));
    }
    public void search() throws IOException {
        claimIdData = Claim_ID_Box.getText();
        Optional<Claim> optionalClaim = claimService.getClaim(claimIdData);
        if (optionalClaim.isPresent()) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Switch-Status-Form.fxml"));
            Parent newSceneParent = loader.load();

            // Get the controller of the scene
            Switch_Status_Controller controller = loader.getController();
            // Pass the claim id to the controller
            controller.initializeData(claimIdData);

            // Create a new scene
            Scene newScene = new Scene(newSceneParent);

            // Get the current stage
            Stage currentStage = (Stage) search_button.getScene().getWindow();

            // Set the new scene on the current stage
            currentStage.setScene(newScene);
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("No Claim found with the provided claim id.");
            alert.showAndWait();

            // Clear the claim id input
            Claim_ID_Box.clear();
        }
    }
}
