package com.example.mydieter9005;

import java.util.ArrayList;
import java.util.Locale;


import java.util.ArrayList;
import java.util.Locale;

public class Meal extends Food {
    private final ArrayList<Ingredient> ingredients = Ingredient.getIngredientsList();
    private ArrayList<Ingredient> neededIngredientsForMeal;

    public Meal(String name) {
        super(name);
        this.neededIngredientsForMeal = new ArrayList<Ingredient>();
        initiateNeededIngredientsForMeal(name);
        updateMealInfo();
    }

    public void initiateNeededIngredientsForMeal(String name){
        String[] mealParts = name.split(" and | with | include ");
        for(String mealPart : mealParts){
            mealPart = mealPart.toLowerCase();
            if(ingredients.contains(Ingredient.getIngredientByName(mealPart)))
                addIfNeeded(Ingredient.getIngredientByName(mealPart));
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
        if(neededIngredientsForMeal.contains(ingredient)){
            neededIngredientsForMeal.get(neededIngredientsForMeal.indexOf(ingredient)).addAmount(1);
            if(ingredient.getName().equals("olive"))
                neededIngredientsForMeal.get(neededIngredientsForMeal.indexOf(ingredient)).addAmount(7);
        }
        else
            neededIngredientsForMeal.add(ingredient);
    }

    public void updateMealInfo(){
        for(int i = 0; i < this.neededIngredientsForMeal.size(); i++){
            if(this.neededIngredientsForMeal.get(i) != null) {
                Ingredient ingredient = neededIngredientsForMeal.get(i);
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

    //    @Override
    public String toString() {
        return "Meal{" +
                "name='" + name + '\'' +
                ", grams=" + grams +
                ", proteins=" + proteins +
                ", fats=" + fats +
                ", calories=" + calories +
                ", amount=" + amount +
                //", ingredients=" + ingredients +
                ", neededIngredientsForMeal=" + neededIngredientsForMeal +
                '}';
    }
}

