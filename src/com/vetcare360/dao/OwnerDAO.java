package com.vetcare360.dao;

import com.vetcare360.model.Owner;
import com.vetcare360.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OwnerDAO {
    private static final String INSERT_OWNER = "INSERT INTO owners (first_name, last_name, address, city, postal_code, phone, email) VALUES (?, ?, ?, ?, ?, ?, ?)";
    private static final String UPDATE_OWNER = "UPDATE owners SET first_name=?, last_name=?, address=?, city=?, postal_code=?, phone=?, email=? WHERE id=?";
    private static final String DELETE_OWNER = "DELETE FROM owners WHERE id=?";
    private static final String FIND_BY_ID = "SELECT * FROM owners WHERE id=?";
    private static final String FIND_BY_LASTNAME = "SELECT * FROM owners WHERE last_name LIKE ?";
    private static final String FIND_ALL = "SELECT * FROM owners";

    private Connection connection;

    public OwnerDAO() {
        this.connection = DatabaseConnection.getInstance().getConnection();
    }

    public Owner insert(Owner owner) throws SQLException {
        try (PreparedStatement ps = connection.prepareStatement(INSERT_OWNER, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, owner.getFirstName());
            ps.setString(2, owner.getLastName());
            ps.setString(3, owner.getAddress());
            ps.setString(4, owner.getCity());
            ps.setString(5, owner.getPostalCode());
            ps.setString(6, owner.getPhone());
            ps.setString(7, owner.getEmail());

            int affectedRows = ps.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Creating owner failed, no rows affected.");
            }

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    owner.setId(rs.getInt(1));
                } else {
                    throw new SQLException("Creating owner failed, no ID obtained.");
                }
            }
        }

        return owner;
    }

    public boolean update(Owner owner) throws SQLException {
        try (PreparedStatement ps = connection.prepareStatement(UPDATE_OWNER)) {
            ps.setString(1, owner.getFirstName());
            ps.setString(2, owner.getLastName());
            ps.setString(3, owner.getAddress());
            ps.setString(4, owner.getCity());
            ps.setString(5, owner.getPostalCode());
            ps.setString(6, owner.getPhone());
            ps.setString(7, owner.getEmail());
            ps.setInt(8, owner.getId());

            return ps.executeUpdate() > 0;
        }
    }

    public boolean delete(int id) throws SQLException {
        try (PreparedStatement ps = connection.prepareStatement(DELETE_OWNER)) {
            ps.setInt(1, id);

            return ps.executeUpdate() > 0;
        }
    }

    public Owner findById(int id) throws SQLException {
        Owner owner = null;

        try (PreparedStatement ps = connection.prepareStatement(FIND_BY_ID)) {
            ps.setInt(1, id);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                owner = extractOwnerFromResultSet(rs);
            }
        }

        return owner;
    }

    public List<Owner> findByLastName(String lastName) throws SQLException {
        List<Owner> owners = new ArrayList<>();

        try (PreparedStatement ps = connection.prepareStatement(FIND_BY_LASTNAME)) {
            ps.setString(1, "%" + lastName + "%");

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                owners.add(extractOwnerFromResultSet(rs));
            }
        }

        return owners;
    }

    public List<Owner> findAll() throws SQLException {
        List<Owner> owners = new ArrayList<>();

        try (Statement stmt = connection.createStatement()) {
            ResultSet rs = stmt.executeQuery(FIND_ALL);

            while (rs.next()) {
                owners.add(extractOwnerFromResultSet(rs));
            }
        }

        return owners;
    }

    private Owner extractOwnerFromResultSet(ResultSet rs) throws SQLException {
        Owner owner = new Owner();
        owner.setId(rs.getInt("id"));
        owner.setFirstName(rs.getString("first_name"));
        owner.setLastName(rs.getString("last_name"));
        owner.setAddress(rs.getString("address"));
        owner.setCity(rs.getString("city"));
        owner.setPostalCode(rs.getString("postal_code"));
        owner.setPhone(rs.getString("phone"));
        owner.setEmail(rs.getString("email"));

        return owner;
    }
}

