package com.example.mydieter9005;

public class Food {
    protected String name;
    protected double grams;
    protected double proteins;
    protected double fats;
    protected double calories; // Per gram.
    protected int amount;

    public Food(String name, double grams, double proteins, double fats, double calories) {
        this.name = name;
        this.grams = 0;
        this.proteins = proteins / grams;
        this.fats = fats / grams;
        this.calories = calories / grams;
        this.amount = 0;
    }

    public Food(String name, double proteins, double fats, double calories) {
        this.name = name;
        this.grams = 100;
        this.proteins = proteins / grams;
        this.fats = fats / grams;
        this.calories = calories / grams;
        this.amount = 0;
    }

    public Food(String name){
        this.name = name;
        this.amount = 0;
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
        this.grams = grams;
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

    public int getAmount() {
        return amount;
    }

    public void addAmount(int amount) {
        this.amount += amount;
    }

    @Override
    public String toString() {
        return name;
    }
}
