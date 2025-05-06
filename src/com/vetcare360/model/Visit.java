package com.vetcare360.model;

import java.time.LocalDate;
import java.time.LocalTime;

public class Visit {
    private int id;
    private Animal animal;
    private Veterinarian veterinarian;
    private LocalDate visitDate;
    private LocalTime visitTime;
    private String reason;
    private String diagnosis;
    private String treatment;
    private String notes;

    public Visit() {
    }

    public Visit(int id, Animal animal, Veterinarian veterinarian, LocalDate visitDate,
                 LocalTime visitTime, String reason, String diagnosis, String treatment, String notes) {
        this.id = id;
        this.animal = animal;
        this.veterinarian = veterinarian;
        this.visitDate = visitDate;
        this.visitTime = visitTime;
        this.reason = reason;
        this.diagnosis = diagnosis;
        this.treatment = treatment;
        this.notes = notes;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Animal getAnimal() {
        return animal;
    }

    public void setAnimal(Animal animal) {
        this.animal = animal;
    }

    public Veterinarian getVeterinarian() {
        return veterinarian;
    }

    public void setVeterinarian(Veterinarian veterinarian) {
        this.veterinarian = veterinarian;
    }

    public LocalDate getVisitDate() {
        return visitDate;
    }

    public void setVisitDate(LocalDate visitDate) {
        this.visitDate = visitDate;
    }

    public LocalTime getVisitTime() {
        return visitTime;
    }

    public void setVisitTime(LocalTime visitTime) {
        this.visitTime = visitTime;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getDiagnosis() {
        return diagnosis;
    }

    public void setDiagnosis(String diagnosis) {
        this.diagnosis = diagnosis;
    }

    public String getTreatment() {
        return treatment;
    }

    public void setTreatment(String treatment) {
        this.treatment = treatment;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    @Override
    public String toString() {
        return "Visite du " + visitDate + " - " + animal.getName();
    }
}