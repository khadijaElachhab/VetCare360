package com.vetcare360.controller;

import com.vetcare360.model.Animal;
import com.vetcare360.model.Owner;
import com.vetcare360.model.Visit;
import com.vetcare360.service.AnimalService;
import com.vetcare360.service.OwnerService;
import com.vetcare360.service.VisitService;
import com.vetcare360.util.DateUtil;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
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

public class OwnerDetailsController {

    @FXML
    private Label nameLabel;
    @FXML
    private Label addressLabel;
    @FXML
    private Label cityLabel;
    @FXML
    private Label phoneLabel;
    @FXML
    private Label emailLabel;

    @FXML
    private TableView<Animal> animalTableView;
    @FXML
    private TableColumn<Animal, String> nameColumn;
    @FXML
    private TableColumn<Animal, String> speciesColumn;
    @FXML
    private TableColumn<Animal, String> breedColumn;
    @FXML
    private TableColumn<Animal, String> birthDateColumn;

    @FXML
    private TableView<Visit> visitTableView;
    @FXML
    private TableColumn<Visit, String> visitDateColumn;
    @FXML
    private TableColumn<Visit, String> reasonColumn;
    @FXML
    private TableColumn<Visit, String> treatmentColumn;
    @FXML
    private TableColumn<Visit, String> veterinarianColumn;

    @FXML
    private Button modifyOwnerButton;
    @FXML
    private Button addAnimalButton;
    @FXML
    private Button modifyAnimalButton;
    @FXML
    private Button addVisitButton;
    @FXML
    private Button backButton;

    private Owner currentOwner;
    private Animal selectedAnimal;

    private OwnerService ownerService;
    private AnimalService animalService;
    private VisitService visitService;

    /**
     * Initializes the controller.
     */
    public void initialize() {
        // Initialize services
        ownerService = new OwnerService();
        animalService = new AnimalService();
        visitService = new VisitService();

        // Configure animal table columns
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        speciesColumn.setCellValueFactory(new PropertyValueFactory<>("species"));
        breedColumn.setCellValueFactory(new PropertyValueFactory<>("breed"));
        birthDateColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(DateUtil.format(cellData.getValue().getBirthDate())));

