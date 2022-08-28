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
        boolean custom = false;
        for(String ingredient : ingredients) {
            ingredientParts = ingredient.split(" ", 100);
            if(ingredientParts.length != 1){
                customIngredient = "";
                custom = true;
                for(int i = 0; i < ingredientParts.length; i++){
                    if(i == 0){
                        customIngredient += ingredientParts[i];
                    }
                    else{
                        customIngredient += "_" + ingredientParts[i];
                    }
                }
                foodImages.add(ingredients.indexOf(ingredient), getResources().getIdentifier(customIngredient, "drawable", getPackageName()));
            }

            try {
                if(!custom){
                    foodImages.add(ingredients.indexOf(ingredient), getResources().getIdentifier(ingredient, "drawable", getPackageName()));
                }
            }
            catch (Exception e) {
                e.printStackTrace();
            }
            custom = false;
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

                    if (mealParts[i].equals("flavored")){
                        String flavoredIngredient = mealParts[i - 1] + " " + mealParts[i] + " " + mealParts[i + 1];
                        addIfNeeded(flavoredIngredient);
                        removeIfNeeded(mealParts[i - 1], 1);
                        i++;
                    }

                    if (mealParts[i].equals("toast")) {
                        addIfNeeded("bread");
                        addIfNeeded("yellow cheese");
                        addIfNeeded("ketchup");
                        addIfNeeded("thousand island dressing");
                    }
                    if (mealParts[i].equals("cereals")) {
                        addIfNeeded("milk");
                    }
                    if (mealParts[i].equals("salad")) {
                        addIfNeeded("tomato");
                        addIfNeeded("cucumber");
                        addIfNeeded("lettuce");
                    }
                }
            }
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