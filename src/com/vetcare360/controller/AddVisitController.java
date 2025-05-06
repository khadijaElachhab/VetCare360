package com.vetcare360.controller;

import com.vetcare360.model.Animal;
import com.vetcare360.model.Visit;
import com.vetcare360.model.Veterinarian;
import com.vetcare360.service.AnimalService;
import com.vetcare360.service.VeterinarianService;
import com.vetcare360.service.VisitService;
import com.vetcare360.util.DateUtil;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;

public class AddVisitController implements Initializable {

    @FXML
    private Label animalNameLabel;

    @FXML
    private Label animalSpeciesLabel;

    @FXML
    private Label ownerNameLabel;

    @FXML
    private DatePicker visitDatePicker;

    @FXML
    private TextArea reasonTextArea;

    @FXML
    private TextArea diagnosisTextArea;

    @FXML
    private TextArea treatmentTextArea;

    @FXML
    private ComboBox<Veterinarian> veterinarianComboBox;

    @FXML
    private Button saveVisitButton;

    @FXML
    private Button cancelButton;

    @FXML
    private TableView<Visit> previousVisitsTableView;

    @FXML
    private TableColumn<Visit, LocalDate> dateColumn;

    @FXML
    private TableColumn<Visit, String> reasonColumn;

    @FXML
    private TableColumn<Visit, String> diagnosisColumn;

    @FXML
    private TableColumn<Visit, String> treatmentColumn;

    @FXML
    private TableColumn<Visit, String> veterinarianColumn;

    private Animal currentAnimal;
    private AnimalService animalService;
    private VisitService visitService;
    private VeterinarianService veterinarianService;

    public AddVisitController() {
        animalService = new AnimalService();
        visitService = new VisitService();
        veterinarianService = new VeterinarianService();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Initialize date picker with current date
        visitDatePicker.setValue(LocalDate.now());

        // Configure table columns
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("visitDate"));
        reasonColumn.setCellValueFactory(new PropertyValueFactory<>("reason"));
        diagnosisColumn.setCellValueFactory(new PropertyValueFactory<>("diagnosis"));
        treatmentColumn.setCellValueFactory(new PropertyValueFactory<>("treatment"));
        veterinarianColumn.setCellValueFactory(cellData ->
                cellData.getValue().getVeterinarian().nameProperty());

        // Load veterinarians for combobox
        loadVeterinarians();

        // Set up buttons
        saveVisitButton.setOnAction(this::handleSaveVisit);
        cancelButton.setOnAction(this::handleCancel);
    }

    public void setAnimal(Animal animal) {
        this.currentAnimal = animal;

        // Update labels with animal and owner information
        animalNameLabel.setText(animal.getName());
        animalSpeciesLabel.setText(animal.getSpecies());
        ownerNameLabel.setText(String.valueOf(animal.getOwner()));

        // Load previous visits for this animal
        loadPreviousVisits();
    }

    private void loadVeterinarians() {
        try {
            List<Veterinarian> veterinarians = veterinarianService.getAllVeterinarians();
            veterinarianComboBox.setItems(FXCollections.observableArrayList(veterinarians));

            // Select first veterinarian by default if list is not empty
            if (!veterinarians.isEmpty()) {
                veterinarianComboBox.setValue(veterinarians.get(0));
            }
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to load veterinarians", e.getMessage());
        }
    }

    private void loadPreviousVisits() {
        try {
            List<Visit> visits = visitService.getVisitsByAnimal(currentAnimal.getId());
            previousVisitsTableView.setItems(FXCollections.observableArrayList(visits));
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to load previous visits", e.getMessage());
        }
    }

    @FXML
    private void handleSaveVisit(ActionEvent event) {
        if (validateInputs()) {
            try {
                Visit newVisit = new Visit();
                newVisit.setAnimal(currentAnimal);
                newVisit.setVisitDate(visitDatePicker.getValue());
                newVisit.setReason(reasonTextArea.getText().trim());
                newVisit.setDiagnosis(diagnosisTextArea.getText().trim());
                newVisit.setTreatment(treatmentTextArea.getText().trim());
                newVisit.setVeterinarian(veterinarianComboBox.getValue());

                // Save the visit
                visitService.saveVisit(newVisit);

                // Show success message
                showAlert(Alert.AlertType.INFORMATION, "Success", "Visit Added",
                        "The visit has been successfully added.");

                // Refresh the previous visits table
                loadPreviousVisits();

                // Clear form for potential new entry
                clearForm();

                // Navigate to owner details page
                navigateToOwnerDetails();

            } catch (Exception e) {
                showAlert(Alert.AlertType.ERROR, "Error", "Failed to save visit", e.getMessage());
            }
        }
    }

    private boolean validateInputs() {
        StringBuilder errorMessage = new StringBuilder();

        if (visitDatePicker.getValue() == null) {
            errorMessage.append("- Please select a visit date.\n");
        }

        if (reasonTextArea.getText().trim().isEmpty()) {
            errorMessage.append("- Please enter a reason for the visit.\n");
        }

        if (veterinarianComboBox.getValue() == null) {
            errorMessage.append("- Please select a veterinarian.\n");
        }

        if (errorMessage.length() > 0) {
            showAlert(Alert.AlertType.WARNING, "Validation Error",
                    "Please correct the following errors:", errorMessage.toString());
            return false;
        }

        return true;
    }

    private void clearForm() {
        visitDatePicker.setValue(LocalDate.now());
        reasonTextArea.clear();
        diagnosisTextArea.clear();
        treatmentTextArea.clear();

        // Do not reset veterinarian selection as it's likely the same vet will handle multiple visits
    }

    @FXML
    private void handleCancel(ActionEvent event) {
        navigateToOwnerDetails();
    }

    private void navigateToOwnerDetails() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/OwnerDetails.fxml"));
            Parent root = loader.load();

            OwnerDetailsController controller = loader.getController();
            controller.setOwner(currentAnimal.getOwner());

            Stage stage = (Stage) cancelButton.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Navigation Error",
                    "Failed to navigate to Owner Details page", e.getMessage());
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String header, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }
}