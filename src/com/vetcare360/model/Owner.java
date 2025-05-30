package com.vetcare360.model;

import java.util.ArrayList;
import java.util.List;

public class Owner {
    private int id;
    private String firstName;
    private String lastName;
    private String address;
    private String city;
    private String postalCode;
    private String phone;
    private String email;
    private List<Animal> pets;

    public Owner() {
        pets = new ArrayList<>();
    }

    public Owner(int id, String firstName, String lastName, String address,
                 String city, String postalCode, String phone, String email) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.city = city;
        this.postalCode = postalCode;
        this.phone = phone;
        this.email = email;
        this.pets = new ArrayList<>();
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
        return firstName + " " + lastName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
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

    public List<Animal> getPets() {
        return pets;
    }

    public void setPets(List<Animal> pets) {
        this.pets = pets;
    }

    public void addPet(Animal pet) {
        pets.add(pet);
        pet.setOwner(this.getId());
    }

    public void removePet(Animal pet) {
        pets.remove(pet);
    }

    @Override
    public String toString() {
        return lastName + ", " + firstName;
    }
}
