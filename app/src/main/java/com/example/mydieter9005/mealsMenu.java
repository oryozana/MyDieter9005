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
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

public class mealsMenu extends AppCompatActivity {

    private MediaPlayer mediaPlayer;
    TextView tvBreakfast, tvLunch, tvDinner;
    TextView tvTotalProteins, tvTotalFats, tvTotalCalories;
    Button btBreakfast, btLunch, btDinner, btFinish;

    Meal breakfast, lunch, dinner;
    Meal[] selectedMeals = new Meal[3];
    double totalProteins = 0, totalFats = 0, totalCalories = 0;

    FileInputStream is;
    InputStreamReader isr;
    BufferedReader br;
    Intent me;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meals_menu);

        me = getIntent();

        tvBreakfast = (TextView) findViewById(R.id.tvBreakfast);
        tvLunch = (TextView) findViewById(R.id.tvLunch);
        tvDinner = (TextView) findViewById(R.id.tvDinner);

        btBreakfast = (Button) findViewById(R.id.btBreakfast);
        btLunch = (Button) findViewById(R.id.btLunch);
        btDinner = (Button) findViewById(R.id.btDinner);
        btFinish = (Button) findViewById(R.id.btFinish);

        tvTotalProteins = (TextView) findViewById(R.id.tvTotalProteins);
        tvTotalFats = (TextView) findViewById(R.id.tvTotalFats);
        tvTotalCalories = (TextView) findViewById(R.id.tvTotalCalories);

        implementSettingsData();
        initiateMediaPlayer();
        updateMeals();
    }

    public void sendToMealSelection(View v){
        int id = v.getId();
        if(id == btBreakfast.getId()) {
            me.setClass(this, breakfastSelection.class);
            startActivity(me);
        }
        if(id == btLunch.getId()) {
            me.setClass(this, lunchSelection.class);
            startActivity(me);
        }
        if(id == btDinner.getId()) {
            me.setClass(this, dinnerSelection.class);
            startActivity(me);
        }
        if(id == btFinish.getId()) {
            if(me.hasExtra("breakfast") || me.hasExtra("lunch") || me.hasExtra("dinner")){
                me.setClass(mealsMenu.this, finishMeals.class);
                updateIngredientsAmount();

                if(selectedMeals[0] != null)
                    me.putExtra("selectedBreakfast", selectedMeals[0]);
                if(selectedMeals[1] != null)
                    me.putExtra("selectedLunch", selectedMeals[1]);
                if(selectedMeals[2] != null)
                    me.putExtra("selectedDinner", selectedMeals[2]);

                me.putExtra("totalProteins", totalProteins);
                me.putExtra("totalFats", totalFats);
                me.putExtra("totalCalories", totalCalories);
                startActivity(me);
            }
            else{
                Toast.makeText(this, "Please pick at least one meal !", Toast.LENGTH_LONG).show();
            }
        }
    }

    public void updateIngredientsAmount(){
        for(Meal meal : selectedMeals){
            if(meal != null){
                for(int i = 0; i < meal.getNeededIngredientsForMeal().size(); i++){
                    Ingredient ingredient = meal.getNeededIngredientsForMeal().get(i);
                    ingredient.addAmount(1);

                    if(ingredient.getName().equals("olive"))
                        ingredient.addAmount(7);
                }
            }
        }
    }

    public void updateMeals(){
        btBreakfast.setText("Select");
        btLunch.setText("Select");
        btDinner.setText("Select");

        if(me.hasExtra("breakfast")){
            breakfast = (Meal) me.getSerializableExtra("breakfast");
            tvBreakfast.setText("Your breakfast is: " + breakfast.getName() + ".");
            btBreakfast.setText("Change breakfast");
            totalProteins += breakfast.getProteins();
            totalFats += breakfast.getFats();
            totalCalories += breakfast.getCalories();
            selectedMeals[0] = breakfast;
        }

        if(me.hasExtra("lunch")){
            lunch = (Meal) me.getSerializableExtra("lunch");
            tvLunch.setText("Your lunch is: " + lunch.getName() + ".");
            btLunch.setText("Change lunch");
            totalProteins += lunch.getProteins();
            totalFats += lunch.getFats();
            totalCalories += lunch.getCalories();
            selectedMeals[1] = lunch;
        }

        if(me.hasExtra("dinner")){
            dinner = (Meal) me.getSerializableExtra("dinner");
            tvDinner.setText("Your dinner is: " + dinner.getName() + ".");
            btDinner.setText("Change dinner");
            totalProteins += dinner.getProteins();
            totalFats += dinner.getFats();
            totalCalories += dinner.getCalories();
            selectedMeals[2] = dinner;
        }

        tvTotalProteins.setText("Total proteins: " + Math.round(totalProteins * 1000.0) / 1000.0 + " .");
        tvTotalFats.setText("Total fats: " + Math.round(totalFats * 1000.0) / 1000.0 + " .");
        tvTotalCalories.setText("Total calories: " + Math.round(totalCalories * 1000.0) / 1000.0 + " .");
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
        mediaPlayer = MediaPlayer.create(mealsMenu.this, R.raw.my_song);
        mediaPlayer.setLooping(true);
        if(me.getBooleanExtra("playMusic", true)){
            mediaPlayer.start();
        }
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
            me.setClass(mealsMenu.this, settingsSetter.class);
            me.putExtra("cameToSettingsFrom", getLocalClassName());
            startActivity(me);
        }
        return super.onOptionsItemSelected(item);
    }
}