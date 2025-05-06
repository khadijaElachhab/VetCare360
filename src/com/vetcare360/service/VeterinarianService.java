package com.vetcare360.service;

import com.vetcare360.dao.VeterinarianDAO;
import com.vetcare360.model.Veterinarian;

import java.sql.SQLException;
import java.util.List;

public class VeterinarianService {
    private VeterinarianDAO veterinarianDAO;

    public VeterinarianService() {
        this.veterinarianDAO = new VeterinarianDAO();
    }

    public Veterinarian saveVeterinarian(Veterinarian veterinarian) throws SQLException {
        if (veterinarian.getId() == 0) {
            return veterinarianDAO.insert(veterinarian);
        } else {
            veterinarianDAO.update(veterinarian);
            return veterinarian;
        }
    }

    public boolean deleteVeterinarian(int id) throws SQLException {
        return veterinarianDAO.delete(id);
    }

    public Veterinarian getVeterinarianById(int id) throws SQLException {
        return veterinarianDAO.findById(id);
    }

    public List<Veterinarian> getAllVeterinarians() throws SQLException {
        return veterinarianDAO.findAll();
    }
}

