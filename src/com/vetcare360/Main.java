package com.vetcare360;

import com.vetcare360.util.DatabaseConnection;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        // Charger la vue d'accueil
        Parent root = FXMLLoader.load(getClass().getResource("/resources.fxml/Home.fxml"));

        // Configurer la scène principale
        Scene scene = new Scene(root, 800, 600);

        // Vérifier si le fichier CSS existe avant de l'ajouter
        if (getClass().getResource("/css/styles.css") != null) {
            scene.getStylesheets().add(getClass().getResource("/css/styles.css").toExternalForm());
        }

        // Configurer la fenêtre principale
        primaryStage.setTitle("VetCare 360 - Gestion de Clinique Vétérinaire");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    @Override
    public void init() throws Exception {
        super.init();
        // Initialiser la connexion à la base de données
        DatabaseConnection.getInstance().connect();
    }

    @Override
    public void stop() throws Exception {
        // Fermer la connexion à la base de données
        DatabaseConnection.getInstance().closeConnection();
        super.stop();
    }

    public static void main(String[] args) {
        launch(args);
    }
}