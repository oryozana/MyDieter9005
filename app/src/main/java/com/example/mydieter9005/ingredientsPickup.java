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
import android.widget.Toast;

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
    Meal[] meals = new Meal[3];
    ArrayList<Ingredient> ingredients, finalIngredients;
    ArrayList<String> foodCompanies;
    int counter = 0, ingredient_counter = 0, ingredient_amount = 0;

    FileInputStream is;
    InputStreamReader isr;
    BufferedReader br;
    Intent me;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingredients_pickup);

        me = getIntent();
        meals[0] = new Meal(me.getStringArrayExtra("meals")[0]);
        meals[1] = new Meal(me.getStringArrayExtra("meals")[1]);
        meals[2] = new Meal(me.getStringArrayExtra("meals")[2]);

        foodCompanies = new ArrayList<String>();  // Food common companies names list.
        ingredients = Ingredient.getIngredientsList();  // All the ingredients that inside the app.
        finalIngredients = new ArrayList<Ingredient>();  // The needed ingredients to make the meals.

        // Food companies:
        foodCompanies.add("nestle");

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

    public void nextItem(View v){
        if(btNext.getText() == "Finish"){
            finish(v);
        }

        if(counter < ingredients.size()){
            String ingredient = finalIngredients.get(counter).getName();
            int index = ingredients.indexOf(Ingredient.getIngredientByName(ingredient));
            tvFoodName.setText("Name: " + ingredient);
            tvFoodAmount.setText("Amount: 1");

            if(index != -1){  // If item exists inside ingredients
                if(ingredients.get(index).getAmount() >= 1){
                    tvFoodAmount.setText("Amount: " + ingredients.get(index).getAmount());
                    foodImg.setImageResource(ingredients.get(index).getImgId());
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

    public void finish(View v){
        me.setClass(this, finishMeals.class);
        me.putExtra("foodCompanies", foodCompanies);
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