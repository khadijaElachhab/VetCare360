package com.vetcare360.dao;

import com.vetcare360.model.Veterinarian;
import com.vetcare360.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class VeterinarianDAO {
    private static final String INSERT_VETERINARIAN = "INSERT INTO veterinarians (first_name, last_name, specialization, phone, email) VALUES (?, ?, ?, ?, ?)";
    private static final String UPDATE_VETERINARIAN = "UPDATE veterinarians SET first_name=?, last_name=?, specialization=?, phone=?, email=? WHERE id=?";
    private static final String DELETE_VETERINARIAN = "DELETE FROM veterinarians WHERE id=?";
    private static final String FIND_BY_ID = "SELECT * FROM veterinarians WHERE id=?";
    private static final String FIND_ALL = "SELECT * FROM veterinarians";

    private Connection connection;

    public VeterinarianDAO() {
        this.connection = DatabaseConnection.getInstance().getConnection();
    }

    public Veterinarian insert(Veterinarian veterinarian) throws SQLException {
        try (PreparedStatement ps = connection.prepareStatement(INSERT_VETERINARIAN, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, veterinarian.getFirstName());
            ps.setString(2, veterinarian.getLastName());
            ps.setString(3, veterinarian.getSpecialization());
            ps.setString(4, veterinarian.getPhone());
            ps.setString(5, veterinarian.getEmail());

            int affectedRows = ps.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Creating veterinarian failed, no rows affected.");
            }

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    veterinarian.setId(rs.getInt(1));
                } else {
                    throw new SQLException("Creating veterinarian failed, no ID obtained.");
                }
            }
        }

        return veterinarian;
    }

    public boolean update(Veterinarian veterinarian) throws SQLException {
        try (PreparedStatement ps = connection.prepareStatement(UPDATE_VETERINARIAN)) {
            ps.setString(1, veterinarian.getFirstName());
            ps.setString(2, veterinarian.getLastName());
            ps.setString(3, veterinarian.getSpecialization());
            ps.setString(4, veterinarian.getPhone());
            ps.setString(5, veterinarian.getEmail());
            ps.setInt(6, veterinarian.getId());

            return ps.executeUpdate() > 0;
        }
    }

    public boolean delete(int id) throws SQLException {
        try (PreparedStatement ps = connection.prepareStatement(DELETE_VETERINARIAN)) {
            ps.setInt(1, id);

            return ps.executeUpdate() > 0;
        }
    }

    public Veterinarian findById(int id) throws SQLException {
        Veterinarian veterinarian = null;

        try (PreparedStatement ps = connection.prepareStatement(FIND_BY_ID)) {
            ps.setInt(1, id);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                veterinarian = extractVeterinarianFromResultSet(rs);
            }
        }

        return veterinarian;
    }

    public List<Veterinarian> findAll() throws SQLException {
        List<Veterinarian> veterinarians = new ArrayList<>();

        try (Statement stmt = connection.createStatement()) {
            ResultSet rs = stmt.executeQuery(FIND_ALL);

            while (rs.next()) {
                veterinarians.add(extractVeterinarianFromResultSet(rs));
            }
        }

        return veterinarians;
    }

    private Veterinarian extractVeterinarianFromResultSet(ResultSet rs) throws SQLException {
        Veterinarian veterinarian = new Veterinarian();
        veterinarian.setId(rs.getInt("id"));
        veterinarian.setFirstName(rs.getString("first_name"));
        veterinarian.setLastName(rs.getString("last_name"));
        veterinarian.setSpecialization(rs.getString("specialization"));
        veterinarian.setPhone(rs.getString("phone"));
        veterinarian.setEmail(rs.getString("email"));

        return veterinarian;
    }
}
