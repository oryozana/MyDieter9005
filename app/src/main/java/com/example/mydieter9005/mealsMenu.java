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
    TextView tvTotalCalories, tvTotalTime;
    Button btBreakfast, btLunch, btDinner, btFinish;
    String breakfast = "", lunch = "", dinner = "";
    String[] list, meals = new String[3];
    int totalCalories = 0, totalTime = 0;

    FileInputStream is;
    InputStreamReader isr;
    BufferedReader br;
    String currentLine, allData;
    Intent me;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meals_menu);

        me = getIntent();
        initiateMediaPlayer();

        tvBreakfast = (TextView) findViewById(R.id.tvBreakfast);
        tvLunch = (TextView) findViewById(R.id.tvLunch);
        tvDinner = (TextView) findViewById(R.id.tvDinner);

        btBreakfast = (Button) findViewById(R.id.btBreakfast);
        btLunch = (Button) findViewById(R.id.btLunch);
        btDinner = (Button) findViewById(R.id.btDinner);
        btFinish = (Button) findViewById(R.id.btFinish);

        tvTotalCalories = (TextView) findViewById(R.id.tvTotalCalories);
        tvTotalTime = (TextView) findViewById(R.id.tvTotalTime);

        implementSettingsData();
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
                me.setClass(this, ingredientsPickup.class);
                me.putExtra("totalCalories", totalCalories);
                me.putExtra("meals", meals);
                startActivity(me);
            }
            else{
                Toast.makeText(this, "Please pick at least one meal !", Toast.LENGTH_LONG).show();
            }
        }
    }

    public void updateMeals(){
        if(me.hasExtra("breakfast")){
            breakfast = me.getStringExtra("breakfast");
            list = multiUsageFunctions.organizeMeal(breakfast);
            tvBreakfast.setText("Your breakfast is: " + list[0] + ".");
            btBreakfast.setText(list[1] + "." + "\n" + list[2]);
            totalCalories += Integer.parseInt(multiUsageFunctions.separateInfo(list[1]));
            totalTime += Integer.parseInt(multiUsageFunctions.separateInfo(list[2]));
            meals[0] = list[0];
        }

        if(me.hasExtra("lunch")){
            lunch = me.getStringExtra("lunch");
            list = multiUsageFunctions.organizeMeal(lunch);
            tvLunch.setText("Your lunch is: " + list[0] + ".");
            btLunch.setText(list[1] + "." + "\n" + list[2]);
            totalCalories += Integer.parseInt(multiUsageFunctions.separateInfo(list[1]));
            totalTime += Integer.parseInt(multiUsageFunctions.separateInfo(list[2]));
            meals[1] = list[0];
        }

        if(me.hasExtra("dinner")){
            dinner = me.getStringExtra("dinner");
            list = multiUsageFunctions.organizeMeal(dinner);
            tvDinner.setText("Your dinner is: " + list[0] + ".");
            btDinner.setText(list[1] + "." + "\n" + list[2]);
            totalCalories += Integer.parseInt(multiUsageFunctions.separateInfo(list[1]));
            totalTime += Integer.parseInt(multiUsageFunctions.separateInfo(list[2]));
            meals[2] = list[0];
        }

        tvTotalCalories.setText("Total calories: " + totalCalories + " calories.");
        tvTotalTime.setText("Total time: " + totalTime + " minutes.");
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
        if(me.getBooleanExtra("playMusic", true)){
            mediaPlayer = MediaPlayer.create(mealsMenu.this, R.raw.my_song);
            mediaPlayer.setLooping(true);
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
            startActivity(me);
        }
        return super.onOptionsItemSelected(item);
    }
}