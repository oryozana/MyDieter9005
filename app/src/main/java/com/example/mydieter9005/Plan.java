package com.example.mydieter9005;

import java.io.Serializable;

public class Plan implements Serializable {
    private double targetCalories;
    private double targetProteins;
    private double targetFats;

    public Plan(double targetCalories, double targetProteins, double targetFats){
        this.targetCalories = targetCalories;
        this.targetProteins = targetProteins;
        this.targetFats = targetFats;
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

    @Override
    public String toString() {
        return this.targetCalories + " , " + this.targetProteins + " , " + this.targetFats;
    }
}
