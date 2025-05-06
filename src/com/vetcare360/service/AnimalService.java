package com.vetcare360.service;

import com.vetcare360.dao.AnimalDAO;
import com.vetcare360.dao.VisitDAO;
import com.vetcare360.model.Animal;
import com.vetcare360.model.Visit;

import java.sql.SQLException;
import java.util.List;

public class AnimalService {
    private AnimalDAO animalDAO;
    private VisitDAO visitDAO;

    public AnimalService() {
        this.animalDAO = new AnimalDAO();
        this.visitDAO = new VisitDAO();
    }

    public Animal saveAnimal(Animal animal) throws SQLException {
        if (animal.getId() != 0) {
            animalDAO.update(animal);
            return animal;
        } else {
            return animalDAO.insert(animal);
        }
    }

    public boolean deleteAnimal(int id) throws SQLException {
        return animalDAO.delete(id);
    }

    public Animal getAnimalById(int id) throws SQLException {
        Animal animal = animalDAO.findById(id);
        if (animal != null) {
            // Charger les visites associées à cet animal
            List<Visit> visits = visitDAO.findByAnimal(animal);
            animal.setVisits(visits);
        }
        return animal;
    }

    public List<Animal> getAnimalsByOwner(int ownerId) throws SQLException {
        List<Animal> animals = animalDAO.findByOwner(ownerId);

        // Charger les visites pour chaque animal
        for (Animal animal : animals) {
            List<Visit> visits = visitDAO.findByAnimal(animal);
            animal.setVisits(visits);
        }

        return animals;
    }

    public List<Animal> getAllAnimals() throws SQLException {
        List<Animal> animals = animalDAO.findAll();

        // Charger les visites pour chaque animal
        for (Animal animal : animals) {
            List<Visit> visits = visitDAO.findByAnimal(animal);
            animal.setVisits(visits);
        }

        return animals;
    }

    public Animal addAnimal(Animal animal) throws SQLException {
        return animalDAO.insert(animal);
    }

    public void updateAnimal(Animal animal) throws SQLException {
        animalDAO.update(animal);
    }
}