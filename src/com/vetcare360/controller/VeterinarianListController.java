package com.vetcare360.controller;

import com.vetcare360.Main;
import com.vetcare360.model.Veterinarian;
import com.vetcare360.service.VeterinarianService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class VeterinarianListController {

    @FXML
    private TableView<Veterinarian> veterinarianTable;

    @FXML
    private TableColumn<Veterinarian, String> colFirstName;

    @FXML
    private TableColumn<Veterinarian, String> colLastName;

    @FXML
    private TableColumn<Veterinarian, String> colSpecialization;

    @FXML
    private TableColumn<Veterinarian, String> colPhone;

    @FXML
    private TableColumn<Veterinarian, String> colEmail;

    @FXML
    private Button btnHome;

    private VeterinarianService veterinarianService;
    private ObservableList<Veterinarian> veterinarianData;

    @FXML
    private void initialize() {
        veterinarianService = new VeterinarianService();
        veterinarianData = FXCollections.observableArrayList();

        // Configuration des colonnes du tableau
        colFirstName.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        colLastName.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        colSpecialization.setCellValueFactory(new PropertyValueFactory<>("specialization"));
        colPhone.setCellValueFactory(new PropertyValueFactory<>("phone"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));

        // Liaison du tableau aux données
        veterinarianTable.setItems(veterinarianData);

        // Chargement initial des données
        loadVeterinarians();
    }

    private void loadVeterinarians() {
        try {
            List<Veterinarian> veterinarians = veterinarianService.getAllVeterinarians();
            veterinarianData.setAll(veterinarians);
        } catch (SQLException e) {
            e.printStackTrace();
            // Gérer l'erreur (afficher un message, etc.)
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

