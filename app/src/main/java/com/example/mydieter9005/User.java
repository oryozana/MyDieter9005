package com.example.mydieter9005;

import java.io.Serializable;
import java.util.ArrayList;

public class User implements Serializable {
    private static ArrayList<User> localUsers;
    private static User PrimaryUser;
    private static User currentUser;
    private String username;
    private String password;
    private String Email;
    private final double startingWeight;
    private double weight;
    private Plan currentPlan;
    private int profilePictureId;
    private ArrayList<DailyMenu> dailyMeals;

    public User(String username, String password, String Email, double startingWeight, Plan currentPlan, int profilePictureId){
        this.username = username;
        this.password = password;
        this.Email = Email;
        this.startingWeight = startingWeight;
        this.weight = startingWeight;
        this.currentPlan = currentPlan;
        this.profilePictureId = profilePictureId;
        this.dailyMeals = new ArrayList<DailyMenu>();
    }

    public User(String username, String password, String Email, String startingWeight, String targetCalories, String targetProteins, String targetFats, String profilePictureId){
        this.username = username;
        this.password = password;
        this.Email = Email;
        this.startingWeight = Double.parseDouble(startingWeight);
        this.weight = Double.parseDouble(startingWeight);
        this.currentPlan = new Plan(targetCalories, targetProteins, targetFats);
        this.profilePictureId = Integer.parseInt(profilePictureId);
        this.dailyMeals = new ArrayList<DailyMenu>();
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

    public int getProfilePictureId() {
        return profilePictureId;
    }

    public void setProfilePictureId(int profilePictureId) {
        this.profilePictureId = profilePictureId;
    }

    public static ArrayList<User> obtainLocalUsers() {
        return localUsers;
    }

    public static void setLocalUsers(ArrayList<User> localUsers) {
        User.localUsers = localUsers;
    }

    public static User obtainPrimaryUser() {
        return PrimaryUser;
    }

    public static void setPrimaryUser(User primaryUser) {
        PrimaryUser = primaryUser;
    }

    public static User getCurrentUser() {
        return currentUser;
    }

    public static void setCurrentUser(User currentUser) {
        User.currentUser = currentUser;
    }

    public ArrayList<DailyMenu> getDailyMeals() {
        return dailyMeals;
    }

    public void setDailyMeals(ArrayList<DailyMenu> dailyMeals) {
        this.dailyMeals = dailyMeals;
    }

    public boolean hasTodayMenu(String currentDate){
        for(int i = 0; i < this.dailyMeals.size(); i++){
            if(this.dailyMeals.get(i).getDate().equals(currentDate))
                return true;
        }
        return false;
    }

    public DailyMenu getTodayMenu(String currentDate){
        if(hasTodayMenu(currentDate)){
            for(int i = 0; i < this.dailyMeals.size(); i++){
                if(this.dailyMeals.get(i).getDate().equals(currentDate))
                    return this.dailyMeals.get(i);
            }
        }
        return null;
    }

    public void addDailyMeals(DailyMenu dailyMeals) {
        boolean found = false;

        for(int i = 0; i < this.dailyMeals.size(); i++){
            if(this.dailyMeals.get(i).getDate().equals(dailyMeals.getDate()) && !found){
                this.dailyMeals.remove(i);
                this.dailyMeals.add(dailyMeals);
                found = true;
            }
        }

        if(this.dailyMeals.size() == 0)
            this.dailyMeals.add(dailyMeals);
    }

    @Override
    public String toString() {
        return this.username + " , " + this.password + " , " + this.Email + " , " + this.startingWeight + " , " + this.weight + " , " + this.currentPlan.toString();
    }
}
