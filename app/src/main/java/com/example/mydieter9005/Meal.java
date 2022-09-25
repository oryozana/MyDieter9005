package com.example.mydieter9005;

import java.util.ArrayList;

public class Meal extends Food {
    private ArrayList<Ingredient> ingredients = Ingredient.getIngredientsList();
    private ArrayList<Ingredient> neededIngredientsForMeal;

    public Meal(String name) {
        super(name);
        initiateNeededIngredientsForMeal(name);
        updateMealInfo();
    }

    public void initiateNeededIngredientsForMeal(String name){
        String[] mealsPart = name.split(" and | with | include ");
        for(String mealPart : mealsPart){
            Ingredient mealPartIngredient = Ingredient.getIngredientByName(mealPart);
            if(ingredients.contains(mealPartIngredient)){
                addIfNeeded(mealPartIngredient);
            }
            else{
                addIfMiniMealInside(mealPart);
            }
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
        for(Ingredient ingredient : neededIngredientsForMeal){
            this.grams += ingredient.grams;
            this.proteins += ingredient.proteins;
            this.fats += ingredient.fats;
            this.calories += ingredient.calories;
        }
    }

    public ArrayList<Ingredient> getNeededIngredientsForMeal() {
        return neededIngredientsForMeal;
    }

    public void setNeededIngredientsForMeal(ArrayList<Ingredient> neededIngredientsForMeal) {
        this.neededIngredientsForMeal = neededIngredientsForMeal;
    }
}
