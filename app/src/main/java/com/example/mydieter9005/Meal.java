package com.example.mydieter9005;

import android.util.Log;

import java.util.ArrayList;
import java.util.Locale;

public class Meal extends Food {
    private final ArrayList<Ingredient> ingredients = Ingredient.getIngredientsList();
    private ArrayList<Ingredient> neededIngredientsForMeal = new ArrayList<Ingredient>();
    protected int amount = 0;

    public Meal(String name){
        super(name);
    }

    public Meal(String name, int grams) {  // Single ingredient meal.
        super(name);
        initiateNeededIngredientsForMeal(name, grams);
        updateMealInfo();
    }

    public Meal(String name, ArrayList<Ingredient> ingredientsNeeded){
        super(name);
        for(int i = 0; i < ingredientsNeeded.size(); i++) {
            Ingredient ingredient = ingredientsNeeded.get(i);
            ingredient.setName(ingredient.getName().toLowerCase(Locale.ROOT));
            this.neededIngredientsForMeal.add(ingredient);
        }
        updateMealInfo();
    }

//    public Meal(Meal meal1, Meal meal2){
//        super(meal2.name);
//        if(meal1 != null) {
//            this.name = meal1.name + " and " + this.name;
//            addNeededIngredientsForMeal(meal1.neededIngredientsForMeal, meal2.neededIngredientsForMeal);
//        }
//        else
//            setNeededIngredientsForMeal(meal2.neededIngredientsForMeal);
//    }

    public String generateMealDescriptionForFiles(){
        String message = "Meal [ " + this.name + ": ";

        for(int i = 0; i < this.neededIngredientsForMeal.size(); i++)
            message += "   " + this.neededIngredientsForMeal.get(i).generateIngredientDescriptionForFiles();

        message += " ]";

        return message;
    }

    public static Meal generateMealObjectFromFileDescription(String data){
        ArrayList<Ingredient> ingredients = new ArrayList<Ingredient>();
        String[] dataParts;
        String name;

        dataParts = data.split("Meal \\[ ", 2);
        dataParts = dataParts[1].split(" \\]", 2);
        data = dataParts[0];

        name = data.split(": ")[0];
        data = data.split(": ")[1];

        dataParts = data.split("   ");
        for(int i = 0; i < dataParts.length; i++) {
            if(!dataParts[i].equals(""))
                ingredients.add(Ingredient.generateIngredientObjectFromFileDescription(dataParts[i] + " ]"));
        }

        return new Meal(name, ingredients);
    }

    public void initiateNeededIngredientsForMeal(String name, int grams){
        String[] mealParts = name.split(" and | with | include ");
        for(String mealPart : mealParts){
            mealPart = mealPart.toLowerCase(Locale.ROOT);
            if(ingredients.contains(Ingredient.getIngredientByName(mealPart)))
                addIfNeeded(new Ingredient(Ingredient.getIngredientByName(mealPart), grams));
        }
    }

    private void addIfNeeded(Ingredient ingredient) {
        if(this.neededIngredientsForMeal.contains(ingredient)){
            this.neededIngredientsForMeal.get(this.neededIngredientsForMeal.indexOf(ingredient)).addGrams(ingredient.getGrams());
        }
        else
            this.neededIngredientsForMeal.add(ingredient);
    }

    public void updateMealInfo(){
        for(int i = 0; i < this.neededIngredientsForMeal.size(); i++){
            if(this.neededIngredientsForMeal.get(i) != null) {
                Ingredient ingredient = this.neededIngredientsForMeal.get(i);
                this.grams += ingredient.grams;
                this.proteins += ingredient.proteins;
                this.fats += ingredient.fats;
                this.calories += ingredient.calories;
            }
        }
        roundValues();
    }

    public int getIngredientIndexInMealIngredients(Ingredient ingredient){
        for(int i = 0; i < this.neededIngredientsForMeal.size(); i++){
            if(this.neededIngredientsForMeal.get(i).name.equals(ingredient.name))
                return i;
        }
        return -1;
    }

    public ArrayList<Ingredient> getNeededIngredientsForMeal() {
        return this.neededIngredientsForMeal;
    }

    public void addNeededIngredientForMeal(Ingredient ingredient){
        this.neededIngredientsForMeal.add(ingredient);
        updateMealInfo();
    }

    public void addNeededIngredientsForMeal(ArrayList<Ingredient> neededIngredientsForMeal) {
        for(int i = 0; i < neededIngredientsForMeal.size(); i++)
            this.neededIngredientsForMeal.add(neededIngredientsForMeal.get(i));
        updateMealInfo();
    }

    public void addNeededIngredientsForMeal(ArrayList<Ingredient> neededIngredientsForMeal1, ArrayList<Ingredient> neededIngredientsForMeal2) {
        for(int i = 0; i < neededIngredientsForMeal1.size(); i++)
            this.neededIngredientsForMeal.add(neededIngredientsForMeal1.get(i));
        for(int i = 0; i < neededIngredientsForMeal2.size(); i++)
            this.neededIngredientsForMeal.add(neededIngredientsForMeal2.get(i));
        updateMealInfo();
    }

    public void setNeededIngredientsForMeal(ArrayList<Ingredient> neededIngredientsForMeal) {
        this.neededIngredientsForMeal.clear();
        for(int i = 0; i < neededIngredientsForMeal.size(); i++)
            this.neededIngredientsForMeal.add(neededIngredientsForMeal.get(i));
        resetMealInfo();
        updateMealInfo();
    }

    public void setNeededIngredientsForMeal(ArrayList<Ingredient> neededIngredientsForMeal, Ingredient ingredient) {
        this.neededIngredientsForMeal.clear();
        for(int i = 0; i < neededIngredientsForMeal.size(); i++)
            this.neededIngredientsForMeal.add(neededIngredientsForMeal.get(i));
        this.neededIngredientsForMeal.add(ingredient);
        resetMealInfo();
        updateMealInfo();
    }

    public void resetMealInfo(){
        this.grams = 0;
        this.proteins = 0;
        this.fats = 0;
        this.calories = 0;
    }

    public String getMealInfo(){
        roundValues();
        return "Name: " + this.name + "\n" + "Grams: " + this.grams + "\n" + "Protein: " + this.proteins + "\n" + "Fats: " + this.fats + "\n" + "Calories: " + this.calories + ".";
    }

    public int getAmount() {
        return amount;
    }

    public void addAmount(int amount) {
        this.amount += amount;
    }

    @Override
    public String toString() {
        return this.name + ": " + this.grams + " grams, " + this.calories + " calories.";
    }
}

