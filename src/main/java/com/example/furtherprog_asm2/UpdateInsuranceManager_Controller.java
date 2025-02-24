package com.example.furtherprog_asm2;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.util.Optional;

public class UpdateInsuranceManager_Controller {
    @FXML
    private TextField idInput;
    @FXML
    private Button searchButton;
    @FXML
    private TextField idBox;
    @FXML
    private TextField nameBox;
    @FXML
    private TextField phoneBox;
    @FXML
    private TextField addressBox;
    @FXML
    private TextField emailBox;
    @FXML
    private TextField passwordBox;
    private InsuranceManager_Service insuranceManagerService;
    private Db_function dbFunction;
    private InsuranceManager originalInsuranceManager;

    public UpdateInsuranceManager_Controller() {
        Db_function dbFunction = new Db_function();
        Connection connection = dbFunction.connect_to_db();
        this.insuranceManagerService = new InsuranceManager_Service(new InsuranceManager_DAO(connection));
    }

    public void initializeData(String insuranceManagerId) {
        if (insuranceManagerId != null) {
            Optional<InsuranceManager> insuranceManagerOptional = insuranceManagerService.getInsuranceManager(insuranceManagerId);
            if (insuranceManagerOptional.isPresent()) {
                originalInsuranceManager = insuranceManagerOptional.get();

                idBox.setText(originalInsuranceManager.getId());
                idBox.setEditable(false);

                nameBox.setText(originalInsuranceManager.getName());
                nameBox.setEditable(false);

                phoneBox.setText(originalInsuranceManager.getPhone());
                addressBox.setText(originalInsuranceManager.getAddress());
                emailBox.setText(originalInsuranceManager.getEmail());
                passwordBox.setText(originalInsuranceManager.getPassword());
            } else {
                // Handle the case where the dependent is not found
                System.out.println("Dependent not found with id: " + insuranceManagerId);
            }
        } else {
            System.out.println("Dependent id is null");
        }
    }

    @FXML
    public void handleSearchInsuranceManager() {
        String insuranceManagerId = idInput.getText();
        Optional<InsuranceManager> optionalInsuranceManager = insuranceManagerService.getInsuranceManager(insuranceManagerId);
        if (optionalInsuranceManager.isPresent()) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("Update-InsuranceManager-Form.fxml"));
                Parent newSceneParent = loader.load();

                // Get the controller of the scene
                UpdateInsuranceManager_Controller controller = loader.getController();
                // Pass the insuranceSurveyor id to the controller
                controller.initializeData(insuranceManagerId);

                // Create a new scene
                Scene newScene = new Scene(newSceneParent);

                // Get the current stage
                Stage currentStage = (Stage) searchButton.getScene().getWindow();

                // Set the new scene on the current stage
                currentStage.setScene(newScene);
            } catch (IOException e) {
                e.printStackTrace();
                showAlert("Error Dialog", "Search Result", "An error occurred while searching for the Insurance Surveyor");
            }
        } else {
            showAlert("Error Dialog", "Search Result", "No Insurance Surveyor found with the provided id.");
            // Clear the insuranceSurveyor id input
            idInput.clear();
        }
    }

    @FXML
    public void handleUpdateInsuranceManager() {
        String id = idBox.getText();
        String name = nameBox.getText();
        String phone = phoneBox.getText();
        String address = addressBox.getText();
        String email = emailBox.getText();
        String password = passwordBox.getText();

        if (name.isEmpty()) {
            name = originalInsuranceManager.getName();
        }
        if (phone.isEmpty()) {
            phone = originalInsuranceManager.getPhone();
        }
        if (address.isEmpty()) {
            address = originalInsuranceManager.getAddress();
        }
        if (email.isEmpty()) {
            email = originalInsuranceManager.getEmail();
        }
        if (password.isEmpty()) {
            password = originalInsuranceManager.getPassword();
        }

        InsuranceManager newInsuranceManager = new InsuranceManager();
        newInsuranceManager.setId(id);
        newInsuranceManager.setName(name);
        newInsuranceManager.setPhone(phone);
        newInsuranceManager.setAddress(address);
        newInsuranceManager.setEmail(email);
        newInsuranceManager.setPassword(password);

        boolean updateSuccessful = insuranceManagerService.updateInsuranceManager(newInsuranceManager);
        if (updateSuccessful) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("Update-InsuranceManager-Search.fxml"));
                Parent newSceneParent = loader.load();
                Scene newScene = new Scene(newSceneParent);
                Stage currentStage = (Stage) idBox.getScene().getWindow();
                currentStage.setScene(newScene);

                showAlert("Success Dialog", "Update Result", "Update successful.");
            } catch (IOException e) {
                e.printStackTrace();
                showAlert("Error Dialog", "Update Result", "An error occurred while navigating back to the search page.");
            }
        } else {
            showAlert("Error Dialog", "Update Result", "Update failed.");
        }
    }

    public void showAlert(String title, String header, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);

        alert.showAndWait();
    }
}
