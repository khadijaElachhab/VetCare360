package com.vetcare360.controller;

import com.vetcare360.Main;
import com.vetcare360.model.Owner;
import com.vetcare360.service.OwnerService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;

public class NewOwnerController {

    @FXML
    private TextField txtFirstName;

    @FXML
    private TextField txtLastName;

    @FXML
    private TextField txtAddress;

    @FXML
    private TextField txtCity;

    @FXML
    private TextField txtPostalCode;

    @FXML
    private TextField txtPhone;

    @FXML
    private TextField txtEmail;

    @FXML
    private Button btnSave;

    @FXML
    private Button btnCancel;

    private OwnerService ownerService;

    @FXML
    private void initialize() {
        ownerService = new OwnerService();
    }

    @FXML
    private void saveOwner() {
        // Validation des champs
        if (txtFirstName.getText().trim().isEmpty() || txtLastName.getText().trim().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Erreur de validation",
                    "Veuillez remplir tous les champs obligatoires (prénom et nom).");
            return;
        }

        try {
            // Création d'un nouvel owner
            Owner owner = new Owner();
            owner.setFirstName(txtFirstName.getText().trim());
            owner.setLastName(txtLastName.getText().trim());
            owner.setAddress(txtAddress.getText().trim());
            owner.setCity(txtCity.getText().trim());
            owner.setPostalCode(txtPostalCode.getText().trim());
            owner.setPhone(txtPhone.getText().trim());
            owner.setEmail(txtEmail.getText().trim());

            // Sauvegarde dans la base de données
            owner = ownerService.saveOwner(owner);

            // Afficher les détails du propriétaire nouvellement créé
            showOwnerDetails(owner);

        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur de base de données",
                    "Une erreur s'est produite lors de l'enregistrement du propriétaire: " + e.getMessage());
        }
    }

    @FXML
    private void cancel() {
        goToOwnerSearch();
    }

    private void showOwnerDetails(Owner owner) {
        try {
            FXMLLoader loader = new FXMLLoader(Main.class.getResource("/fxml/OwnerDetails.fxml"));
            Parent root = loader.load();

            OwnerDetailsController controller = loader.getController();
            controller.setOwner(owner.getId());

            Stage stage = (Stage) btnSave.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void goToOwnerSearch() {
        try {
            FXMLLoader loader = new FXMLLoader(Main.class.getResource("/fxml/OwnerSearch.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) btnCancel.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
