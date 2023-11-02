package com.vv.game.utils;

import java.net.InetAddress;

/**
 * The User class is used to store user information. It uses the singleton pattern.
 */
public class User {
    private String username;
    private String firstName;
    private String lastName;
    private String middleInitial;
    private String dateOfBirth;
    private String email;
    private String password;
    private InetAddress userInet;
    private int log_in_on_id;
    private static User instance = new User();

    private User(){
        try {
            userInet = InetAddress.getLocalHost();
        } catch (Exception e){
            System.out.println(e);
        }
    }

    public boolean hasValidData(){
        return !username.isEmpty() || !firstName.isEmpty() || !lastName.isEmpty() || !email.isEmpty() || !password.isEmpty();
    }

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

    public String getInet(){ return this.userInet.getHostAddress();}

    public int getLog_in_on_id() { return log_in_on_id; }

    public void setLog_in_on_id(int log_in_on_id) { this.log_in_on_id = log_in_on_id; }
}
