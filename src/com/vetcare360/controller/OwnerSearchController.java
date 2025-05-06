package com.vetcare360.controller;

import com.vetcare360.Main;
import com.vetcare360.model.Owner;
import com.vetcare360.service.OwnerService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class OwnerSearchController {

    @FXML
    private TextField txtSearch;

    @FXML
    private Button btnSearch;

    @FXML
    private Button btnNewOwner;

    @FXML
    private Button btnHome;

    @FXML
    private TableView<Owner> ownerTable;

    @FXML
    private TableColumn<Owner, String> colFirstName;

    @FXML
    private TableColumn<Owner, String> colLastName;

    @FXML
    private TableColumn<Owner, String> colAddress;

    @FXML
    private TableColumn<Owner, String> colPhone;

    @FXML
    private TableColumn<Owner, String> colEmail;

    private OwnerService ownerService;
    private ObservableList<Owner> ownerData;

    @FXML
    private void initialize() {
        ownerService = new OwnerService();
        ownerData = FXCollections.observableArrayList();

        // Configuration des colonnes du tableau
        colFirstName.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        colLastName.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        colAddress.setCellValueFactory(new PropertyValueFactory<>("address"));
        colPhone.setCellValueFactory(new PropertyValueFactory<>("phone"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));

        // Liaison du tableau aux données
        ownerTable.setItems(ownerData);

        // Configuration du double-clic pour afficher les détails du propriétaire
        ownerTable.setRowFactory(tv -> {
            TableRow<Owner> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && !row.isEmpty()) {
                    Owner owner = row.getItem();
                    showOwnerDetails(owner);
                }
            });
            return row;
        });
    }

    @FXML
    private void searchOwners() {
        String searchText = txtSearch.getText().trim();

        try {
            List<Owner> owners;

            if (searchText.isEmpty()) {
                owners = ownerService.getAllOwners();
            } else {
                owners = ownerService.searchOwnersByLastName(searchText);
            }

            ownerData.setAll(owners);
        } catch (SQLException e) {
            e.printStackTrace();
            // Gérer l'erreur (afficher un message, etc.)
        }
    }

    private void showOwnerDetails(Owner owner) {
        try {
            FXMLLoader loader = new FXMLLoader(Main.class.getResource("/fxml/OwnerDetails.fxml"));
            Parent root = loader.load();

            OwnerDetailsController controller = loader.getController();
            controller.setOwner(owner);

            Stage stage = (Stage) ownerTable.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void showNewOwnerForm() {
        try {
            FXMLLoader loader = new FXMLLoader(Main.class.getResource("/fxml/NewOwner.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) btnNewOwner.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void goToHome() {
        try {
            FXMLLoader loader = new FXMLLoader(Main.class.getResource("/fxml/Home.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) btnHome.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

