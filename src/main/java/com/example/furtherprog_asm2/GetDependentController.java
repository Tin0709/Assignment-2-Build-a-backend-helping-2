package com.example.furtherprog_asm2;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.Connection;
import java.util.List;
import java.util.Optional;

public class GetDependentController {

    private DependentService dependentService;

    public GetDependentController() {
        Db_function dbFunction = new Db_function();
        Connection connection = dbFunction.connect_to_db();
        this.dependentService = new DependentService(new DependentDAO_IMP(connection));
    }

    @FXML
    private TableView<Dependent> tableView;
    @FXML
    private TableColumn<Dependent, String> idColumn;
    @FXML
    private TableColumn<Dependent, String> nameColumn;
    @FXML
    private TableColumn<Dependent, String> phoneColumn;
    @FXML
    private TableColumn<Dependent, String> mailColumn;
    @FXML
    private TableColumn<Dependent, String> addressColumn;
    @FXML
    private TableColumn<Dependent, String> passColumn;

    @FXML
    private TextField ID_BOX;

    @FXML
    public void initialize() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        phoneColumn.setCellValueFactory(new PropertyValueFactory<>("phone"));
        mailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        addressColumn.setCellValueFactory(new PropertyValueFactory<>("address"));
        passColumn.setCellValueFactory(new PropertyValueFactory<>("password"));
    }

    public void handleSearchButtonAction() {
        String id = ID_BOX.getText();
        Dependent dependent = this.dependentService.getOneDependent(id);
        if (dependent != null) {
            displayDependent(dependent);
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Search Result");
            alert.setHeaderText(null);
            alert.setContentText("Dependent not found");
            alert.showAndWait();
        }
    }

    public void displayDependent(Dependent dependent) {
        ObservableList<Dependent> data = FXCollections.observableArrayList();
        if (dependent != null) {
            data.add(dependent);
        }
        tableView.setItems(data);
    }

    public void Viewall() {
        List<Dependent> dependents = dependentService.getAllDependents();
        ObservableList<Dependent> data = FXCollections.observableArrayList(dependents);
        tableView.setItems(data);
    }
}