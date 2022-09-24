package com.example.mydieter9005;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class customMeals extends AppCompatActivity {

    private MediaPlayer mediaPlayer;
    private VideoView videoView;

    Button btFinishCustomize;
    TextView tvInstructions;
    EditText customMeal;
    String[] mealInfo;
    String cameFrom;

    FileOutputStream fos;
    OutputStreamWriter osw;
    BufferedWriter bw;
    String fileName = "savedCustomMeals";

    FileInputStream is;
    InputStreamReader isr;
    BufferedReader br;
    Intent me;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_meals);

        me = getIntent();
        cameFrom = me.getStringExtra("cameFrom");
        Toast.makeText(this, "selected meal: " + cameFrom, Toast.LENGTH_SHORT).show();

        btFinishCustomize = (Button) findViewById(R.id.btFinishCustomize);
        videoView = (VideoView) findViewById(R.id.customVideoView);
        tvInstructions = (TextView) findViewById(R.id.tvInstructions);
        customMeal = (EditText) findViewById(R.id.customMeal);

        writeTheInstructions();
        initiateVideoPlayer();
        initiateMediaPlayer();
        implementSettingsData();
    }

    public void writeTheInstructions(){
        tvInstructions.setText(
                "Make sure to use words like: 'with', 'and' and 'include' instead of using ','." + "\n" +
                "You need to use the special word 'flavored' to make something flavored, like this: " + "\n" +
                "chocolate flavored ice cream or chocolate flavored yogurt and so on..." + "\n" +
                "You can also use 'mini-meals' like: salad, toast and cereals in your text." + "\n" +
                "Not every ingredient have photo so it will just show you the name."
        );
    }

    public void setCustomFood(View v){
        String meal = customMeal.getText().toString();
        String testMeal;
        if(meal.contains(":") && meal.contains(",") && meal.contains(".") && meal.contains(" ")){
            testMeal = meal.replaceAll(":", "");
            if(meal.length() != testMeal.length() + 1){
                Toast.makeText(this, "There should be one ':' .", Toast.LENGTH_SHORT).show();
            }
            else{
                testMeal = meal.replaceAll(",", "");
                if(meal.length() != testMeal.length() + 1){
                    Toast.makeText(this, "There should be one ',' .", Toast.LENGTH_SHORT).show();
                }
                else{
                    testMeal = meal.replaceAll("\\.", "");
                    if(meal.length() != testMeal.length() + 1){
                        Toast.makeText(this, "There should be one '.' .", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        if(meal.contains("_") || meal.contains("-")){
                            Toast.makeText(this, "Make sure to use only the format symbols.", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            viewInfo();
                        }
                    }
                }
            }
        }
        else{
            Toast.makeText(this, "Make sure to follow the format instructions.", Toast.LENGTH_SHORT).show();
        }
    }

    public void viewInfo(){
        AlertDialog ad;
        AlertDialog.Builder adb;
        adb = new AlertDialog.Builder(this);
        mealInfo = multiUsageFunctions.organizeMeal(customMeal.getText().toString());
        adb.setTitle("Your meal is: ");
        adb.setMessage("Name: " + mealInfo[0] + "\n" + "Calories: " + mealInfo[1] + "\n" + "Minutes: " + mealInfo[2]);
        adb.setNegativeButton("Retry", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });

        adb.setNeutralButton("Finish", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finishCustomize();
            }
        });

        adb.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                saveCustomMeal();
            }
        });

        ad = adb.create();
        ad.show();
    }

    public void sendToCustomSelection(View v){
        me.setClass(customMeals.this, customSelection.class);
        startActivity(me);
    }

    public void finishCustomize(){
        me.setClass(customMeals.this, mealsMenu.class);
        me.putExtra(cameFrom, customMeal.getText().toString());
        startActivity(me);
    }

    public void saveCustomMeal(){
        try {
            fos = openFileOutput(fileName, Context.MODE_APPEND);
            osw = new OutputStreamWriter(fos);
            bw = new BufferedWriter(osw);

            bw.write(customMeal.getText().toString() + "\n");

            bw.close();

            Toast.makeText(this, "Your custom meal has saved.", Toast.LENGTH_SHORT).show();
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
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

    public void initiateVideoPlayer(){
        Uri uri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.custom_background_video);
        videoView.setVideoURI(uri);
        videoView.start();

        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.setLooping(true);
            }
        });
    }

    public void initiateMediaPlayer(){
        mediaPlayer = MediaPlayer.create(customMeals.this, R.raw.my_song);
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
            me.setClass(customMeals.this, settingsSetter.class);
            me.putExtra("cameToSettingsFrom", getLocalClassName());
            startActivity(me);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPostResume() {
        videoView.resume();
        super.onPostResume();
    }

    @Override
    protected void onRestart() {
        videoView.start();
        super.onRestart();
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
        videoView.suspend();
        mediaPlayer.pause();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        videoView.stopPlayback();
        mediaPlayer.stop();
        mediaPlayer.release();
        super.onDestroy();
    }
}