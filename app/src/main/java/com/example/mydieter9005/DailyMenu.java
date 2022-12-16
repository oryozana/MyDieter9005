package com.example.mydieter9005;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

public class DailyMenu {
    private static ArrayList<DailyMenu> dailyMenus;
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

    public static ArrayList<DailyMenu> getDailyMenus() {
        removeDailyMenuDuplications();
        return dailyMenus;
    }

    public static void setDailyMenus(ArrayList<DailyMenu> dailyMenus) {
        DailyMenu.dailyMenus = dailyMenus;
    }

    public static void setDailyMenus(Context context) {
        DailyMenu.dailyMenus = getDailyMenusFromFile(context);
    }

    public static DailyMenu getTodayMenu() {
        return todayMenu;
    }

    public static void setTodayMenu(DailyMenu todayMeals) {
        DailyMenu.todayMenu = todayMeals;
    }

    public String generateDailyMenuDescriptionForFiles(){
        String message = "DailyMenu { ";

        message += "breakfast ( ";
        for(int i = 0; i < this.breakfast.size(); i++){
            Food food = this.breakfast.get(i);
            if(food instanceof Meal){
                message += "     " + ((Meal) food).generateMealDescriptionForFiles();
            }
            if(food instanceof Ingredient){
                message += "     " + ((Ingredient) food).generateIngredientDescriptionForFiles();
            }
        }

        if(this.breakfast.size() == 0)
            message += "null";

        message += " ) ";


        message += "lunch ( ";
        for(int i = 0; i < this.lunch.size(); i++){
            Food food = this.lunch.get(i);
            if(food instanceof Meal){
                message += "     " + ((Meal) food).generateMealDescriptionForFiles();
            }
            if(food instanceof Ingredient){
                message += "     " + ((Ingredient) food).generateIngredientDescriptionForFiles();
            }
        }

        if(this.lunch.size() == 0)
            message += "null";

        message += " ) ";


        message += "dinner ( ";
        for(int i = 0; i < this.dinner.size(); i++){
            Food food = this.dinner.get(i);
            if(food instanceof Meal){
                message += "     " + ((Meal) food).generateMealDescriptionForFiles();
            }
            if(food instanceof Ingredient){
                message += "     " + ((Ingredient) food).generateIngredientDescriptionForFiles();
            }
        }

        if(this.dinner.size() == 0)
            message += "null";

        message += " ) ";

        message += "date: " + this.date + " }";

        return message;
    }

