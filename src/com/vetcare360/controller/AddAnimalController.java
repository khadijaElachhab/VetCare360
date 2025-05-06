package com.vetcare360.controller;

import com.vetcare360.model.Animal;
import com.vetcare360.model.Owner;
import com.vetcare360.service.AnimalService;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class AddAnimalController implements Initializable {

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

    private AnimalService animalService;
    private Owner currentOwner;

    public AddAnimalController() {
        animalService = new AnimalService();
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

        // Set default value for birth date to current date
        birthDatePicker.setValue(LocalDate.now());
    }

    /**
     * Sets the owner to whom the animal will be added
     * @param owner The owner of the animal
     */
    public void setOwner(Owner owner) {
        this.currentOwner = owner;
    }

    @FXML
    private void handleSaveButton(ActionEvent event) {
        if (validateInputs()) {
            try {
                // Create new animal object
                Animal animal = new Animal();
                animal.setName(nameField.getText().trim());
                animal.setSpecies(speciesComboBox.getValue());
                animal.setBreed(breedField.getText().trim());
                animal.setBirthDate(birthDatePicker.getValue());
                animal.setGender(genderComboBox.getValue());
                animal.setColor(colorField.getText().trim());

                try {
                    double weight = Double.parseDouble(weightField.getText().trim());
                    animal.setWeight(weight);
                } catch (NumberFormatException e) {
                    animal.setWeight(0.0); // Default weight if parsing fails
                }

                // Set the owner
                animal.setOwnerId(currentOwner.getId());

                // Save the animal to the database
                animalService.addAnimal(animal);

                // Show success message
                showAlert(Alert.AlertType.INFORMATION, "Success", "Animal added successfully");

                // Return to the owner details screen
                loadOwnerDetailsScene(currentOwner);

            } catch (Exception e) {
                showAlert(Alert.AlertType.ERROR, "Error", "Failed to add animal: " + e.getMessage());
            }
        }
    }

    @FXML
    private void handleCancelButton(ActionEvent event) {
        // Return to the owner details screen without saving
        loadOwnerDetailsScene(currentOwner);
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