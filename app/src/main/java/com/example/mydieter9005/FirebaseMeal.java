package com.example.mydieter9005;

import java.util.ArrayList;

public class FirebaseMeal {
    private ArrayList<FirebaseIngredient> neededIngredientsForMeal = new ArrayList<FirebaseIngredient>();
    private String name;

    public FirebaseMeal(String name, ArrayList<Ingredient> ingredientsNeeded){
        this.name = User.getCurrentUser().getUsername() + " - " + name;
        for(int i = 0; i < ingredientsNeeded.size(); i++)
            this.neededIngredientsForMeal.add(new FirebaseIngredient(ingredientsNeeded.get(i)));
    }

    public FirebaseMeal(Meal meal){
        this.name = User.getCurrentUser().getUsername() + " - " + meal.getName();
        for(int i = 0; i < meal.getNeededIngredientsForMeal().size(); i++)
            this.neededIngredientsForMeal.add(new FirebaseIngredient(meal.getNeededIngredientsForMeal().get(i)));
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<FirebaseIngredient> getNeededIngredientsForMeal() {
        return this.neededIngredientsForMeal;
    }

    public void setNeededIngredientsForMeal(ArrayList<FirebaseIngredient> ingredientsNeeded){
        this.neededIngredientsForMeal = ingredientsNeeded;
    }
}
