package com.vetcare360.controller;

import com.vetcare360.model.Owner;
import com.vetcare360.service.OwnerService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class ModifyOwnerController implements Initializable {

    @FXML
    private TextField firstNameField;

    @FXML
    private TextField lastNameField;

    @FXML
    private TextField emailField;

    @FXML
    private TextField phoneField;

    @FXML
    private TextField addressField;

    @FXML
    private Button saveButton;

    @FXML
    private Button cancelButton;

    private OwnerService ownerService;
    private Owner currentOwner;

    public ModifyOwnerController() {
        ownerService = new OwnerService();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Initialization code if needed
    }

    /**
     * Sets the owner to be modified and populates the form fields
     * @param owner The owner to modify
     */
    public void setOwner(Owner owner) {
        this.currentOwner = owner;

        // Populate form fields with the owner's data
        firstNameField.setText(owner.getFirstName());
        lastNameField.setText(owner.getLastName());
        emailField.setText(owner.getEmail());
        phoneField.setText(owner.getPhone());
        addressField.setText(owner.getAddress());
    }

    @FXML
    private void handleSaveButton(ActionEvent event) {
        if (validateInputs()) {
            // Update owner information
            currentOwner.setFirstName(firstNameField.getText().trim());
            currentOwner.setLastName(lastNameField.getText().trim());
            currentOwner.setEmail(emailField.getText().trim());
            currentOwner.setPhone(phoneField.getText().trim());
            currentOwner.setAddress(addressField.getText().trim());

            try {
                // Save the updated owner to the database
                ownerService.updateOwner(currentOwner);

                // Show success message
                showAlert(Alert.AlertType.INFORMATION, "Success", "Owner updated successfully");

                // Return to the owner details screen
                loadOwnerDetailsScene(currentOwner);

            } catch (Exception e) {
                showAlert(Alert.AlertType.ERROR, "Error", "Failed to update owner: " + e.getMessage());
            }
        }
    }

    @FXML
    private void handleCancelButton(ActionEvent event) {
        // Return to the owner details screen without saving changes
        loadOwnerDetailsScene(currentOwner);
    }

    private boolean validateInputs() {
        String errorMessage = "";

        if (firstNameField.getText() == null || firstNameField.getText().trim().isEmpty()) {
            errorMessage += "First name is required.\n";
        }

        if (lastNameField.getText() == null || lastNameField.getText().trim().isEmpty()) {
            errorMessage += "Last name is required.\n";
        }

        if (emailField.getText() == null || emailField.getText().trim().isEmpty()) {
            errorMessage += "Email is required.\n";
        } else if (!isValidEmail(emailField.getText().trim())) {
            errorMessage += "Invalid email format.\n";
        }

        if (phoneField.getText() == null || phoneField.getText().trim().isEmpty()) {
            errorMessage += "Phone number is required.\n";
        }

        if (addressField.getText() == null || addressField.getText().trim().isEmpty()) {
            errorMessage += "Address is required.\n";
        }

        if (errorMessage.isEmpty()) {
            return true;
        } else {
            showAlert(Alert.AlertType.ERROR, "Validation Error", errorMessage);
            return false;
        }
    }

    private boolean isValidEmail(String email) {
        // Basic email validation
        return email.matches("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}");
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void loadOwnerDetailsScene(Owner owner) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/OwnerDetails.fxml"));
            Parent root = loader.load();

            OwnerDetailsController controller = loader.getController();
            controller.setOwner(owner.getId());

            Stage stage = (Stage) cancelButton.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Navigation Error", "Error loading owner details: " + e.getMessage());
        }
    }
}