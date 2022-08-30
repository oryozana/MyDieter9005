package com.example.mydieter9005;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class ingredientsPickup extends AppCompatActivity {

    private MediaPlayer mediaPlayer;
    TextView tvFoodName, tvFoodAmount;
    ImageView foodImg;
    Button btNext;
    String[] meals, mealParts;
    ArrayList<String> ingredients, finalIngredients, foodCompanies;
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

        foodCompanies = new ArrayList<String>();
        ingredients = new ArrayList<String>();
        finalIngredients = new ArrayList<String>();
        foodImages = new ArrayList<Integer>();

        // Vegetables, fruits and mushrooms:
        ingredients.add("tomato");
        ingredients.add("cucumber");
        ingredients.add("lettuce");
        ingredients.add("parsley");
        ingredients.add("olive");
        ingredients.add("corn");
        ingredients.add("potato");
        ingredients.add("lemon");
        ingredients.add("mushroom");
        ingredients.add("cauliflower");
        ingredients.add("dwarf corn");
        ingredients.add("strawberry");
        ingredients.add("avocado");

        // Milky ingredients:
        ingredients.add("milk");
        ingredients.add("yogurt");
        ingredients.add("chocolate flavored yogurt");
        ingredients.add("cheese");
        ingredients.add("yellow cheese");
        ingredients.add("chocolate");
        ingredients.add("chocolate flavored ice cream");

        // Parve ingredients:
        ingredients.add("egg");
        ingredients.add("patit");
        ingredients.add("bread");
        ingredients.add("nestle cereals");
        ingredients.add("honey");
        ingredients.add("rice");
        ingredients.add("pasta");
        ingredients.add("breadcrumbs");
        ingredients.add("sugar");

        // Powders:
        ingredients.add("flour");
        ingredients.add("baking soda powder");
        ingredients.add("cocoa powder");
        ingredients.add("cafe powder");

        // Sauces:
        ingredients.add("ketchup");
        ingredients.add("tehina");
        ingredients.add("mayonnaise");
        ingredients.add("thousand island dressing");

        // Oils:
        ingredients.add("olive oil");
        ingredients.add("canola oil");

        // Food companies:
        foodCompanies.add("nestle");

        initiateAmountCounter();
        initiateIngredientsPictures();
        initiateMealsRecipes();

        tvFoodName = (TextView) findViewById(R.id.tvFoodName);
        tvFoodAmount = (TextView) findViewById(R.id.tvFoodAmount);
        foodImg = (ImageView) findViewById(R.id.foodImg);
        btNext = (Button) findViewById(R.id.btNext);

        if(ingredient_amount == 0){
            btNext.setText("Finish");
        }
        else{
            btNext.setText("Next" + "\n" + "Item: " + ingredient_counter + " out of " + ingredient_amount);
        }

        mediaPlayer = MediaPlayer.create(ingredientsPickup.this, R.raw.my_song);
        mediaPlayer.setLooping(true);
        mediaPlayer.start();
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
            if(ingredients.contains(ingredient)){
                if(ingredient.contains(" ")){
                    customIngredient = ingredient.replaceAll(" ", "_");
                    foodImages.add(ingredients.indexOf(ingredient), getResources().getIdentifier(customIngredient, "drawable", getPackageName()));
                }
                else{
                    foodImages.add(ingredients.indexOf(ingredient), getResources().getIdentifier(ingredient, "drawable", getPackageName()));
                }
            }
        }
    }

    public void initiateMealsRecipes(){
        for(String meal : meals){
            if(meal != null){
                mealParts = meal.split("and|with| ");
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
                    Toast.makeText(this, "make it", Toast.LENGTH_SHORT).show();
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

        if(mealParts[i].equals("ice") || foodCompanies.contains(mealParts[i])){
            previousIngredient = mealParts[i] + " " + mealParts[i + 1];
            addIfNeeded(previousIngredient);
            i += combo;
            return i;
        }

        if (mealParts[i].equals("flavored")){
            middleIngredient = mealParts[i - 1] + " " + mealParts[i] + " " + mealParts[i + 1];
            if(mealParts[i + 1].equals("ice") || foodCompanies.contains(mealParts[i])){
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
        me.putExtra("foodCompanies", foodCompanies);
        startActivity(me);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mediaPlayer.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mediaPlayer.pause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mediaPlayer.stop();
        mediaPlayer.release();
    }
}