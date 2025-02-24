package com.example.furtherprog_asm2;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;

public class Create_PO_controller {
    @FXML
    private TextField Name_Form;
    @FXML
    private TextField ID_Form;

    @FXML
    private TextField Phone_Form;

    @FXML
    private TextField Mail_Form;
    @FXML
    private TextField Address_Form;

    @FXML
    private PasswordField Password_Form;
    private PolicyOwnerService policyOwnerService;

    public Create_PO_controller() {
        Db_function dbFunction = new Db_function();
        Connection connection = dbFunction.connect_to_db();
        this.policyOwnerService = new PolicyOwnerService(new PolicyOwnerDao_IMP(connection));
    }

    public void Create(){
        String Name = Name_Form.getText();
        String ID = ID_Form.getText();
        String Phone = Phone_Form.getText();
        String Mail = Mail_Form.getText();
        String Address = Address_Form.getText();
        String Password = Password_Form.getText();

        if (Name.isEmpty() || ID.isEmpty() || Phone.isEmpty() || Mail.isEmpty() || Address.isEmpty() || Password.isEmpty()) {
            showAlert("Error Dialog", "Input Error", "All fields are required");
            return;
        }
        if (!ID.matches("PO-\\d{10}")) {
            showAlert("Error Dialog", "Input Error", "ID must be in the format 'PO-10 numbers'");
            return;
        }
        // Create a new InsuranceCard object
        InsuranceCard insuranceCard = new InsuranceCard();

        // Create a new PolicyOwner object
        PolicyOwner policyOwner = new PolicyOwner(ID, Name, Phone, Mail, Address, Password, insuranceCard, new ArrayList<>(), "PolicyOwner", new HashMap<>());

        policyOwnerService.addPolicyOwner(policyOwner);
    }

    public void showAlert(String title, String header, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);

        alert.showAndWait();
    }
}