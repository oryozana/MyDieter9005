package com.example.mydieter9005;

import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;


import java.util.ArrayList;
import java.util.Locale;

public class Meal extends Food {
    private final ArrayList<Ingredient> ingredients = Ingredient.getIngredientsList();
    private ArrayList<Ingredient> neededIngredientsForMeal = new ArrayList<Ingredient>();

    public Meal(String name, int grams) {  // Single ingredient meal.
        super(name.toLowerCase());
        name = name.toLowerCase();
        initiateNeededIngredientsForMeal(name, grams);
        updateMealInfo();
    }

    public Meal(String name, ArrayList<Ingredient> ingredientsNeeded){
        super(name.toLowerCase());
        for(int i = 0; i < ingredientsNeeded.size(); i++)
            this.neededIngredientsForMeal.add(ingredientsNeeded.get(i));
        updateMealInfo();
    }

    public void initiateNeededIngredientsForMeal(String name, int grams){
        String[] mealParts = name.split(" and | with | include ");
        for(String mealPart : mealParts){
            mealPart = mealPart.toLowerCase();
            if(ingredients.contains(Ingredient.getIngredientByName(mealPart)))
                addIfNeeded(new Ingredient(Ingredient.getIngredientByName(mealPart), grams));
            else
                addIfMiniMealInside(mealPart);
        }
    }

    public void addIfMiniMealInside(String mealPart){
        if (mealPart.equals("toast")) {
            addIfNeeded(Ingredient.getIngredientByName("bread"));
            addIfNeeded(Ingredient.getIngredientByName("yellow cheese"));
            addIfNeeded(Ingredient.getIngredientByName("ketchup"));
            addIfNeeded(Ingredient.getIngredientByName("thousand island dressing"));
        }
        if (mealPart.equals("salad")) {
            addIfNeeded(Ingredient.getIngredientByName("tomato"));
            addIfNeeded(Ingredient.getIngredientByName("cucumber"));
            addIfNeeded(Ingredient.getIngredientByName("lettuce"));
        }
    }

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

    public ArrayList<Ingredient> getNeededIngredientsForMeal() {
        return neededIngredientsForMeal;
    }

    public void setNeededIngredientsForMeal(ArrayList<Ingredient> neededIngredientsForMeal) {
        this.neededIngredientsForMeal = neededIngredientsForMeal;
    }

    @Override
    public String toString() {
        return this.name + ": " + this.calories + " calories, " + this.fats + " fats.";
    }
}

