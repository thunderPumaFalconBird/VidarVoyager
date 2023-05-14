package com.vv.game.utils;

public class User {
    private String username;
    private String firstName;
    private String lastName;
    private String middleInitial;
    private String dateOfBirth;
    private String email;
    private String password;
    private int log_in_on_id;
    private static User instance = new User();

    private User(){}

    public static User getInstance(){ return instance; }

    public String getUsername(){ return username; }

    public void setUsername(String username) { this.username = username; }

    public String getFirstName() { return firstName; }

    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }

    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getMiddleInitial() { return middleInitial; }

    public void setMiddleInitial(String middleInitial) { this.middleInitial = middleInitial; }

    public String getDateOfBirth() { return dateOfBirth; }

    public void setDateOfBirth(String dateOfBirth) { this.dateOfBirth = dateOfBirth; }

    public String getEmail() { return email; }

    public void setEmail(String email) { this.email = email; }

    public String getPassword(){ return password; }

    public void setPassword(String password) { this.password = password; }

    public int getLog_in_on_id() { return log_in_on_id; }

    public void setLog_in_on_id(int log_in_on_id) { this.log_in_on_id = log_in_on_id; }
}
