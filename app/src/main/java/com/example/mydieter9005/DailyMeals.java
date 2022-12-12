package com.example.mydieter9005;

import java.util.ArrayList;

public class DailyMeals {
    private static DailyMeals todayMeals;
    private ArrayList<Food> breakfast;
    private ArrayList<Food> lunch;
    private ArrayList<Food> dinner;
    private double totalProteins;
    private double totalFats;
    private double totalCalories;
    private final String date;

    public DailyMeals(String date) {
        this.breakfast = new ArrayList<Food>();
        this.lunch = new ArrayList<Food>();
        this.dinner = new ArrayList<Food>();

        this.totalProteins = 0;
        this.totalFats = 0;
        this.totalCalories = 0;
        this.date = date;
    }

    public static DailyMeals getTodayMeals() {
        return todayMeals;
    }

    public static void setTodayMeals(DailyMeals todayMeals) {
        DailyMeals.todayMeals = todayMeals;
    }

    public String generateDailyMealsDescriptionForFiles(DailyMeals dailyMeals){
        String message = "DailyMeals { ";

        message += "breakfast ( ";
        for(int i = 0; i < this.breakfast.size(); i++){
            Food food = this.breakfast.get(i);
            if(food instanceof Meal){
                message += ((Meal) food).generateMealDescriptionForFiles();
            }
            if(food instanceof Ingredient){
                message += ((Ingredient) food).generateIngredientDescriptionForFiles();
            }
        }

        if(this.breakfast.size() == 0)
            message += "null";

        message += " ) ";


        message += "lunch ( ";
        for(int i = 0; i < this.lunch.size(); i++){
            Food food = this.lunch.get(i);
            if(food instanceof Meal){
                message += ((Meal) food).generateMealDescriptionForFiles();
            }
            if(food instanceof Ingredient){
                message += ((Ingredient) food).generateIngredientDescriptionForFiles();
            }
        }

        if(this.lunch.size() == 0)
            message += "null";

        message += " ) ";


        message += "dinner ( ";
        for(int i = 0; i < this.dinner.size(); i++){
            Food food = this.dinner.get(i);
            if(food instanceof Meal){
                message += ((Meal) food).generateMealDescriptionForFiles();
            }
            if(food instanceof Ingredient){
                message += ((Ingredient) food).generateIngredientDescriptionForFiles();
            }
        }

        if(this.dinner.size() == 0)
            message += "null";

        message += " ) ";

        message += "date: " + this.date + " }";

        return message;
    }

    public static DailyMeals generateDailyMealsObjectFromFile(String data){
        return null;
    }

    private void addFoodNutritionalValues(Food food) {
        this.totalProteins += food.getProteins();
        this.totalFats += food.getFats();
        this.totalCalories += food.getCalories();
    }

    private void subtractFoodNutritionalValues(Food food) {
        this.totalProteins -= food.getProteins();
        this.totalFats -= food.getFats();
        this.totalCalories -= food.getCalories();
    }

    public ArrayList<Food> getBreakfast() {
        return this.breakfast;
    }

    public void addBreakfast(Food breakfast) {
        this.breakfast.add(breakfast);
        addFoodNutritionalValues(breakfast);
    }

    public void removeBreakfast(String breakfastName) {
        boolean removed = false;
        for(int i = 0; i < this.breakfast.size(); i++){
            if(breakfastName.equals(this.breakfast.get(i).getName()) && !removed) {
                subtractFoodNutritionalValues(this.breakfast.get(i));
                this.breakfast.remove(i);
                removed = true;
            }
        }
    }

    public ArrayList<Food> getLunch() {
        return this.lunch;
    }

    public void addLunch(Food lunch) {
        this.lunch.add(lunch);
        addFoodNutritionalValues(lunch);
    }

    public void removeLunch(String lunchName) {
        boolean removed = false;
        for(int i = 0; i < this.lunch.size(); i++){
            if(lunchName.equals(this.lunch.get(i).getName()) && !removed) {
                subtractFoodNutritionalValues(this.lunch.get(i));
                this.lunch.remove(i);
                removed = true;
            }
        }
    }

    public ArrayList<Food> getDinner() {
        return this.dinner;
    }

    public void addDinner(Food dinner) {
        this.dinner.add(dinner);
        addFoodNutritionalValues(dinner);
    }

    public void removeDinner(String dinnerName) {
        boolean removed = false;
        for(int i = 0; i < this.dinner.size(); i++){
            if(dinnerName.equals(this.dinner.get(i).getName()) && !removed) {
                subtractFoodNutritionalValues(this.dinner.get(i));
                this.dinner.remove(i);
                removed = true;
            }
        }
    }

    public double getTotalProteins() {
        return this.totalProteins;
    }

    public double getTotalFats() {
        return this.totalFats;
    }

    public double getTotalCalories() {
        return this.totalCalories;
    }

    public String getDate() {
        return this.date;
    }
}
