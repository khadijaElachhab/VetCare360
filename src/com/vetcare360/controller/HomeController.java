package com.vetcare360.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Contrôleur pour la page d'accueil de l'application VetCare 360.
 */
public class HomeController implements Initializable {

    @FXML
    private Button btnSearchOwner;

    @FXML
    private Button btnNewOwner;

    @FXML
    private Button btnVeterinarianList;

    /**
     * Initialise le contrôleur après que son élément racine a été complètement traité.
     *
     * @param location  L'emplacement utilisé pour résoudre les chemins relatifs des objets racine.
     * @param resources Les ressources utilisées pour localiser l'objet racine.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Initialisation supplémentaire si nécessaire
    }

    /**
     * Navigue vers la page de recherche de propriétaires.
     *
     * @param event L'événement qui a déclenché cette méthode.
     */
    @FXML
    private void navigateToOwnerSearch(ActionEvent event) {
        try {
            Parent ownerSearchRoot = FXMLLoader.load(getClass().getResource("/fxml/OwnerSearch.fxml"));
            Scene scene = new Scene(ownerSearchRoot);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("VetCare 360 - Recherche de Propriétaires");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            // Gérer l'exception appropriée, peut-être afficher une alerte
        }
    }

    /**
     * Navigue vers la page d'ajout d'un nouveau propriétaire.
     *
     * @param event L'événement qui a déclenché cette méthode.
     */
    @FXML
    private void navigateToNewOwner(ActionEvent event) {
        try {
            Parent newOwnerRoot = FXMLLoader.load(getClass().getResource("/fxml/NewOwner.fxml"));
            Scene scene = new Scene(newOwnerRoot);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("VetCare 360 - Nouveau Propriétaire");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            // Gérer l'exception appropriée
        }
    }

    /**
     * Navigue vers la page de liste des vétérinaires.
     *
     * @param event L'événement qui a déclenché cette méthode.
     */
    @FXML
    private void navigateToVeterinarianList(ActionEvent event) {
        try {
            Parent vetListRoot = FXMLLoader.load(getClass().getResource("/fxml/VeterinarianList.fxml"));
            Scene scene = new Scene(vetListRoot);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("VetCare 360 - Liste des Vétérinaires");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            // Gérer l'exception appropriée
        }
    }
}
