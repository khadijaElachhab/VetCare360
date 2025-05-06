package com.vetcare360.model;

import javafx.beans.value.ObservableValue;

public class Veterinarian {
    private int id;
    private String firstName;
    private String lastName;
    private String specialization;
    private String phone;
    private String email;

    public Veterinarian() {
    }

    public Veterinarian(int id, String firstName, String lastName, String specialization,
                        String phone, String email) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.specialization = specialization;
        this.phone = phone;
        this.email = email;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFullName() {
        return "Dr. " + firstName + " " + lastName;
    }

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "Dr. " + lastName + ", " + firstName + " (" + specialization + ")";
    }

    public ObservableValue<String> nameProperty() {

        return null;
    }

    public String getName() {
        return "";
    }
}
