package com.example.mydieter9005;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

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

}