    public static DailyMenu generateDailyMenuObjectFromFile(String data){
        String[] dataParts, breakfastDataParts, lunchDataParts, dinnerDataParts;
        String breakfastData, lunchData, dinnerData, date = "";

        ArrayList<Food> breakfastFood = new ArrayList<Food>();
        ArrayList<Food> lunchFood = new ArrayList<Food>();
        ArrayList<Food> dinnerFood = new ArrayList<Food>();

        dataParts = data.split("DailyMenu \\{");
        dataParts = dataParts[1].split(" \\}");
        data = dataParts[0];

        dataParts = data.split(" breakfast \\( ");
        dataParts = dataParts[1].split(" \\)");
        breakfastData = dataParts[0];

        dataParts = data.split(" lunch \\( ");
        dataParts = dataParts[1].split(" \\)");
        lunchData = dataParts[0];

        dataParts = data.split(" dinner \\( ");
        dataParts = dataParts[1].split(" \\)");
        dinnerData = dataParts[0];

        date = data.split(" date: ")[1];

        if(!breakfastData.equals("null")){
            breakfastDataParts = breakfastData.split("     ");
            for(int i = 0; i < breakfastDataParts.length; i++){
                if(breakfastDataParts[i].split(" \\[ ")[0].equals("Meal"))
                    breakfastFood.add(Meal.generateMealObjectFromFileDescription(breakfastDataParts[i] + " ]"));
                if(breakfastDataParts[i].split(" \\[ ")[0].equals("Ingredient"))
                    breakfastFood.add(Ingredient.generateIngredientObjectFromFileDescription(breakfastDataParts[i] + " ]"));
            }
        }

        if(!lunchData.equals("null")){
            lunchDataParts = lunchData.split("     ");
            for(int i = 0; i < lunchDataParts.length; i++){
                if(lunchDataParts[i].split(" \\[ ")[0].equals("Meal"))
                    lunchFood.add(Meal.generateMealObjectFromFileDescription(lunchDataParts[i]));
                if(lunchDataParts[i].split(" \\[ ")[0].equals("Ingredient"))
                    lunchFood.add(Ingredient.generateIngredientObjectFromFileDescription(lunchDataParts[i]));
            }
        }

        if(!dinnerData.equals("null")){
            dinnerDataParts = dinnerData.split("     ");
            for(int i = 0; i < dinnerDataParts.length; i++){
                if(dinnerDataParts[i].split(" \\[ ")[0].equals("Meal"))
                    dinnerFood.add(Meal.generateMealObjectFromFileDescription(dinnerDataParts[i]));
                if(dinnerDataParts[i].split(" \\[ ")[0].equals("Ingredient"))
                    dinnerFood.add(Ingredient.generateIngredientObjectFromFileDescription(dinnerDataParts[i]));
            }
        }

        DailyMenu dailyMenu = new DailyMenu(date);
        for(int i = 0; i < breakfastFood.size(); i++)
            dailyMenu.addBreakfast(breakfastFood.get(i));

        for(int i = 0; i < lunchFood.size(); i++)
            dailyMenu.addLunch(lunchFood.get(i));

        for(int i = 0; i < dinnerFood.size(); i++)
            dailyMenu.addDinner(dinnerFood.get(i));

        return dailyMenu;
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
            if(breakfastName.equals(this.breakfast.get(i).name) && !removed) {
                subtractFoodNutritionalValues(this.breakfast.get(i));
                this.breakfast.remove(i);
                removed = true;
            }
        }
    }

    public void clearBreakfast(Context context){
        if(hasBreakfast()){
            ArrayList<Food> tmpBreakfast = new ArrayList<Food>(this.breakfast);
            for(int i = 0; i < tmpBreakfast.size(); i++)
                removeBreakfast(tmpBreakfast.get(i).name);

            saveDailyMenuIntoFile(this, context);
            Toast.makeText(context, "Breakfast successfully deleted.", Toast.LENGTH_SHORT).show();
        }
        else
            Toast.makeText(context, "Breakfast already empty.", Toast.LENGTH_SHORT).show();
    }

    public ArrayList<Ingredient> generateBreakfastIngredientsArray(){
        ArrayList<Ingredient> ingredients = new ArrayList<Ingredient>();
        Ingredient tmpIngredient;

        for(int i = 0; i < this.breakfast.size(); i++){
            Food food = this.breakfast.get(i);
            if(food instanceof Ingredient)
                ingredients.add(new Ingredient((Ingredient) food));

            if(food instanceof Meal){
                for(int j = 0; j < ((Meal) food).getNeededIngredientsForMeal().size(); j++){
                    tmpIngredient = ((Meal) food).getNeededIngredientsForMeal().get(j);
                    ingredients.add(new Ingredient(tmpIngredient));
                }
            }
        }
        return ingredients;
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

    public void clearLunch(Context context){
        if(hasLunch()){
            ArrayList<Food> tmpLunch = new ArrayList<Food>(this.lunch);
            for(int i = 0; i < tmpLunch.size(); i++)
                removeLunch(tmpLunch.get(i).name);

            saveDailyMenuIntoFile(this, context);
            Toast.makeText(context, "Lunch successfully deleted.", Toast.LENGTH_SHORT).show();
        }
        else
            Toast.makeText(context, "Lunch already empty.", Toast.LENGTH_SHORT).show();
    }

    public ArrayList<Ingredient> generateLunchIngredientsArray(){
        ArrayList<Ingredient> ingredients = new ArrayList<Ingredient>();
        Ingredient tmpIngredient;

        for(int i = 0; i < this.lunch.size(); i++){
            Food food = this.lunch.get(i);
            if(food instanceof Ingredient)
                ingredients.add(new Ingredient((Ingredient) food));

            if(food instanceof Meal){
                for(int j = 0; j < ((Meal) food).getNeededIngredientsForMeal().size(); j++){
                    tmpIngredient = ((Meal) food).getNeededIngredientsForMeal().get(j);
                    ingredients.add(new Ingredient(tmpIngredient));
                }
            }
        }
        return ingredients;
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

    public void clearDinner(Context context){
        if(hasDinner()){
            ArrayList<Food> tmpDinner = new ArrayList<Food>(this.dinner);
            for(int i = 0; i < tmpDinner.size(); i++)
                removeDinner(tmpDinner.get(i).name);

            saveDailyMenuIntoFile(this, context);
            Toast.makeText(context, "Dinner successfully deleted.", Toast.LENGTH_SHORT).show();
        }
        else
            Toast.makeText(context, "Dinner already empty.", Toast.LENGTH_SHORT).show();
    }

    public ArrayList<Ingredient> generateDinnerIngredientsArray(){
        ArrayList<Ingredient> ingredients = new ArrayList<Ingredient>();
        Ingredient tmpIngredient;

        for(int i = 0; i < this.dinner.size(); i++){
            Food food = this.dinner.get(i);
            if(food instanceof Ingredient)
                ingredients.add(new Ingredient((Ingredient) food));

            if(food instanceof Meal){
                for(int j = 0; j < ((Meal) food).getNeededIngredientsForMeal().size(); j++){
                    tmpIngredient = ((Meal) food).getNeededIngredientsForMeal().get(j);
                    ingredients.add(new Ingredient(tmpIngredient));
                }
            }
        }
        return ingredients;
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

    public void clearAllFood(Context context){
        this.breakfast.clear();
        this.lunch.clear();
        this.dinner.clear();

        this.totalProteins = 0;
        this.totalFats = 0;
        this.totalCalories = 0;

        saveDailyMenuIntoFile(this, context);
        Toast.makeText(context, "Successfully deleted all food.", Toast.LENGTH_SHORT).show();
    }

    public ArrayList<Ingredient> generateAllIngredientsNeededArrayList(){
        ArrayList<Ingredient> allIngredients = new ArrayList<Ingredient>();
        ArrayList<Ingredient> tmpIngredients;

        tmpIngredients = generateBreakfastIngredientsArray();
        for(int i = 0; i < tmpIngredients.size(); i++)
            allIngredients.add(tmpIngredients.get(i));

        tmpIngredients = generateLunchIngredientsArray();
        for(int i = 0; i < tmpIngredients.size(); i++)
            allIngredients.add(tmpIngredients.get(i));

        tmpIngredients = generateDinnerIngredientsArray();
        for(int i = 0; i < tmpIngredients.size(); i++)
            allIngredients.add(tmpIngredients.get(i));

        return allIngredients;
    }

    public static boolean hasTodayMenuInsideAllDailyMenus(String currentDate){
        for(int i = 0; i < dailyMenus.size(); i++){
            if(dailyMenus.get(i).getDate().equals(currentDate))
                return true;
        }
        return false;
    }

    public static DailyMenu getTodayMenuFromAllDailyMenus(String currentDate){
        if(hasTodayMenuInsideAllDailyMenus(currentDate)){
            for(int i = 0; i < dailyMenus.size(); i++){
                if(dailyMenus.get(i).getDate().equals(currentDate))
                    return dailyMenus.get(i);
            }
        }
        return null;
    }

    public static void removeDailyMenuDuplications(){
        ArrayList<DailyMenu> tmpDailyMenus = new ArrayList<DailyMenu>();
        boolean found;

        for(int i = 0; i < dailyMenus.size(); i++){
            found = false;
            for(int j = i + 1; j < dailyMenus.size(); j++){
                if(dailyMenus.get(i).date.equals(dailyMenus.get(j).date))
                    found = true;
            }

            if(!found)
                tmpDailyMenus.add(dailyMenus.get(i));
        }

        dailyMenus = tmpDailyMenus;
    }

    public static void removeDailyMenuDuplicationsAndAddAnotherOne(DailyMenu dailyMenu){
        ArrayList<DailyMenu> tmpDailyMenus = new ArrayList<DailyMenu>();
        boolean found;

        for(int i = 0; i < dailyMenus.size(); i++){
            found = false;
            for(int j = i + 1; j < dailyMenus.size(); j++){
                if(dailyMenus.get(i).date.equals(dailyMenus.get(j).date) || dailyMenus.get(i).date.equals(dailyMenu.date))
                    found = true;
            }

            if(!found)
                tmpDailyMenus.add(dailyMenus.get(i));
        }

        tmpDailyMenus.add(dailyMenu);
        dailyMenus = tmpDailyMenus;
    }

    public static void saveDailyMenuIntoFile(DailyMenu dailyMenu, Context context){
        FileOutputStream fos;
        OutputStreamWriter osw;
        BufferedWriter bw;

        try {
            fos = context.openFileOutput("dailyMenusFile", Context.MODE_PRIVATE);
            osw = new OutputStreamWriter(fos);
            bw = new BufferedWriter(osw);

            bw.write("Daily menus: " + "\n");

            DailyMenu.removeDailyMenuDuplicationsAndAddAnotherOne(dailyMenu);
            for(int i = 0; i < DailyMenu.getDailyMenus().size(); i++)
                bw.write(DailyMenu.getDailyMenus().get(i).generateDailyMenuDescriptionForFiles() + "\n");

            bw.close();
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static ArrayList<DailyMenu> getDailyMenusFromFile(Context context){
        String[] dataParts = getFileData("dailyMenusFile", context).split("\n");
        ArrayList<DailyMenu> dailyMenus = new ArrayList<DailyMenu>();

        for(int i = 1; i < dataParts.length; i++)
            dailyMenus.add(DailyMenu.generateDailyMenuObjectFromFile(dataParts[i]));
        return dailyMenus;
    }

    private static String getFileData(String fileName, Context context){
        FileInputStream is;
        InputStreamReader isr;
        BufferedReader br;

        String currentLine = "", allData = "";
        try{
            is = context.openFileInput(fileName);
            isr = new InputStreamReader(is);
            br = new BufferedReader(isr);

            currentLine = br.readLine();
            while(currentLine != null){
                allData += currentLine + "\n";
                currentLine = br.readLine();
            }
            br.close();
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return allData;
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

    @Override
    public String toString() {
        return "DailyMenu{" +
                "breakfast=" + breakfast +
                ", lunch=" + lunch +
                ", dinner=" + dinner +
                ", totalProteins=" + totalProteins +
                ", totalFats=" + totalFats +
                ", totalCalories=" + totalCalories +
                ", date='" + date + '\'' +
                '}';
    }
}
