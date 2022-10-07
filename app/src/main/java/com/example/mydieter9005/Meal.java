package com.example.mydieter9005;

import android.widget.Toast;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Locale;


import java.util.ArrayList;
import java.util.Locale;

public class Meal extends Food {
    private final ArrayList<Ingredient> ingredients = Ingredient.getIngredientsList();
    private ArrayList<Ingredient> neededIngredientsForMeal = new ArrayList<Ingredient>();

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

    public Meal(Meal meal1, Meal meal2){
        super(meal1.name);
        this.grams = meal1.grams + meal2.grams;
        this.proteins = meal1.proteins + meal2.proteins;
        this.fats = meal1.fats + meal2.fats;
        this.calories = meal1.calories + meal2.calories;
    }

    public void initiateNeededIngredientsForMeal(String name, int grams){
        String[] mealParts = name.split(" and | with | include ");
        for(String mealPart : mealParts){
            mealPart = mealPart.toLowerCase(Locale.ROOT);
            if(ingredients.contains(Ingredient.getIngredientByName(mealPart)))
                addIfNeeded(new Ingredient(Ingredient.getIngredientByName(mealPart), grams));
        }
    }

//    public void addIfMiniMealInside(String mealPart){
//        if (mealPart.equals("toast")) {
//            addIfNeeded(Ingredient.getIngredientByName("bread"));
//            addIfNeeded(Ingredient.getIngredientByName("yellow cheese"));
//            addIfNeeded(Ingredient.getIngredientByName("ketchup"));
//            addIfNeeded(Ingredient.getIngredientByName("thousand island dressing"));
//        }
//        if (mealPart.equals("salad")) {
//            addIfNeeded(Ingredient.getIngredientByName("tomato"));
//            addIfNeeded(Ingredient.getIngredientByName("cucumber"));
//            addIfNeeded(Ingredient.getIngredientByName("lettuce"));
//        }
//    }

    private void addIfNeeded(Ingredient ingredient) {
        if(this.neededIngredientsForMeal.contains(ingredient)){
            this.neededIngredientsForMeal.get(this.neededIngredientsForMeal.indexOf(ingredient)).addAmount(1);
            if(ingredient.getName().equals("olive"))
                this.neededIngredientsForMeal.get(this.neededIngredientsForMeal.indexOf(ingredient)).addAmount(7);
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

    public void setNeededIngredientsForMeal(ArrayList<Ingredient> neededIngredientsForMeal) {
        this.neededIngredientsForMeal.clear();
        for(int i = 0; i < neededIngredientsForMeal.size(); i++)
            this.neededIngredientsForMeal.add(neededIngredientsForMeal.get(i));
        resetMealInfo();
        updateMealInfo();
    }

    public void resetMealInfo(){
        this.grams = 0;
        this.proteins = 0;
        this.fats = 0;
        this.calories = 0;
    }

    @Override
    public String toString() {
        return this.name + ": " + this.grams + " grams, " + this.calories + " calories.";
    }
}

