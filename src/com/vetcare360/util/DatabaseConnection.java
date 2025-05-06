package com.vetcare360.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static DatabaseConnection instance;
    private Connection connection;

    // Paramètres de connexion à la base de données
    private final String url = "jdbc:sqlite:vetcare360.db";

    private DatabaseConnection() {
        try {
            // Chargement du pilote SQLite
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            System.err.println("Erreur de chargement du driver SQLite: " + e.getMessage());
        }
    }

    public static DatabaseConnection getInstance() {
        if (instance == null) {
            instance = new DatabaseConnection();
        }
        return instance;
    }

    public Connection getConnection() {
        return connection;
    }

    public void connect() throws SQLException {
        if (connection == null || connection.isClosed()) {
            try {
                connection = DriverManager.getConnection(url);
                initializeDatabase();
            } catch (SQLException e) {
                System.err.println("Erreur de connexion à la base de données: " + e.getMessage());
                throw e;
            }
        }
    }

    public void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                System.err.println("Erreur lors de la fermeture de la connexion: " + e.getMessage());
            }
        }
    }

    // Initialisation de la base de données avec les tables nécessaires si elles n'existent pas
    private void initializeDatabase() throws SQLException {
        try (var statement = connection.createStatement()) {
            // Création de la table owners
            statement.execute("CREATE TABLE IF NOT EXISTS owners (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "first_name TEXT NOT NULL," +
                    "last_name TEXT NOT NULL," +
                    "address TEXT," +
                    "city TEXT," +
                    "postal_code TEXT," +
                    "phone TEXT," +
                    "email TEXT)");

            // Création de la table animals
            statement.execute("CREATE TABLE IF NOT EXISTS animals (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "name TEXT NOT NULL," +
                    "species TEXT NOT NULL," +
                    "breed TEXT," +
                    "birth_date DATE," +
                    "gender TEXT," +
                    "color TEXT," +
                    "weight REAL," +
                    "owner_id INTEGER," +
                    "FOREIGN KEY (owner_id) REFERENCES owners(id) ON DELETE CASCADE)");

            // Création de la table veterinarians
            statement.execute("CREATE TABLE IF NOT EXISTS veterinarians (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "first_name TEXT NOT NULL," +
                    "last_name TEXT NOT NULL," +
                    "specialization TEXT," +
                    "phone TEXT," +
                    "email TEXT)");

            // Création de la table visits
            statement.execute("CREATE TABLE IF NOT EXISTS visits (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "animal_id INTEGER NOT NULL," +
                    "veterinarian_id INTEGER NOT NULL," +
                    "visit_date DATE NOT NULL," +
                    "visit_time TIME," +
                    "reason TEXT," +
                    "diagnosis TEXT," +
                    "treatment TEXT," +
                    "notes TEXT," +
                    "FOREIGN KEY (animal_id) REFERENCES animals(id) ON DELETE CASCADE," +
                    "FOREIGN KEY (veterinarian_id) REFERENCES veterinarians(id) ON DELETE CASCADE)");
        }
    }
}
