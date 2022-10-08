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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class ingredientsPickup extends AppCompatActivity {

    private MediaPlayer mediaPlayer;

    TextView tvFoodName, tvFoodGrams, tvFoodAmount, tvCounterShow;
    ImageView ivFoodImg;
    ImageButton ibtNext, ibtPrevious;
    Button btFinishIngredientsPickup;
    Meal[] selectedMeals = new Meal[3];
    ArrayList<Ingredient> ingredients, finalIngredients;
    int ingredientsCounter = 0, ingredientsAmount = 0;
    ArrayList<Ingredient> ingredientsToShow;
    String lastClicked = "next";

    FileInputStream is;
    InputStreamReader isr;
    BufferedReader br;
    Intent me;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingredients_pickup);

        me = getIntent();
        selectedMeals[0] = (Meal) me.getSerializableExtra("selectedBreakfast");
        selectedMeals[1] = (Meal) me.getSerializableExtra("selectedLunch");
        selectedMeals[2] = (Meal) me.getSerializableExtra("selectedDinner");

        ingredients = Ingredient.getIngredientsList();  // All the ingredients that inside the app.
        finalIngredients = new ArrayList<Ingredient>();  // The needed ingredients to make the meals.

        tvCounterShow = (TextView) findViewById(R.id.tvCounterShow);
        tvFoodName = (TextView) findViewById(R.id.tvFoodName);
        tvFoodGrams = (TextView) findViewById(R.id.tvFoodGrams);
        tvFoodAmount = (TextView) findViewById(R.id.tvFoodAmount);

        ivFoodImg = (ImageView) findViewById(R.id.ivFoodImg);

        btFinishIngredientsPickup = (Button) findViewById(R.id.btFinishIngredientsPickup);
        ibtPrevious = (ImageButton) findViewById(R.id.ibtPrevious);
        ibtNext = (ImageButton) findViewById(R.id.ibtNext);

        implementSettingsData();
        initiateIngredientsToShow();
        initiateMediaPlayer();
    }

    public int getIngredientIndexInArrayList(Ingredient ingredient, ArrayList<Ingredient> ingredientsArrayList){
        for(int i = 0; i < ingredientsArrayList.size(); i++){
            if(ingredientsArrayList.get(i).getName().equals(ingredient.getName()))
                return i;
        }
        return -1;
    }

    public void initiateIngredientsToShow(){
        ingredientsToShow = new ArrayList<Ingredient>();
        for(Meal meal : selectedMeals) {
            if(meal != null){
                for(int i = 0; i < meal.getNeededIngredientsForMeal().size(); i++) {
                    Ingredient ingredient = new Ingredient(meal.getNeededIngredientsForMeal().get(i));

                    if(getIngredientIndexInArrayList(ingredient, ingredientsToShow) == -1)
                        ingredientsToShow.add(new Ingredient(ingredient));
                    else
                        ingredientsToShow.get(getIngredientIndexInArrayList(ingredient, ingredientsToShow)).addGrams(ingredient.getGrams());
                }
            }
        }
        ingredientsAmount = ingredientsToShow.size();

        tvCounterShow.setText("Item: " + ingredientsCounter + " out of " + ingredientsAmount);
        if(ingredientsAmount == 0)
            ibtNext.setVisibility(View.INVISIBLE);
    }

    public void nextItem(View v){
        if(ingredientsCounter + 1 == ingredientsAmount)
            ibtNext.setVisibility(View.INVISIBLE);
        if(ibtPrevious.getVisibility() == View.INVISIBLE && ingredientsCounter != 0)
            ibtPrevious.setVisibility(View.VISIBLE);

        ingredientsCounter++;
        setIngredientInfo();

        lastClicked = "next";
        tvCounterShow.setText("Item: " + ingredientsCounter + " out of " + ingredientsAmount);
    }

    public void previousItem(View v){
        if(ingredientsCounter == 2)
            ibtPrevious.setVisibility(View.INVISIBLE);
        if(ibtNext.getVisibility() == View.INVISIBLE)
            ibtNext.setVisibility(View.VISIBLE);

        ingredientsCounter--;
        setIngredientInfo();

        lastClicked = "previous";
        tvCounterShow.setText("Item: " + ingredientsCounter + " out of " + ingredientsAmount);
    }

    public void setIngredientInfo(){
        Ingredient ingredient = ingredientsToShow.get(ingredientsCounter - 1);
        tvFoodName.setText("Name: " + ingredient.getName());
        tvFoodGrams.setText("Grams: " + ingredient.getGrams());
        tvFoodAmount.setText("Amount: " + ingredient.getAmount());

        if(getIngredientIndexInArrayList(ingredient, ingredients) != -1)
            ivFoodImg.setImageResource(ingredient.getImgId());
        else
            ivFoodImg.setImageResource(R.drawable.image_not_available);
    }

    public void finish(View v){
        me.setClass(this, finishMeals.class);
        startActivity(me);
    }

    public String getFileData(String fileName){
        String currentLine = "", allData = "";
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
            if(fileName.equals(me.getStringExtra("todayDate")))
                Toast.makeText(this, "Today saved data not exists yet.", Toast.LENGTH_SHORT).show();
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
            me.putExtra("cameToSettingsFrom", getLocalClassName());
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