        // Configure visit table columns
        visitDateColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(DateUtil.format(cellData.getValue().getVisitDate())));
        reasonColumn.setCellValueFactory(new PropertyValueFactory<>("reason"));
        treatmentColumn.setCellValueFactory(new PropertyValueFactory<>("treatment"));
        veterinarianColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getVeterinarian().getName()));

        // Add change listener for animal selection
        animalTableView.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> {
                    try {
                        showAnimalDetails(newValue);
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                });

        // Disable buttons initially
        modifyAnimalButton.setDisable(true);
        addVisitButton.setDisable(true);
    }

    /**
     * Sets the owner whose details should be displayed.
     *
     * @param owner the owner to display
     */
    public void setOwner(Owner owner) throws SQLException {
        this.currentOwner = owner;

        // Fill the owner's information
        nameLabel.setText(owner.getFirstName() + " " + owner.getLastName());
        addressLabel.setText(owner.getAddress());
        cityLabel.setText(owner.getCity());
        phoneLabel.setText(owner.getPhone());
        emailLabel.setText(owner.getEmail());

        // Load the owner's animals
        loadAnimals();
    }

    /**
     * Shows details of the selected animal and its visits.
     *
     * @param animal the selected animal
     */
    private void showAnimalDetails(Animal animal) throws SQLException {
        selectedAnimal = animal;

        if (animal != null) {
            // Enable animal-related buttons
            modifyAnimalButton.setDisable(false);
            addVisitButton.setDisable(false);

            // Load the animal's visits
            loadVisits(animal);
        } else {
            // Clear visit table and disable buttons
            visitTableView.setItems(FXCollections.observableArrayList());
            modifyAnimalButton.setDisable(true);
            addVisitButton.setDisable(true);
        }
    }

    /**
     * Loads all animals owned by the current owner.
     */
    private void loadAnimals() throws SQLException {
        if (currentOwner != null) {
            List<Animal> animals = animalService.getAnimalsByOwner(currentOwner.getId());
            ObservableList<Animal> animalList = FXCollections.observableArrayList(animals);
            animalTableView.setItems(animalList);
        }
    }

    /**
     * Loads all visits for the selected animal.
     *
     * @param animal the animal whose visits should be loaded
     */
    private void loadVisits(Animal animal) throws SQLException {
        if (animal != null) {
            List<Visit> visits = visitService.getVisitsByAnimal(animal.getId());
            ObservableList<Visit> visitList = FXCollections.observableArrayList(visits);
            visitTableView.setItems(visitList);
        }
    }

    /**
     * Handles the action of clicking the "Modify Owner" button.
     */
    @FXML
    private void handleModifyOwner(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/ModifyOwner.fxml"));
            Parent root = loader.load();

            ModifyOwnerController controller = loader.getController();
            controller.setOwner(currentOwner);

            Stage stage = new Stage();
            stage.setTitle("Modify Owner");
            stage.setScene(new Scene(root));
            stage.showAndWait();

            // Refresh owner details
            Owner refreshedOwner = ownerService.getOwnerById(currentOwner.getId());
            setOwner(refreshedOwner);

        } catch (IOException e) {
            showAlert("Error", "Error loading the Modify Owner view: " + e.getMessage());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Handles the action of clicking the "Add Animal" button.
     */
    @FXML
    private void handleAddAnimal(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/AddAnimal.fxml"));
            Parent root = loader.load();

            AddAnimalController controller = loader.getController();
            controller.setOwner(currentOwner);

            Stage stage = new Stage();
            stage.setTitle("Add Animal");
            stage.setScene(new Scene(root));
            stage.showAndWait();

            // Refresh animal list
            loadAnimals();

        } catch (IOException e) {
            showAlert("Error", "Error loading the Add Animal view: " + e.getMessage());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Handles the action of clicking the "Modify Animal" button.
     */
    @FXML
    private void handleModifyAnimal(ActionEvent event) {
        if (selectedAnimal != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/ModifyAnimal.fxml"));
                Parent root = loader.load();

                ModifyAnimalController controller = loader.getController();
                controller.setAnimal(selectedAnimal);

                Stage stage = new Stage();
                stage.setTitle("Modify Animal");
                stage.setScene(new Scene(root));
                stage.showAndWait();

                // Refresh animal list
                loadAnimals();

                // Reselect the animal to refresh visit list
                for (Animal animal : animalTableView.getItems()) {
                    if (animal.getId() == selectedAnimal.getId()) {
                        animalTableView.getSelectionModel().select(animal);
                        break;
                    }
                }

            } catch (IOException e) {
                showAlert("Error", "Error loading the Modify Animal view: " + e.getMessage());
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * Handles the action of clicking the "Add Visit" button.
     */
    @FXML
    private void handleAddVisit(ActionEvent event) {
        if (selectedAnimal != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/AddVisit.fxml"));
                Parent root = loader.load();

                AddVisitController controller = loader.getController();
                controller.setAnimal(selectedAnimal);

                Stage stage = new Stage();
                stage.setTitle("Add Visit");
                stage.setScene(new Scene(root));
                stage.showAndWait();

                // Refresh visit list
                loadVisits(selectedAnimal);

            } catch (IOException e) {
                showAlert("Error", "Error loading the Add Visit view: " + e.getMessage());
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * Handles the action of clicking the "Back" button.
     */
    @FXML
    private void handleBack(ActionEvent event) {
        // Close the current window
        Stage stage = (Stage) backButton.getScene().getWindow();
        stage.close();

        try {
            // Return to owner search view
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/OwnerSearch.fxml"));
            Parent root = loader.load();

            Stage newStage = new Stage();
            newStage.setTitle("Owner Search");
            newStage.setScene(new Scene(root));
            newStage.show();

        } catch (IOException e) {
            showAlert("Error", "Error loading the Owner Search view: " + e.getMessage());
        }
    }

    /**
     * Shows an alert dialog with the specified title and message.
     *
     * @param title the title of the alert
     * @param message the message to display
     */
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void setOwner(int id) {

    }
}
