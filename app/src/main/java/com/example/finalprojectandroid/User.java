package com.example.finalprojectandroid;

public class User {
    private int id;
    private String username;
    private String email;
    private String name;
    private String birthdate;
    private String city;
    private String country;

    // Construtor
    public User(int id, String username, String email, String name, String birthdate, String city, String country) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.name = name;
        this.birthdate = birthdate;
        this.city = city;
        this.country = country;
    }

    // Getters
    public int getId() { return id; }
    public String getUsername() { return username; }
    public String getEmail() { return email; }
    public String getName() { return name; }
    public String getBirthdate() { return birthdate; }
    public String getCity() { return city; }
    public String getCountry() { return country; }
}