package com.example.mydieter9005;

import java.io.Serializable;

public class Food implements Serializable {
    protected String name;
    protected double grams = 0;
    protected double proteins = 0;
    protected double fats = 0;
    protected double calories = 0; // Per gram.

    public Food(String name, double grams, double proteins, double fats, double calories) {
        this.name = name;
        this.grams = grams;
        this.proteins = proteins;
        this.fats = fats;
        this.calories = calories;
        roundValues();
    }

    public Food(String name, double proteins, double fats, double calories) { // Default ingredient maker.
        this.name = name;
        this.grams = 100;
        this.proteins = proteins / grams;
        this.fats = fats / grams;
        this.calories = calories / grams;
        roundValues();
    }

    public Food(String name){
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getGrams() {
        return grams;
    }

    public void setGrams(double grams) {
        this.calories /= this.grams;
        this.proteins /= this.grams;
        this.fats /= this.grams;
        this.grams = grams;
        this.calories *= this.grams;
        this.proteins *= this.grams;
        this.fats *= this.grams;
        roundValues();
    }

    public void roundValues(){
        this.grams = Math.round(this.grams * 1000.0) / 1000.0;
        this.proteins = Math.round(this.proteins * 1000.0) / 1000.0;
        this.fats = Math.round(this.fats * 1000.0) / 1000.0;
        this.calories = Math.round(this.calories * 1000.0) / 1000.0;
    }

    public double getProteins() {
        return proteins;
    }

    public void setProteins(double proteins) {
        this.proteins = proteins;
    }

    public double getFats() {
        return fats;
    }

    public void setFats(double fats) {
        this.fats = fats;
    }

    public double getCalories() {
        return calories;
    }

    public void setCalories(double calories) {
        this.calories = calories;
    }

    @Override
    public String toString() {
        return name + ": " + this.grams + " grams.";
    }
}
