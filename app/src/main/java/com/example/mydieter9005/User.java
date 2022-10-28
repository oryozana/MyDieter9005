package com.example.mydieter9005;

import java.io.Serializable;

public class User implements Serializable {
    private static User currentUser;
    private String username;
    private String password;
    private String Email;
    private double startingWeight;
    private double weight;
    private Plan currentPlan;

    public User(String username, String password, String Email, double startingWeight, Plan currentPlan){
        this.username = username;
        this.password = password;
        this.Email = Email;
        this.startingWeight = startingWeight;
        this.weight = startingWeight;
        this.currentPlan = currentPlan;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public double getStartingWeight() {
        return startingWeight;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public Plan getCurrentPlan() {
        return currentPlan;
    }

    public void setCurrentPlan(Plan currentPlan) {
        this.currentPlan = currentPlan;
    }

    public static User getCurrentUser() {
        return currentUser;
    }

    public static void setCurrentUser(User currentUser) {
        User.currentUser = currentUser;
    }

    @Override
    public String toString() {
        return this.username + " , " + this.password + " , " + this.Email + " , " + this.startingWeight + " , " + this.weight + " , " + this.currentPlan.toString();
    }
}
