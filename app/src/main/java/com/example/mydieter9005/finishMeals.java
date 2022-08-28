package com.example.mydieter9005;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class finishMeals extends AppCompatActivity {

    TextView tvBreakfastInfo, tvLunchInfo, tvDinnerInfo;
    ListView breakfastIngredients, lunchIngredients, dinnerIngredients;
    String[] meals, mealParts;
    ArrayList<String> ingredients;
    ArrayList<String> breakfastIngredientsList, lunchIngredientsList, dinnerIngredientsList;
    ArrayList<Integer> breakfastIngredientsAmount, lunchIngredientsAmount, dinnerIngredientsAmount;
    ArrayAdapter<String> breakfastIngredientsAdapter, lunchIngredientsAdapter, dinnerIngredientsAdapter;
    Intent me;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finish_meals);

        me = getIntent();
        meals = me.getStringArrayExtra("meals");
        ingredients = me.getStringArrayListExtra("ingredients");

        breakfastIngredientsList = new ArrayList<String>();
        breakfastIngredientsAmount = new ArrayList<Integer>();
        lunchIngredientsList = new ArrayList<String>();
        lunchIngredientsAmount = new ArrayList<Integer>();
        dinnerIngredientsList = new ArrayList<String>();
        dinnerIngredientsAmount = new ArrayList<Integer>();

        tvBreakfastInfo = (TextView) findViewById(R.id.tvBreakfastInfo);
        tvBreakfastInfo.setMovementMethod(new ScrollingMovementMethod());
        tvLunchInfo = (TextView) findViewById(R.id.tvLunchInfo);
        tvLunchInfo.setMovementMethod(new ScrollingMovementMethod());
        tvDinnerInfo = (TextView) findViewById(R.id.tvDinnerInfo);
        tvDinnerInfo.setMovementMethod(new ScrollingMovementMethod());

        breakfastIngredients = (ListView) findViewById(R.id.breakfastIngredients);
        lunchIngredients = (ListView) findViewById(R.id.lunchIngredients);
        dinnerIngredients = (ListView) findViewById(R.id.dinnerIngredients);

        initiateMealsRecipes();
        setAdapters();
    }

    public void initiateMealsRecipes(){
        for(int mealIndex = 0; mealIndex < meals.length; mealIndex++) {
            String meal = meals[mealIndex];
            if (meal != null) {
                mealParts = meal.split(" ", 100);
                mealParts[0] = mealParts[0].toLowerCase();
                for (int i = 0; i < mealParts.length; i++) {
                    mealParts[i] = mealParts[i].replaceAll(" ", "");

                    if (ingredients.contains(mealParts[i])) {
                        addIfNeeded(mealParts[i], mealIndex);
                        if (mealParts[i].equals("olive")) {
                            addExtra("olive", 7, mealIndex);
                        }
                    }

                    i = lookForSpecialWords(mealParts, i, mealIndex);
                    addIfMiniMealInside(mealParts[i], mealIndex);
                }
                updateMealName(meal, mealIndex);
            }
        }
    }

    public int lookForSpecialWords(String[] mealParts, int i, int mealIndex){
        String previousIngredient, middleIngredient, nextIngredient;
        int combo = 1;

        if(mealParts[i].equals("oil") || mealParts[i].equals("powder")){
            nextIngredient = mealParts[i - 1] + " " + mealParts[i];
            removeIfNeeded(mealParts[i - 1], 1, mealIndex);
            addIfNeeded(nextIngredient, mealIndex);
            i += combo;
            return i;
        }

        if(mealParts[i].equals("ice")){
            previousIngredient = mealParts[i] + " " + mealParts[i + 1];
            addIfNeeded(previousIngredient, mealIndex);
            i += combo;
            return i;
        }

        if (mealParts[i].equals("flavored")){
            middleIngredient = mealParts[i - 1] + " " + mealParts[i] + " " + mealParts[i + 1];
            if(mealParts[i + 1].equals("ice")){
                middleIngredient += mealParts[i + 2];
                combo++;
            }
            addIfNeeded(middleIngredient, mealIndex);
            removeIfNeeded(mealParts[i - 1], 1, mealIndex);
            i += combo;
            return i;
        }
        return i;
    }

    public void addIfMiniMealInside(String mealPart, int mealIndex){
        if (mealPart.equals("toast")) {
            addIfNeeded("bread", mealIndex);
            addIfNeeded("yellow cheese", mealIndex);
            addIfNeeded("ketchup", mealIndex);
            addIfNeeded("thousand island dressing", mealIndex);
        }
        if (mealPart.equals("cereals")) {
            addIfNeeded("milk", mealIndex);
        }
        if (mealPart.equals("salad")) {
            addIfNeeded("tomato", mealIndex);
            addIfNeeded("cucumber", mealIndex);
            addIfNeeded("lettuce", mealIndex);
        }
    }

    public void addExtra(String ingredient, int amount, int mealIndex){
        if(mealIndex == 0){
            int index = breakfastIngredientsList.indexOf(ingredient);
            breakfastIngredientsAmount.add(index, breakfastIngredientsAmount.get(index) + amount);
        }

        if(mealIndex == 1){
            int index = lunchIngredientsList.indexOf(ingredient);
            lunchIngredientsAmount.add(index, lunchIngredientsAmount.get(index) + amount);
        }

        if(mealIndex == 2){
            int index = dinnerIngredientsList.indexOf(ingredient);
            dinnerIngredientsAmount.add(index, dinnerIngredientsAmount.get(index) + amount);
        }
    }

    public void addIfNeeded(String ingredient, int mealIndex){
        if(mealIndex == 0){
            if(!breakfastIngredientsList.contains(ingredient)){
                breakfastIngredientsList.add(ingredient);
                breakfastIngredientsAmount.add(1);
            }
            else{
                int index = breakfastIngredientsList.indexOf(ingredient);
                breakfastIngredientsAmount.add(index, breakfastIngredientsAmount.get(index) + 1);
            }
        }

        if(mealIndex == 1){
            if(!lunchIngredientsList.contains(ingredient)){
                lunchIngredientsList.add(ingredient);
                lunchIngredientsAmount.add(1);
            }
            else{
                int index = lunchIngredientsList.indexOf(ingredient);
                lunchIngredientsAmount.add(index, lunchIngredientsAmount.get(index) + 1);
            }
        }

        if(mealIndex == 2){
            if(!dinnerIngredientsList.contains(ingredient)){
                dinnerIngredientsList.add(ingredient);
                dinnerIngredientsAmount.add(1);
            }
            else{
                int index = dinnerIngredientsList.indexOf(ingredient);
                dinnerIngredientsAmount.add(index, dinnerIngredientsAmount.get(index) + 1);
            }
        }
    }

    public void removeIfNeeded(String ingredient, int amountToRemove, int mealIndex){
        if(mealIndex == 0){
            int index = breakfastIngredientsList.indexOf(ingredient);
            if(breakfastIngredientsAmount.get(index) == amountToRemove){
                breakfastIngredientsList.remove(index);
                breakfastIngredientsAmount.remove(index);
            }
            else{
                breakfastIngredientsAmount.add(index, breakfastIngredientsAmount.get(index) - amountToRemove);
            }
        }

        if(mealIndex == 1){
            int index = lunchIngredientsList.indexOf(ingredient);
            if(lunchIngredientsAmount.get(index) == amountToRemove){
                lunchIngredientsList.remove(index);
                lunchIngredientsAmount.remove(index);
            }
            else{
                lunchIngredientsAmount.add(index, lunchIngredientsAmount.get(index) - amountToRemove);
            }
        }

        if(mealIndex == 2){
            int index = dinnerIngredientsList.indexOf(ingredient);
            if(dinnerIngredientsAmount.get(index) == amountToRemove){
                dinnerIngredientsList.remove(index);
                dinnerIngredientsAmount.remove(index);
            }
            else{
                dinnerIngredientsAmount.add(index, dinnerIngredientsAmount.get(index) - amountToRemove);
            }
        }
    }

    public void updateMealName(String mealName, int mealIndex){
        if(mealIndex == 0){
            tvBreakfastInfo.setText("Breakfast: " + mealName);
        }

        if(mealIndex == 1){
            tvLunchInfo.setText("Lunch: " + mealName);
        }

        if(mealIndex == 2){
            tvDinnerInfo.setText("Dinner: " + mealName);
        }
    }

    public void setAdapters(){
        for(int mealIndex = 0; mealIndex < meals.length; mealIndex++) {
            String meal = meals[mealIndex];
            if (meal != null) {
                if(mealIndex == 0){
                    for(int i = 0; i < breakfastIngredientsList.size(); i++){
                        String text = breakfastIngredientsList.get(i) + " X " + breakfastIngredientsAmount.get(i);
                        breakfastIngredientsList.set(i, text);
                    }
                    breakfastIngredientsAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, breakfastIngredientsList);
                    breakfastIngredients.setAdapter(breakfastIngredientsAdapter);
                }

                if(mealIndex == 1){
                    for(int i = 0; i < lunchIngredientsList.size(); i++){
                        String text = lunchIngredientsList.get(i) + " X " + lunchIngredientsAmount.get(i);
                        lunchIngredientsList.set(i, text);
                    }
                    lunchIngredientsAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, lunchIngredientsList);
                    lunchIngredients.setAdapter(lunchIngredientsAdapter);
                }

                if(mealIndex == 2){
                    for(int i = 0; i < dinnerIngredientsList.size(); i++){
                        String text = dinnerIngredientsList.get(i) + " X " + dinnerIngredientsAmount.get(i);
                        dinnerIngredientsList.set(i, text);
                    }
                    dinnerIngredientsAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, dinnerIngredientsList);
                    dinnerIngredients.setAdapter(dinnerIngredientsAdapter);
                }
            }
        }
    }

    public void finish(View v){
        me.setClass(this, MainActivity.class);
        startActivity(me);
    }
}