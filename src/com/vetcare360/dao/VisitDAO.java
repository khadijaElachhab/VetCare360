package com.vetcare360.dao;

import com.vetcare360.model.Animal;
import com.vetcare360.model.Veterinarian;
import com.vetcare360.model.Visit;
import com.vetcare360.util.DatabaseConnection;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class VisitDAO {
    private static final String INSERT_VISIT = "INSERT INTO visits (animal_id, veterinarian_id, visit_date, visit_time, reason, diagnosis, treatment, notes) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
    private static final String UPDATE_VISIT = "UPDATE visits SET animal_id=?, veterinarian_id=?, visit_date=?, visit_time=?, reason=?, diagnosis=?, treatment=?, notes=? WHERE id=?";
    private static final String DELETE_VISIT = "DELETE FROM visits WHERE id=?";
    private static final String FIND_BY_ID = "SELECT * FROM visits WHERE id=?";
    private static final String FIND_BY_ANIMAL = "SELECT * FROM visits WHERE animal_id=?";
    private static final String FIND_ALL = "SELECT * FROM visits";

    private Connection connection;
    private AnimalDAO animalDAO;
    private VeterinarianDAO veterinarianDAO;

    public VisitDAO() {
        this.connection = DatabaseConnection.getInstance().getConnection();
        this.animalDAO = new AnimalDAO();
        this.veterinarianDAO = new VeterinarianDAO();
    }

    public Visit insert(Visit visit) throws SQLException {
        try (PreparedStatement ps = connection.prepareStatement(INSERT_VISIT, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, visit.getAnimal().getId());
            ps.setInt(2, visit.getVeterinarian().getId());
            ps.setDate(3, Date.valueOf(visit.getVisitDate()));
            ps.setTime(4, Time.valueOf(visit.getVisitTime()));
            ps.setString(5, visit.getReason());
            ps.setString(6, visit.getDiagnosis());
            ps.setString(7, visit.getTreatment());
            ps.setString(8, visit.getNotes());

            int affectedRows = ps.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Creating visit failed, no rows affected.");
            }

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    visit.setId(rs.getInt(1));
                } else {
                    throw new SQLException("Creating visit failed, no ID obtained.");
                }
            }
        }

        return visit;
    }

    public boolean update(Visit visit) throws SQLException {
        try (PreparedStatement ps = connection.prepareStatement(UPDATE_VISIT)) {
            ps.setInt(1, visit.getAnimal().getId());
            ps.setInt(2, visit.getVeterinarian().getId());
            ps.setDate(3, Date.valueOf(visit.getVisitDate()));
            ps.setTime(4, Time.valueOf(visit.getVisitTime()));
            ps.setString(5, visit.getReason());
            ps.setString(6, visit.getDiagnosis());
            ps.setString(7, visit.getTreatment());
            ps.setString(8, visit.getNotes());
            ps.setInt(9, visit.getId());

            return ps.executeUpdate() > 0;
        }
    }

    public boolean delete(int id) throws SQLException {
        try (PreparedStatement ps = connection.prepareStatement(DELETE_VISIT)) {
            ps.setInt(1, id);

            return ps.executeUpdate() > 0;
        }
    }

    public Visit findById(int id) throws SQLException {
        Visit visit = null;

        try (PreparedStatement ps = connection.prepareStatement(FIND_BY_ID)) {
            ps.setInt(1, id);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                visit = extractVisitFromResultSet(rs);
            }
        }

        return visit;
    }

    public List<Visit> findByAnimal(Animal animal) throws SQLException {
        List<Visit> visits = new ArrayList<>();

        try (PreparedStatement ps = connection.prepareStatement(FIND_BY_ANIMAL)) {
            ps.setInt(1, animal.getId());

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Visit visit = extractVisitFromResultSet(rs);
                visit.setAnimal(animal);
                visits.add(visit);
            }
        }

        return visits;
    }

    public List<Visit> findAll() throws SQLException {
        List<Visit> visits = new ArrayList<>();

        try (Statement stmt = connection.createStatement()) {
            ResultSet rs = stmt.executeQuery(FIND_ALL);

            while (rs.next()) {
                visits.add(extractVisitFromResultSet(rs));
            }
        }

        return visits;
    }

    private Visit extractVisitFromResultSet(ResultSet rs) throws SQLException {
        Visit visit = new Visit();
        visit.setId(rs.getInt("id"));

        int animalId = rs.getInt("animal_id");
        Animal animal = animalDAO.findById(animalId);
        visit.setAnimal(animal);

        int veterinarianId = rs.getInt("veterinarian_id");
        Veterinarian veterinarian = veterinarianDAO.findById(veterinarianId);
        visit.setVeterinarian(veterinarian);

        Date visitDate = rs.getDate("visit_date");
        if (visitDate != null) {
            visit.setVisitDate(visitDate.toLocalDate());
        }

        Time visitTime = rs.getTime("visit_time");
        if (visitTime != null) {
            visit.setVisitTime(visitTime.toLocalTime());
        }

        visit.setReason(rs.getString("reason"));
        visit.setDiagnosis(rs.getString("diagnosis"));
        visit.setTreatment(rs.getString("treatment"));
        visit.setNotes(rs.getString("notes"));

        return visit;
    }

    public List<Visit> findByAnimal(int animal) {
        return null;
    }
}