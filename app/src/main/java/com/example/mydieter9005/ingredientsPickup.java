package com.example.mydieter9005;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class ingredientsPickup extends AppCompatActivity {

    TextView tvFoodName, tvFoodAmount;
    ImageView foodImg;
    Button btNext;
    String[] meals, mealParts, ingredientParts;
    ArrayList<String> ingredients, finalIngredients;
    ArrayList<Integer> foodImages;
    int[] amount;
    int counter = 0, ingredient_counter = 0, ingredient_amount = 0;
    Intent me;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingredients_pickup);

        me = getIntent();
        meals = me.getStringArrayExtra("meals");

        ingredients = new ArrayList<String>();
        finalIngredients = new ArrayList<String>();
        foodImages = new ArrayList<Integer>();

        ingredients.add("tomato");
        ingredients.add("cucumber");
        ingredients.add("lettuce");
        ingredients.add("parsley");
        ingredients.add("olive");
        ingredients.add("corn");

        ingredients.add("milk");
        ingredients.add("yogurt");
        ingredients.add("chocolate flavored yogurt");
        ingredients.add("cheese");
        ingredients.add("yellow cheese");
        ingredients.add("chocolate");

        ingredients.add("egg");
        ingredients.add("patit");
        ingredients.add("bread");
        ingredients.add("cereals");

        ingredients.add("ketchup");
        ingredients.add("thousand island dressing");

        ingredients.add("olive oil");
        ingredients.add("canola oil");
        ingredients.add("sugar");
        ingredients.add("rice");
        ingredients.add("pasta");
        ingredients.add("potato");
        ingredients.add("flour");
        ingredients.add("tehina");
        ingredients.add("baking soda powder");
        ingredients.add("breadcrumbs");
        ingredients.add("mayonnaise");
        ingredients.add("honey");
        ingredients.add("lemon");
        ingredients.add("cocoa powder");
        ingredients.add("cafe powder");
        ingredients.add("mushroom");
        ingredients.add("cauliflower");
        ingredients.add("dwarf corn");
        ingredients.add("chocolate flavored ice cream");
        ingredients.add("strawberry");
        ingredients.add("avocado");


        initiateAmountCounter();
        initiateIngredientsPictures();
        initiateMealsRecipes();

        tvFoodName = (TextView) findViewById(R.id.tvFoodName);
        tvFoodAmount = (TextView) findViewById(R.id.tvFoodAmount);
        foodImg = (ImageView) findViewById(R.id.foodImg);
        btNext = (Button) findViewById(R.id.btNext);

        btNext.setText("Next" + "\n" + "Item: " + ingredient_counter + " out of " + ingredient_amount);
    }

    public void initiateAmountCounter(){
        amount = new int[ingredients.size()];
        for(int i = 0; i < ingredients.size(); i++){
            amount[i] = 0;
        }
    }

    public void initiateIngredientsPictures(){
        String customIngredient;
        for(String ingredient : ingredients) {
            if(ingredient.contains(" ")){
                customIngredient = ingredient.replaceAll(" ", "_");;
                foodImages.add(ingredients.indexOf(ingredient), getResources().getIdentifier(customIngredient, "drawable", getPackageName()));
            }
            else{
                if(ingredients.contains(ingredient)) {
                    foodImages.add(ingredients.indexOf(ingredient), getResources().getIdentifier(ingredient, "drawable", getPackageName()));
                }
            }
        }
    }

    public void initiateMealsRecipes(){
        for(String meal : meals){
            if(meal != null){
                mealParts = meal.split(" ", 100);
                mealParts[0] = mealParts[0].toLowerCase();
                for(int i = 0; i < mealParts.length; i++) {
                    mealParts[i] = mealParts[i].replaceAll(" ", "");

                    if (ingredients.contains(mealParts[i])) {
                        addIfNeeded(mealParts[i]);
                        if (mealParts[i].equals("olive")) {
                            amount[ingredients.indexOf("olive")] += 7;
                        }
                    }

                    i = lookForSpecialWords(mealParts, i);
                    addIfMiniMealInside(mealParts[i]);
                }
            }
        }
    }

    public int lookForSpecialWords(String[] mealParts, int i){
        String previousIngredient, middleIngredient, nextIngredient;
        int combo = 1;

        if(mealParts[i].equals("oil") || mealParts[i].equals("powder")){
            nextIngredient = mealParts[i - 1] + " " + mealParts[i];
            removeIfNeeded(mealParts[i - 1], 1);
            addIfNeeded(nextIngredient);
            i += combo;
            return i;
        }

        if(mealParts[i].equals("ice")){
            previousIngredient = mealParts[i] + " " + mealParts[i + 1];
            addIfNeeded(previousIngredient);
            i += combo;
            return i;
        }

        if (mealParts[i].equals("flavored")){
            middleIngredient = mealParts[i - 1] + " " + mealParts[i] + " " + mealParts[i + 1];
            if(mealParts[i + 1].equals("ice")){
                middleIngredient += " " + mealParts[i + 2];
                combo++;
            }
            addIfNeeded(middleIngredient);
            removeIfNeeded(mealParts[i - 1], 1);
            i += combo;
            return i;
        }
        return i;
    }

    public void addIfMiniMealInside(String mealPart){
        if (mealPart.equals("toast")) {
            addIfNeeded("bread");
            addIfNeeded("yellow cheese");
            addIfNeeded("ketchup");
            addIfNeeded("thousand island dressing");
        }
        if (mealPart.equals("cereals")) {
            addIfNeeded("milk");
        }
        if (mealPart.equals("salad")) {
            addIfNeeded("tomato");
            addIfNeeded("cucumber");
            addIfNeeded("lettuce");
        }
    }

    public void nextItem(View v){
        if(btNext.getText() == "Finish"){
            finish(v);
        }
        if(counter < ingredients.size()){
            String ingredient = finalIngredients.get(counter);
            int index = ingredients.indexOf(ingredient);
            if(amount[index] >= 1){
                tvFoodName.setText("Name: " + ingredient);
                tvFoodAmount.setText("Amount: " + amount[index]);
                foodImg.setImageResource(foodImages.get(index));
                ingredient_counter += 1;
                btNext.setText("Next" + "\n" + "Item: " + ingredient_counter + " out of " + ingredient_amount);
            }
            counter += 1;
            if(ingredient_counter == ingredient_amount){
                btNext.setText("Finish");
            }
        }
    }

    public void addIfNeeded(String ingredient){
        if(amount[ingredients.indexOf(ingredient)] == 0){
            finalIngredients.add(ingredient);
            ingredient_amount += 1;
        }
        amount[ingredients.indexOf(ingredient)] += 1;
    }

    public void removeIfNeeded(String ingredient, int amountToRemove){
        if(amount[ingredients.indexOf(ingredient)] == amountToRemove){
            finalIngredients.remove(ingredient);
            ingredient_amount -= 1;
        }
        else{
            amount[ingredients.indexOf(ingredient)] -= amountToRemove;
        }
    }

    public void finish(View v){
        me.setClass(this, finishMeals.class);
        me.putExtra("ingredients", ingredients);
        startActivity(me);
    }
}