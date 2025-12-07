package com.example.sanisidropharmacy;

public class User {
    private int id;
    private String fullname;
    private String email;
    private String contact;
    private String address;
    private int loyalty_points;

    private String birthdate;
    private String role;

    public String getBirthdate() { return birthdate; }
    public void setBirthdate(String birthdate) { this.birthdate = birthdate; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    // getters and setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getFullname() { return fullname; }
    public void setFullname(String fullname) { this.fullname = fullname; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getContact() { return contact; }
    public void setContact(String contact) { this.contact = contact; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public int getLoyalty_points() { return loyalty_points; }
    public void setLoyalty_points(int loyalty_points) { this.loyalty_points = loyalty_points; }
}
