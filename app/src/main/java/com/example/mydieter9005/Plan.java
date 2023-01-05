package com.example.mydieter9005;

import java.io.Serializable;

public class Plan implements Serializable {
    private double targetCalories;
    private double targetProteins;
    private double targetFats;
    private String goal;

    public Plan(double targetCalories, double targetProteins, double targetFats){
        this.targetCalories = targetCalories;
        this.targetProteins = targetProteins;
        this.targetFats = targetFats;
        this.goal = "Custom";
    }

    public Plan(String targetCalories, String targetProteins, String targetFats) {
        this.targetCalories = Double.parseDouble(targetCalories);
        this.targetProteins = Double.parseDouble(targetProteins);
        this.targetFats = Double.parseDouble(targetFats);
        this.goal = "Custom";
    }

    public Plan(String goal, String sex, double weight, double height, int age){
        double calories, proteins, fats;
        if(sex.equals("Man")) {
            calories = 66.5 + (13.8 * weight) + (5 * height) - (6.8 * age);
            fats = calories * 0.15;
        }
        else {
            calories = 655.1 + (9.6 * weight) + (1.9 * height) - (4.7 * age);
            fats = calories * 0.2;
        }
        proteins = calories * 0.2;

        if(goal.equals("Maintain weight")){
            this.targetCalories = calories;
            this.targetProteins = proteins;
            this.targetFats = fats;
        }

        if(goal.equals("Lose weight")){
            this.targetCalories = calories * 0.8;
            this.targetProteins = proteins * 0.8;
            this.targetFats = fats * 0.8;
        }

        if(goal.equals("Gain weight")){
            this.targetCalories = calories * 1.15;
            this.targetProteins = proteins * 1.15;
            this.targetFats = fats * 1.15;
        }
        this.goal = goal;
    }

    public double getTargetCalories() {
        return targetCalories;
    }

    public void setTargetCalories(double targetCalories) {
        this.targetCalories = targetCalories;
    }

    public double getTargetProteins() {
        return targetProteins;
    }

    public void setTargetProteins(double targetProteins) {
        this.targetProteins = targetProteins;
    }

    public double getTargetFats() {
        return targetFats;
    }

    public void setTargetFats(double targetFats) {
        this.targetFats = targetFats;
    }

    public String getGoal() {
        return goal;
    }

    @Override
    public String toString() {
        return this.targetCalories + " , " + this.targetProteins + " , " + this.targetFats;
    }
}
