package com.vetcare360.service;

import com.vetcare360.dao.OwnerDAO;
import com.vetcare360.model.Animal;
import com.vetcare360.model.Owner;

import java.sql.SQLException;
import java.util.List;

public class OwnerService {
    private OwnerDAO ownerDAO;
    private AnimalService animalService;

    public OwnerService() {
        this.ownerDAO = new OwnerDAO();
        this.animalService = new AnimalService();
    }

    public Owner saveOwner(Owner owner) throws SQLException {
        if (owner.getId() == 0) {
            return ownerDAO.insert(owner);
        } else {
            ownerDAO.update(owner);
            return owner;
        }
    }

    public boolean deleteOwner(int id) throws SQLException {
        return ownerDAO.delete(id);
    }

    public Owner getOwnerById(int id) throws SQLException {
        Owner owner = ownerDAO.findById(id);
        if (owner != null) {
            // Charger les animaux de compagnie associés à cet owner
            List<Animal> pets = animalService.getAnimalsByOwner(owner.getId());
            owner.setPets(pets);
        }
        return owner;
    }

    public List<Owner> searchOwnersByLastName(String lastName) throws SQLException {
        List<Owner> owners = ownerDAO.findByLastName(lastName);

        // Charger les animaux de compagnie pour chaque propriétaire
        for (Owner owner : owners) {
            List<Animal> pets = animalService.getAnimalsByOwner(owner.getId());
            owner.setPets(pets);
        }

        return owners;
    }

    public List<Owner> getAllOwners() throws SQLException {
        List<Owner> owners = ownerDAO.findAll();

        // Charger les animaux de compagnie pour chaque propriétaire
        for (Owner owner : owners) {
            List<Animal> pets = animalService.getAnimalsByOwner(owner.getId());
            owner.setPets(pets);
        }

        return owners;
    }

    public void updateOwner(Owner currentOwner) {

    }
}