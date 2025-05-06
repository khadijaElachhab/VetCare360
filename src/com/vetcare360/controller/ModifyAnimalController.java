package com.vetcare360.controller;

import com.vetcare360.model.Animal;
import com.vetcare360.model.Owner;
import com.vetcare360.service.AnimalService;
import com.vetcare360.service.OwnerService;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType; // Correction 1: Ajout de l'import manquant
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class ModifyAnimalController implements Initializable {

    @FXML
    private TextField nameField;

    @FXML
    private ComboBox<String> speciesComboBox;

    @FXML
    private TextField breedField;

    @FXML
    private DatePicker birthDatePicker;

    @FXML
    private ComboBox<String> genderComboBox;

    @FXML
    private TextField colorField;

    @FXML
    private TextField weightField;

    @FXML
    private Button saveButton;

    @FXML
    private Button cancelButton;

    @FXML
    private Button deleteButton;

    private AnimalService animalService;
    private OwnerService ownerService;
    private Animal currentAnimal;
    private Owner currentOwner;

    public ModifyAnimalController() {
        animalService = new AnimalService();
        ownerService = new OwnerService();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Initialize species combo box
        speciesComboBox.setItems(FXCollections.observableArrayList(
                "Dog", "Cat", "Bird", "Rabbit", "Hamster", "Guinea Pig", "Ferret", "Reptile", "Other"
        ));

        // Initialize gender combo box
        genderComboBox.setItems(FXCollections.observableArrayList(
                "Male", "Female", "Unknown"
        ));

        // Correction 2: Ajout des gestionnaires d'événements pour les boutons
        saveButton.setOnAction(this::handleSaveButton);
        cancelButton.setOnAction(this::handleCancelButton);
        deleteButton.setOnAction(this::handleDeleteButton);
    }

    /**
     * Sets the animal to be modified and populates the form fields
     * @param animal The animal to modify
     */
    public void setAnimal(Animal animal) {
        this.currentAnimal = animal;

        try {
            // Load the owner for this animal
            this.currentOwner = ownerService.getOwnerById(animal.getOwnerId());

            // Populate form fields with the animal's data
            nameField.setText(animal.getName());
            speciesComboBox.setValue(animal.getSpecies());
            breedField.setText(animal.getBreed());
            birthDatePicker.setValue(animal.getBirthDate());
            genderComboBox.setValue(animal.getGender());
            colorField.setText(animal.getColor());
            weightField.setText(String.valueOf(animal.getWeight()));
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to load animal details: " + e.getMessage());
        }
    }

    @FXML
    private void handleSaveButton(ActionEvent event) {
        if (validateInputs()) {
            try {
                // Update animal information
                currentAnimal.setName(nameField.getText().trim());
                currentAnimal.setSpecies(speciesComboBox.getValue());
                currentAnimal.setBreed(breedField.getText().trim());
                currentAnimal.setBirthDate(birthDatePicker.getValue());
                currentAnimal.setGender(genderComboBox.getValue());
                currentAnimal.setColor(colorField.getText().trim());

                try {
                    double weight = Double.parseDouble(weightField.getText().trim());
                    currentAnimal.setWeight(weight);
                } catch (NumberFormatException e) {
                    // Keep the existing weight if parsing fails
                }

                // Save the updated animal to the database
                animalService.updateAnimal(currentAnimal);

                // Show success message
                showAlert(Alert.AlertType.INFORMATION, "Success", "Animal updated successfully");

                // Return to the owner details screen
                loadOwnerDetailsScene(currentOwner);

            } catch (Exception e) {
                showAlert(Alert.AlertType.ERROR, "Error", "Failed to update animal: " + e.getMessage());
            }
        }
    }

    @FXML
    private void handleCancelButton(ActionEvent event) {
        // Return to the owner details screen without saving changes
        loadOwnerDetailsScene(currentOwner);
    }

    @FXML
    private void handleDeleteButton(ActionEvent event) {
        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Confirm Deletion");
        confirmAlert.setHeaderText(null);
        confirmAlert.setContentText("Are you sure you want to delete this animal? This action cannot be undone.");

        confirmAlert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) { // Correction 3: Utilisation directe de ButtonType au lieu de javafx.scene.control.ButtonType
                try {
                    // Delete the animal from the database
                    animalService.deleteAnimal(currentAnimal.getId());

                    // Show success message
                    showAlert(Alert.AlertType.INFORMATION, "Success", "Animal deleted successfully");

                    // Return to the owner details screen
                    loadOwnerDetailsScene(currentOwner);

                } catch (Exception e) {
                    showAlert(Alert.AlertType.ERROR, "Error", "Failed to delete animal: " + e.getMessage());
                }
            }
        });
    }

    private boolean validateInputs() {
        String errorMessage = "";

        if (nameField.getText() == null || nameField.getText().trim().isEmpty()) {
            errorMessage += "Name is required.\n";
        }

        if (speciesComboBox.getValue() == null) {
            errorMessage += "Species is required.\n";
        }

        if (genderComboBox.getValue() == null) {
            errorMessage += "Gender is required.\n";
        }

        if (birthDatePicker.getValue() == null) {
            errorMessage += "Birth date is required.\n";
        } else if (birthDatePicker.getValue().isAfter(LocalDate.now())) {
            errorMessage += "Birth date cannot be in the future.\n";
        }

        if (weightField.getText() != null && !weightField.getText().trim().isEmpty()) {
            try {
                double weight = Double.parseDouble(weightField.getText().trim());
                if (weight <= 0) {
                    errorMessage += "Weight must be greater than zero.\n";
                }
            } catch (NumberFormatException e) {
                errorMessage += "Weight must be a valid number.\n";
            }
        }

        if (errorMessage.isEmpty()) {
            return true;
        } else {
            showAlert(Alert.AlertType.ERROR, "Validation Error", errorMessage);
            return false;
        }
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
