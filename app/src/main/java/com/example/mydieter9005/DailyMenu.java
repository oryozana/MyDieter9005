package com.example.mydieter9005;

import java.util.ArrayList;

public class DailyMenu {
    private static DailyMenu todayMenu;
    private ArrayList<Food> breakfast;
    private ArrayList<Food> lunch;
    private ArrayList<Food> dinner;
    private double totalProteins;
    private double totalFats;
    private double totalCalories;
    private final String date;

    public DailyMenu(String date) {
        this.breakfast = new ArrayList<Food>();
        this.lunch = new ArrayList<Food>();
        this.dinner = new ArrayList<Food>();

        this.totalProteins = 0;
        this.totalFats = 0;
        this.totalCalories = 0;
        this.date = date;
    }

    public static DailyMenu getTodayMenu() {
        return todayMenu;
    }

    public static void setTodayMenu(DailyMenu todayMeals) {
        DailyMenu.todayMenu = todayMeals;
    }

    public String generateDailyMenuDescriptionForFiles(DailyMenu dailyMeals){
        String message = "DailyMenu { ";

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

    public static DailyMenu generateDailyMenuObjectFromFile(String data){
        return null;
    }

    public boolean isThereAtLeastOneThing(){
        boolean found = false;

        if(this.breakfast.size() != 0)
            found = true;

        if(this.lunch.size() != 0)
            found = true;

        if(this.dinner.size() != 0)
            found = true;

        return found;
    }

    private void roundNutritionalValues(){
        this.totalProteins = Math.round(this.totalProteins * 1000.0) / 1000.0;
        this.totalFats = Math.round(this.totalFats * 1000.0) / 1000.0;
        this.totalCalories = Math.round(this.totalCalories * 1000.0) / 1000.0;
    }

    private void addFoodNutritionalValues(Food food) {
        this.totalProteins += food.getProteins();
        this.totalFats += food.getFats();
        this.totalCalories += food.getCalories();
        roundNutritionalValues();
    }

    private void subtractFoodNutritionalValues(Food food) {
        this.totalProteins -= food.getProteins();
        this.totalFats -= food.getFats();
        this.totalCalories -= food.getCalories();
        roundNutritionalValues();
    }

    public ArrayList<Food> getBreakfast() {
        return this.breakfast;
    }

    public boolean hasBreakfast() {
        return this.breakfast.size() != 0;
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

    public boolean hasLunch(){
        return this.lunch.size() != 0;
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

    public boolean hasDinner() {
        return this.dinner.size() != 0;
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

    public String getUnitedBreakfastName(){
        if(breakfast.size() == 0)
            return "null";

        String unitedName = breakfast.get(0).name;
        for(int i = 1; i < breakfast.size(); i++){
            if(i == breakfast.size() - 1)
                unitedName += " and " + breakfast.get(i).name;
            else
                unitedName += breakfast.get(i).name + ".";
        }
        return unitedName;
    }

    public String getUnitedLunchName(){
        if(lunch.size() == 0)
            return "null";

        String unitedName = lunch.get(0).name;
        for(int i = 1; i < lunch.size(); i++){
            if(i == lunch.size() - 1)
                unitedName += " and " + lunch.get(i).name;
            else
                unitedName += " " + lunch.get(i).name + ".";
        }
        return unitedName;
    }

    public String getUnitedDinnerName(){
        if(dinner.size() == 0)
            return "";

        String unitedName = dinner.get(0).name;
        for(int i = 1; i < dinner.size(); i++){
            if(i == dinner.size() - 1)
                unitedName += " and " + dinner.get(i).name;
            else
                unitedName += dinner.get(i).name + ".";
        }
        return unitedName;
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
