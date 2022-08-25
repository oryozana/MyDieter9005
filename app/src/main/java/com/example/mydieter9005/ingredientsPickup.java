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
    String[] meals, mealParts;
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
        ingredients.add("cheese");
        ingredients.add("yellow cheese");

        ingredients.add("egg");
        ingredients.add("patit");
        ingredients.add("bread");
        ingredients.add("cereals");

        ingredients.add("ketchup");
        ingredients.add("thousand island dressing");

        amount = new int[ingredients.size()];
        for(int i = 0; i < ingredients.size(); i++){
            amount[i] = 0;
        }

        boolean custom = false;
        for(String ingredient : ingredients) {
            if(ingredient.equals("yellow cheese")){
                custom = true;
                foodImages.add(ingredients.indexOf(ingredient), getResources().getIdentifier("yellow_cheese", "drawable", getPackageName()));
            }
            if(ingredient.equals("thousand island dressing")){
                custom = true;
                foodImages.add(ingredients.indexOf(ingredient), getResources().getIdentifier("thousand_island_dressing", "drawable", getPackageName()));
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

        for(String meal : meals){
            if(meal != null){
                Toast.makeText(this, meal, Toast.LENGTH_SHORT).show();
                mealParts = meal.split(" ", 100);
                mealParts[0] = mealParts[0].toLowerCase();
                for(int i = 0; i < mealParts.length; i++) {
                    mealParts[i] = mealParts[i].replaceAll(" ", "");

                    if (ingredients.contains(mealParts[i])) {
                        add_if_needed(mealParts[i]);
                        if (mealParts[i].equals("olive")) {
                            amount[ingredients.indexOf("olive")] += 7;
                        }
                        amount[ingredients.indexOf(mealParts[i])] += 1;
                    }

                    if (mealParts[i].equals("toast")) {
                        add_if_needed("bread");
                        amount[ingredients.indexOf("bread")] += 1;
                        add_if_needed("yellow cheese");
                        amount[ingredients.indexOf("yellow cheese")] += 1;
                        add_if_needed("ketchup");
                        amount[ingredients.indexOf("ketchup")] += 1;
                        add_if_needed("thousand island dressing");
                        amount[ingredients.indexOf("thousand island dressing")] += 1;
                    }
                    if (mealParts[i].equals("cereals")) {
                        add_if_needed("milk");
                        amount[ingredients.indexOf("milk")] += 1;
                    }
                    if (mealParts[i].equals("salad")) {
                        add_if_needed("tomato");
                        amount[ingredients.indexOf("tomato")] += 1;
                        add_if_needed("cucumber");
                        amount[ingredients.indexOf("cucumber")] += 1;
                        add_if_needed("lettuce");
                        amount[ingredients.indexOf("lettuce")] += 1;
                    }
                }
            }
        }

        tvFoodName = (TextView) findViewById(R.id.tvFoodName);
        tvFoodAmount = (TextView) findViewById(R.id.tvFoodAmount);
        foodImg = (ImageView) findViewById(R.id.foodImg);
        btNext = (Button) findViewById(R.id.btNext);

        btNext.setText("Next" + "\n" + "Item: " + ingredient_counter + " out of " + ingredient_amount);
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

    public void finish(View v){
        me.setClass(this, finishMeals.class);
        me.putExtra("ingredients", ingredients);
        startActivity(me);
    }

    public void add_if_needed(String ingredient){
        if(amount[ingredients.indexOf(ingredient)] == 0){
            finalIngredients.add(ingredient);
            ingredient_amount += 1;
        }
    }
}