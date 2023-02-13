package com.example.mydieter9005;

public class FirebaseIngredient {
    private String name;
    private double grams;

    public FirebaseIngredient(String name, double grams){
        this.name = name;
        this.grams = grams;
    }

    public FirebaseIngredient(Ingredient ingredient){
        this.name = ingredient.getName();
        this.grams = ingredient.getGrams();
    }

    public double getGrams() {
        return grams;
    }

    public void setGrams(double grams) {
        this.grams = grams;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
