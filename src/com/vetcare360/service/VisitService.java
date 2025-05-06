package com.vetcare360.service;

import com.vetcare360.dao.VisitDAO;
import com.vetcare360.model.Visit;

import java.sql.SQLException;
import java.util.List;

public class VisitService {
    private VisitDAO visitDAO;

    public VisitService() {
        this.visitDAO = new VisitDAO();
    }

    public Visit saveVisit(Visit visit) throws SQLException {
        if (visit.getId() == 0) {
            return visitDAO.insert(visit);
        } else {
            visitDAO.update(visit);
            return visit;
        }
    }

    public boolean deleteVisit(int id) throws SQLException {
        return visitDAO.delete(id);
    }

    public Visit getVisitById(int id) throws SQLException {
        return visitDAO.findById(id);
    }

    public List<Visit> getVisitsByAnimal(int animal) throws SQLException {
        return visitDAO.findByAnimal(animal);
    }

    public List<Visit> getAllVisits() throws SQLException {
        return visitDAO.findAll();
    }
}
