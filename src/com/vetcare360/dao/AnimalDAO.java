package com.vetcare360.dao;

import com.vetcare360.model.Animal;
import com.vetcare360.model.Owner;
import com.vetcare360.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AnimalDAO {
    private static final String INSERT_ANIMAL = "INSERT INTO animals (name, species, breed, birth_date, gender, color, weight, owner_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
    private static final String UPDATE_ANIMAL = "UPDATE animals SET name=?, species=?, breed=?, birth_date=?, gender=?, color=?, weight=?, owner_id=? WHERE id=?";
    private static final String DELETE_ANIMAL = "DELETE FROM animals WHERE id=?";
    private static final String FIND_BY_ID = "SELECT * FROM animals WHERE id=?";
    private static final String FIND_BY_OWNER = "SELECT * FROM animals WHERE owner_id=?";
    private static final String FIND_ALL = "SELECT * FROM animals";

    private Connection connection;
    private OwnerDAO ownerDAO;

    public AnimalDAO() {
        this.connection = DatabaseConnection.getInstance().getConnection();
        this.ownerDAO = new OwnerDAO();
    }

    public Animal insert(Animal animal) throws SQLException {
        try (PreparedStatement ps = connection.prepareStatement(INSERT_ANIMAL, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, animal.getName());
            ps.setString(2, animal.getSpecies());
            ps.setString(3, animal.getBreed());
            ps.setDate(4, Date.valueOf(animal.getBirthDate()));
            ps.setString(5, animal.getGender());
            ps.setString(6, animal.getColor());
            ps.setDouble(7, animal.getWeight());
            ps.setInt(8, animal.getOwner());

            int affectedRows = ps.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Creating animal failed, no rows affected.");
            }

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    animal.setId(rs.getInt(1));
                } else {
                    throw new SQLException("Creating animal failed, no ID obtained.");
                }
            }
        }

        return animal;
    }

    public boolean update(Animal animal) throws SQLException {
        try (PreparedStatement ps = connection.prepareStatement(UPDATE_ANIMAL)) {
            ps.setString(1, animal.getName());
            ps.setString(2, animal.getSpecies());
            ps.setString(3, animal.getBreed());
            ps.setDate(4, Date.valueOf(animal.getBirthDate()));
            ps.setString(5, animal.getGender());
            ps.setString(6, animal.getColor());
            ps.setDouble(7, animal.getWeight());
            ps.setInt(8, animal.getOwner());
            ps.setInt(9, animal.getId());

            return ps.executeUpdate() > 0;
        }
    }

    public boolean delete(int id) throws SQLException {
        try (PreparedStatement ps = connection.prepareStatement(DELETE_ANIMAL)) {
            ps.setInt(1, id);

            return ps.executeUpdate() > 0;
        }
    }

    public Animal findById(int id) throws SQLException {
        Animal animal = null;

        try (PreparedStatement ps = connection.prepareStatement(FIND_BY_ID)) {
            ps.setInt(1, id);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                animal = extractAnimalFromResultSet(rs);
            }
        }

        return animal;
    }

    public List<Animal> findByOwner(int owner) throws SQLException {
        List<Animal> animals = new ArrayList<>();

        try (PreparedStatement ps = connection.prepareStatement(FIND_BY_OWNER)) {
            ps.setInt(1, owner);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Animal animal = extractAnimalFromResultSet(rs);
                animal.setOwner(owner);
                animals.add(animal);
            }
        }

        return animals;
    }

    public List<Animal> findAll() throws SQLException {
        List<Animal> animals = new ArrayList<>();

        try (Statement stmt = connection.createStatement()) {
            ResultSet rs = stmt.executeQuery(FIND_ALL);

            while (rs.next()) {
                animals.add(extractAnimalFromResultSet(rs));
            }
        }

        return animals;
    }

    private Animal extractAnimalFromResultSet(ResultSet rs) throws SQLException {
        Animal animal = new Animal();
        animal.setId(rs.getInt("id"));
        animal.setName(rs.getString("name"));
        animal.setSpecies(rs.getString("species"));
        animal.setBreed(rs.getString("breed"));

        Date birthDate = rs.getDate("birth_date");
        if (birthDate != null) {
            animal.setBirthDate(birthDate.toLocalDate());
        }

        animal.setGender(rs.getString("gender"));
        animal.setColor(rs.getString("color"));
        animal.setWeight(rs.getDouble("weight"));

        int ownerId = rs.getInt("owner_id");
        Owner owner = ownerDAO.findById(ownerId);
        animal.setOwner(owner.getId());

        return animal;
    }
}