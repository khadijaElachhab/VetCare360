package com.vetcare360.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Animal {
    private int id;
    private String name;
    private String species;
    private String breed;
    private LocalDate birthDate;
    private String gender;
    private String color;
    private double weight;
    private int owner;
    private List<Visit> visits;

    public Animal() {
        visits = new ArrayList<>();
    }

    public Animal(int id, String name, String species, String breed, LocalDate birthDate,
                  String gender, String color, double weight, Owner owner) {
        this.id = id;
        this.name = name;
        this.species = species;
        this.breed = breed;
        this.birthDate = birthDate;
        this.gender = gender;
        this.color = color;
        this.weight = weight;
        this.owner = owner.getId();
        this.visits = new ArrayList<>();
    }

    // Getters and Setters
    public int getId() {
        int id1 = id;
        return id1;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSpecies() {
        return species;
    }

    public void setSpecies(String species) {
        this.species = species;
    }

    public String getBreed() {
        return breed;
    }

    public void setBreed(String breed) {
        this.breed = breed;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public int getOwner() {
        return owner;
    }

    public void setOwner(int owner) {
        this.owner = owner;
    }

    public List<Visit> getVisits() {
        return visits;
    }

    public void setVisits(List<Visit> visits) {
        this.visits = visits;
    }

    public void addVisit(Visit visit) {
        visits.add(visit);
    }

    public void removeVisit(Visit visit) {
        visits.remove(visit);
    }

    // Calcul de l'âge de l'animal en années
    public int getAge() {
        if (birthDate == null) {
            return 0;
        }
        return birthDate.until(LocalDate.now()).getYears();
    }

    @Override
    public String toString() {
        return name + " (" + species + " - " + breed + ")";
    }

    public void setOwnerId(int id) {

    }

    public int getOwnerId() {
        return 0;
    }
}
