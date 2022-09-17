package com.example.mydieter9005;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
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

    FileInputStream is;
    InputStreamReader isr;
    BufferedReader br;
    String currentLine, allData;
    Intent me;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingredients_pickup);

        me = getIntent();
        meals = me.getStringArrayExtra("meals");

        foodCompanies = new ArrayList<String>();  // Food common companies names list.
        ingredients = new ArrayList<String>();  // All the ingredients that inside the app.
        finalIngredients = new ArrayList<String>();  // The needed ingredients to make the meals.
        foodImages = new ArrayList<Integer>();  // All the ingredients pictures IDs.

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
        ingredients.add("garlic");
        ingredients.add("red onion");

        // Milky ingredients:
        ingredients.add("milk");
        ingredients.add("yogurt");
        ingredients.add("chocolate flavored yogurt");
        ingredients.add("cheese");
        ingredients.add("yellow cheese");
        ingredients.add("chocolate");
        ingredients.add("chocolate flavored ice cream");
        ingredients.add("butter");
        ingredients.add("cheddar");

        // Parve ingredients:
        ingredients.add("egg");
        ingredients.add("patit");
        ingredients.add("bread");
        ingredients.add("nestle cereals");
        ingredients.add("honey");
        ingredients.add("rice");
        ingredients.add("pasta");
        ingredients.add("breadcrumbs");
        ingredients.add("flour");
        ingredients.add("sugar");
        ingredients.add("brown sugar");
        ingredients.add("spaghetti");
        ingredients.add("peanut butter");
        ingredients.add("cumin");

        // Fleshy ingredients:
        ingredients.add("chicken breast");

        // Powders:
        ingredients.add("baking soda powder");
        ingredients.add("cocoa powder");
        ingredients.add("cafe powder");
        ingredients.add("sweet paprika powder");
        ingredients.add("garlic powder");
        ingredients.add("cinnamon powder");
        ingredients.add("baking powder");
        ingredients.add("oregano powder");
        ingredients.add("paprika powder");
        ingredients.add("vanilla powder");
        ingredients.add("chili powder");

        // Sauces:
        ingredients.add("ketchup");
        ingredients.add("tehina");
        ingredients.add("mayonnaise");
        ingredients.add("thousand island dressing");
        ingredients.add("soy sauce");
        ingredients.add("mustard sauce");

        // Oils:
        ingredients.add("olive oil");
        ingredients.add("canola oil");
        ingredients.add("vegetable oil");

        // Food companies:
        foodCompanies.add("nestle");


        ingredients.add("onion powder");
        ingredients.add("carrot");
        ingredients.add("basil");
        ingredients.add("parmesan");
        ingredients.add("italian seasoning");
        ingredients.add("thyme");
        ingredients.add("bell pepper");
        ingredients.add("scallion");
        ingredients.add("onion");
        ingredients.add("celery");
        ingredients.add("chili pepper");
        ingredients.add("canned tomato");
        ingredients.add("jalapeno");
        ingredients.add("zucchini");
        ingredients.add("shallot");
        ingredients.add("cherry tomato");
        ingredients.add("spinach");
        ingredients.add("sweat potato");
        ingredients.add("broccoli");
        ingredients.add("pumpkin");
        ingredients.add("baby greens");



























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

        initiateMediaPlayer();
        implementSettingsData();
    }

    public void initiateAmountCounter(){  // Represent all the ingredients amount.
        amount = new int[ingredients.size()];
        for(int i = 0; i < ingredients.size(); i++){
            amount[i] = 0;
        }
    }

    public void initiateIngredientsPictures(){  // Save all the ingredients pictures IDs.
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

    public void initiateMealsRecipes(){  // Separate the meals into ingredients.
        char firstChar, lastChar;
        String ingredient;
        for(String meal : meals){
            if(meal != null){
                mealParts = meal.split(" and | with | include ");
                for(int i = 0; i < mealParts.length; i++) {
                    mealParts[i] = mealParts[i].toLowerCase();

                    firstChar = mealParts[i].charAt(0);
                    lastChar = mealParts[i].charAt(mealParts[i].length() - 1);
                    ingredient = "";

                    for(int letter = 0; letter < mealParts[i].length(); letter++){
                        if(!(firstChar != ' ' && letter == 0) || !(lastChar != ' ' && letter == mealParts[i].length() - 1)){
                            ingredient += mealParts[i].charAt(letter);
                        }
                    }

                    if (ingredients.contains(ingredient)) {
                        addIfNeeded(ingredient);
                        if (ingredient.equals("olive")) {
                            amount[ingredients.indexOf("olive")] += 7;
                        }
                    }

                    addIfMiniMealInside(ingredient);
                }
            }
        }
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
            tvFoodName.setText("Name: " + ingredient);
            tvFoodAmount.setText("Amount: 1");

            if(index != -1){  // If item exists inside ingredients
                if(amount[index] >= 1){
                    tvFoodAmount.setText("Amount: " + amount[index]);
                    foodImg.setImageResource(foodImages.get(index));
                }
            }

            ingredient_counter += 1;
            btNext.setText("Next" + "\n" + "Item: " + ingredient_counter + " out of " + ingredient_amount);

            counter += 1;
            if(ingredient_counter == ingredient_amount){
                btNext.setText("Finish");
            }
        }
    }

    public void addIfNeeded(String ingredient){
        if(!finalIngredients.contains(ingredient)){
            finalIngredients.add(ingredient);
            ingredient_amount += 1;
        }
        amount[ingredients.indexOf(ingredient)] += 1;
    }

    public void finish(View v){
        me.setClass(this, finishMeals.class);
        me.putExtra("ingredients", ingredients);
        me.putExtra("foodCompanies", foodCompanies);
        startActivity(me);
    }

    public String getFileData(String fileName){
        try{
            is = openFileInput(fileName);
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

    public void implementSettingsData(){
        if(getFileData("settings") != null){
            String[] settingsParts = getFileData("settings").split("\n");
            Boolean playMusic, useVideos, useManuallySave;

            playMusic = Boolean.parseBoolean(settingsParts[0].split(": ")[1]);
            useVideos = Boolean.parseBoolean(settingsParts[1].split(": ")[1]);
            useManuallySave = Boolean.parseBoolean(settingsParts[2].split(": ")[1]);

            me.putExtra("playMusic", playMusic);
            me.putExtra("useVideos", useVideos);
            me.putExtra("useManuallySave", useManuallySave);
        }
    }

    public void initiateMediaPlayer(){
        mediaPlayer = MediaPlayer.create(ingredientsPickup.this, R.raw.my_song);
        mediaPlayer.setLooping(true);
        if(me.getBooleanExtra("playMusic", true)){
            mediaPlayer.start();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(@NonNull Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemID = item.getItemId();
        if(itemID == R.id.musicController){
            if(mediaPlayer.isPlaying()){
                me.putExtra("playMusic", false);
                item.setIcon(R.drawable.ic_music_off_icon);
                mediaPlayer.pause();
            }
            else{
                me.putExtra("playMusic", true);
                item.setIcon(R.drawable.ic_music_on_icon);
                initiateMediaPlayer();
                mediaPlayer.start();
            }
        }

        if(itemID == R.id.sendToSettings){
            me.setClass(ingredientsPickup.this, settingsSetter.class);
            startActivity(me);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mediaPlayer.start();
        if(!me.getBooleanExtra("playMusic", true)){
            mediaPlayer.stop();
        }
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