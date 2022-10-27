package com.example.mydieter9005;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;

public class removedFunctions extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_removed_functions);
    }

    // From: ingredientPickup:

//    public int lookForSpecialWords(String[] mealParts, int i){
//        String previousIngredient, middleIngredient, nextIngredient;
//        int combo = 1;
//
//        if(mealParts[i].equals("oil") || mealParts[i].equals("powder")){
//            nextIngredient = mealParts[i - 1] + " " + mealParts[i];
//            removeIfNeeded(mealParts[i - 1], 1);
//            addIfNeeded(nextIngredient);
//            i += combo;
//            return i;
//        }
//
//        if(mealParts[i].equals("ice") || foodCompanies.contains(mealParts[i])){
//            previousIngredient = mealParts[i] + " " + mealParts[i + 1];
//            addIfNeeded(previousIngredient);
//            i += combo;
//            return i;
//        }
//
//        if (mealParts[i].equals("flavored")){
//            middleIngredient = mealParts[i - 1] + " " + mealParts[i] + " " + mealParts[i + 1];
//            if(mealParts[i + 1].equals("ice") || foodCompanies.contains(mealParts[i])){
//                middleIngredient += " " + mealParts[i + 2];
//                combo++;
//            }
//            addIfNeeded(middleIngredient);
//            removeIfNeeded(mealParts[i - 1], 1);
//            i += combo;
//            return i;
//        }
//        return i;
//    }

//    public void removeIfNeeded(String ingredient, int amountToRemove){
//        if(amount[ingredients.indexOf(ingredient)] == amountToRemove){
//            finalIngredients.remove(ingredient);
//            ingredient_amount -= 1;
//        }
//        else{
//            amount[ingredients.indexOf(ingredient)] -= amountToRemove;
//        }
//    }

        // From: selections activities.

//    public void setListViewFields(){
//        String[] fields = new String[mealsList.size()];
//        for(int i = 0; i < mealsList.size(); i += 3){
//            String field = mealsList.get(i) + ": " + mealsList.get(i + 1) + " calories, " + mealsList.get(i + 2) + " minutes.";
//            fields[i / 3] = field;
//        }
//        setListViewAdapter(fields);
//    }



//    public void setSavedModifiedMealsInfo(){
//        String data = getFileData(modifiedMealsFileName);
//        data = data.substring(data.indexOf('\n') + 1);
//
//        String[] dataParts = data.split("\n");
//        String[] mealPart;
//        String name;
//        double grams, proteins, fats, calories;
//        for(int i = 0; i < dataParts.length; i++){
//            mealPart = dataParts[i].split(" , ");
//
//            name = mealPart[0];
//            grams = Double.parseDouble(mealPart[1]);
//            proteins = Double.parseDouble(mealPart[2]);
//            fats = Double.parseDouble(mealPart[3]);
//            calories = Double.parseDouble(mealPart[4]);
//
//            Meal modifiedMeal = new Meal(name, grams, proteins, fats, calories);
//            if(getMealIndexInMealsList(modifiedMeal) != -1)  // Check if exist inside mealsList.
//                mealsList.set(getMealIndexInMealsList(modifiedMeal), modifiedMeal);
//        }
//    }


        // From: finishMeals:

//    public void initiateMealsRecipes(){
//        for(int mealIndex = 0; mealIndex < meals.length; mealIndex++) {
//            String meal = meals[mealIndex];
//            if (meal != null) {
//                mealParts = meal.split(" ", 100);
//                mealParts[0] = mealParts[0].toLowerCase();
//                for (int i = 0; i < mealParts.length; i++) {
//                    mealParts[i] = mealParts[i].replaceAll(" ", "");
//
//                    if (ingredients.contains(Ingredient.getIngredientByName(mealParts[i]))) {
//                        addIfNeeded(mealParts[i], mealIndex);
//                        if (mealParts[i].equals("olive")) {
//                            addExtra("olive", 7, mealIndex);
//                        }
//                    }
//
//                    i = lookForSpecialWords(mealParts, i, mealIndex);
//                    addIfMiniMealInside(mealParts[i], mealIndex);
//                }
//                updateMealName(meal, mealIndex);
//            }
//        }
//    }
//
//    public int lookForSpecialWords(String[] mealParts, int i, int mealIndex){
//        String previousIngredient, middleIngredient, nextIngredient;
//        int combo = 1;
//
//        if(mealParts[i].equals("oil") || mealParts[i].equals("powder")){
//            nextIngredient = mealParts[i - 1] + " " + mealParts[i];
//            removeIfNeeded(mealParts[i - 1], 1, mealIndex);
//            addIfNeeded(nextIngredient, mealIndex);
//            i += combo;
//            return i;
//        }
//
//        if(mealParts[i].equals("ice") || foodCompanies.contains(mealParts[i])){
//            previousIngredient = mealParts[i] + " " + mealParts[i + 1];
//            addIfNeeded(previousIngredient, mealIndex);
//            i += combo;
//            return i;
//        }
//
//        if (mealParts[i].equals("flavored")){
//            middleIngredient = mealParts[i - 1] + " " + mealParts[i] + " " + mealParts[i + 1];
//            if(mealParts[i + 1].equals("ice") || foodCompanies.contains(mealParts[i])){
//                middleIngredient += " " + mealParts[i + 2];
//                combo++;
//            }
//            addIfNeeded(middleIngredient, mealIndex);
//            removeIfNeeded(mealParts[i - 1], 1, mealIndex);
//            i += combo;
//            return i;
//        }
//        return i;
//    }
//
//    public void addIfMiniMealInside(String mealPart, int mealIndex){
//        if (mealPart.equals("toast")) {
//            addIfNeeded("bread", mealIndex);
//            addIfNeeded("yellow cheese", mealIndex);
//            addIfNeeded("ketchup", mealIndex);
//            addIfNeeded("thousand island dressing", mealIndex);
//        }
//        if (mealPart.equals("salad")) {
//            addIfNeeded("tomato", mealIndex);
//            addIfNeeded("cucumber", mealIndex);
//            addIfNeeded("lettuce", mealIndex);
//        }
//    }
//
//    public void addExtra(String ingredient, int amount, int mealIndex){
//        if(mealIndex == 0){
//            int index = breakfastIngredientsList.indexOf(ingredient);
//            breakfastIngredientsAmount.add(index, breakfastIngredientsAmount.get(index) + amount);
//        }
//
//        if(mealIndex == 1){
//            int index = lunchIngredientsList.indexOf(ingredient);
//            lunchIngredientsAmount.add(index, lunchIngredientsAmount.get(index) + amount);
//        }
//
//        if(mealIndex == 2){
//            int index = dinnerIngredientsList.indexOf(ingredient);
//            dinnerIngredientsAmount.add(index, dinnerIngredientsAmount.get(index) + amount);
//        }
//    }
//
//    public void addIfNeeded(String selectedIngredient, int mealIndex){
//        Ingredient ingredient = Ingredient.getIngredientByName(selectedIngredient);
//        if(mealIndex == 0){
//            if(!breakfastIngredientsList.contains(ingredient)){
//                breakfastIngredientsList.add(ingredient);
//                breakfastIngredientsAmount.add(1);
//            }
//            else{
//                int index = breakfastIngredientsList.indexOf(ingredient);
//                breakfastIngredientsAmount.add(index, breakfastIngredientsAmount.get(index) + 1);
//            }
//        }
//
//        if(mealIndex == 1){
//            if(!lunchIngredientsList.contains(ingredient)){
//                lunchIngredientsList.add(ingredient);
//                lunchIngredientsAmount.add(1);
//            }
//            else{
//                int index = lunchIngredientsList.indexOf(ingredient);
//                lunchIngredientsAmount.add(index, lunchIngredientsAmount.get(index) + 1);
//            }
//        }
//
//        if(mealIndex == 2){
//            if(!dinnerIngredientsList.contains(ingredient)){
//                dinnerIngredientsList.add(ingredient);
//                dinnerIngredientsAmount.add(1);
//            }
//            else{
//                int index = dinnerIngredientsList.indexOf(ingredient);
//                dinnerIngredientsAmount.add(index, dinnerIngredientsAmount.get(index) + 1);
//            }
//        }
//    }
//
//    public void removeIfNeeded(String ingredient, int amountToRemove, int mealIndex){
//        if(mealIndex == 0){
//            int index = breakfastIngredientsList.indexOf(ingredient);
//            if(breakfastIngredientsAmount.get(index) == amountToRemove){
//                breakfastIngredientsList.remove(index);
//                breakfastIngredientsAmount.remove(index);
//            }
//            else{
//                breakfastIngredientsAmount.add(index, breakfastIngredientsAmount.get(index) - amountToRemove);
//            }
//        }
//
//        if(mealIndex == 1){
//            int index = lunchIngredientsList.indexOf(ingredient);
//            if(lunchIngredientsAmount.get(index) == amountToRemove){
//                lunchIngredientsList.remove(index);
//                lunchIngredientsAmount.remove(index);
//            }
//            else{
//                lunchIngredientsAmount.add(index, lunchIngredientsAmount.get(index) - amountToRemove);
//            }
//        }
//
//        if(mealIndex == 2){
//            int index = dinnerIngredientsList.indexOf(ingredient);
//            if(dinnerIngredientsAmount.get(index) == amountToRemove){
//                dinnerIngredientsList.remove(index);
//                dinnerIngredientsAmount.remove(index);
//            }
//            else{
//                dinnerIngredientsAmount.add(index, dinnerIngredientsAmount.get(index) - amountToRemove);
//            }
//        }
//    }


        // From Meal:

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


        // From app-wild:

//    @Override
//    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
//        int itemID = item.getItemId();
//        if(itemID == R.id.musicController){
//            if(mediaPlayer.isPlaying()){
//                me.putExtra("playMusic", false);
//                item.setIcon(R.drawable.ic_music_off_icon);
//                mediaPlayer.pause();
//            }
//            else{
//                me.putExtra("playMusic", true);
//                item.setIcon(R.drawable.ic_music_on_icon);
//                initiateMediaPlayer();
//                mediaPlayer.start();
//            }
//        }
//
//        if(itemID == R.id.sendToSettings){
//            me.setClass(MainActivity.this, settingsSetter.class);
//            me.putExtra("cameToSettingsFrom", getLocalClassName());
//            startActivity(me);
//        }
//        return super.onOptionsItemSelected(item);
//    }


        // From: ingredientsSelection:

    //    public void initiateListViewFields(){
//        ingredientsListViewInfo = new ArrayList<String>();
//
//        for(int i = 0; i < ingredients.size(); i++){
//            Ingredient ingredient = ingredients.get(i);
//            ingredientsListViewInfo.add(ingredient.getName());  //  + ": " + ingredient.getProteins() * 100.0 + " proteins, " + ingredient.getFats() * 100.0 + " fats and " + ingredient.getCalories() + " calories."
//        }
//    }

        // From: customMeals:


//    public void writeTheInstructions(){
//        tvInstructions.setText(
//                "Make sure to use words like: 'with', 'and' and 'include' instead of using ','." + "\n" +
//                "You need to use the special word 'flavored' to make something flavored, like this: " + "\n" +
//                "chocolate flavored ice cream or chocolate flavored yogurt and so on..." + "\n" +
//                "You can also use 'mini-meals' like: salad, toast and cereals in your text." + "\n" +
//                "Not every ingredient have photo so it will just show you the name."
//        );
//    }

//    public void setCustomFood(){
//        String meal = customMeal.getText().toString();
//        String testMeal;
//        if(meal.contains(":") && meal.contains(",") && meal.contains(".") && meal.contains(" ")){
//            testMeal = meal.replaceAll(":", "");
//            if(meal.length() != testMeal.length() + 1){
//                Toast.makeText(this, "There should be one ':' .", Toast.LENGTH_SHORT).show();
//            }
//            else{
//                testMeal = meal.replaceAll(",", "");
//                if(meal.length() != testMeal.length() + 1){
//                    Toast.makeText(this, "There should be one ',' .", Toast.LENGTH_SHORT).show();
//                }
//                else{
//                    testMeal = meal.replaceAll("\\.", "");
//                    if(meal.length() != testMeal.length() + 1){
//                        Toast.makeText(this, "There should be one '.' .", Toast.LENGTH_SHORT).show();
//                    }
//                    else{
//                        if(meal.contains("_") || meal.contains("-")){
//                            Toast.makeText(this, "Make sure to use only the format symbols.", Toast.LENGTH_SHORT).show();
//                        }
//                        else{
//                            viewInfo();
//                        }
//                    }
//                }
//            }
//        }
//        else{
//            Toast.makeText(this, "Make sure to follow the format instructions.", Toast.LENGTH_SHORT).show();
//        }
//    }
}