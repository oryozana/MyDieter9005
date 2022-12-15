package com.example.mydieter9005;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

public class mealsMenu extends AppCompatActivity implements View.OnClickListener {

    private MediaPlayer mediaPlayer;
    TextView tvBreakfast, tvLunch, tvDinner;
    TextView tvTotalProteins, tvTotalFats, tvTotalCalories;
    Button btBreakfast, btLunch, btDinner, btFinish;

    DailyMenu todayMenu = DailyMenu.getTodayMenu();
    Song activeSong = Song.getSongs().get(0);

    FileInputStream is;
    InputStreamReader isr;
    BufferedReader br;
    Intent me;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meals_menu);

        me = getIntent();
        if(me.hasExtra("activeSong"))
            activeSong = (Song) me.getSerializableExtra("activeSong");

        tvBreakfast = (TextView) findViewById(R.id.tvBreakfast);
        tvLunch = (TextView) findViewById(R.id.tvLunch);
        tvDinner = (TextView) findViewById(R.id.tvDinner);

        btBreakfast = (Button) findViewById(R.id.btBreakfast);
        btBreakfast.setOnClickListener(this);
        btLunch = (Button) findViewById(R.id.btLunch);
        btLunch.setOnClickListener(this);
        btDinner = (Button) findViewById(R.id.btDinner);
        btDinner.setOnClickListener(this);
        btFinish = (Button) findViewById(R.id.btFinish);
        btFinish.setOnClickListener(this);

        tvTotalProteins = (TextView) findViewById(R.id.tvTotalProteins);
        tvTotalFats = (TextView) findViewById(R.id.tvTotalFats);
        tvTotalCalories = (TextView) findViewById(R.id.tvTotalCalories);

        implementSettingsData();
        initiateMediaPlayer();
        updateMeals();
    }

    public void chooseMealType(){
        AlertDialog ad;
        AlertDialog.Builder adb;
        adb = new AlertDialog.Builder(this);
        adb.setTitle("Choose meal type!");
        adb.setIcon(R.drawable.ic_food_icon);

        LinearLayout alertDialogLinearLayout = new LinearLayout(getApplicationContext());
        alertDialogLinearLayout.setOrientation(LinearLayout.VERTICAL);

        Button chooseBreakfast = new Button(getApplicationContext());
        chooseBreakfast.setOnClickListener(this);
        alertDialogLinearLayout.addView(chooseBreakfast);

        Button chooseLunch = new Button(getApplicationContext());
        chooseLunch.setOnClickListener(this);
        alertDialogLinearLayout.addView(chooseLunch);

        Button chooseDinner = new Button(getApplicationContext());
        chooseDinner.setOnClickListener(this);
        alertDialogLinearLayout.addView(chooseDinner);

        Button chooseCustom = new Button(getApplicationContext());
        chooseCustom.setOnClickListener(this);
        alertDialogLinearLayout.addView(chooseCustom);

        adb.setView(alertDialogLinearLayout);

        adb.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });

        ad = adb.create();
        ad.show();
    }

    public void updateMeals(){
        btBreakfast.setText("Select");
        btLunch.setText("Select");
        btDinner.setText("Select");

        if(todayMenu.hasBreakfast()){
            tvBreakfast.setText("Your breakfast is: " + todayMenu.getUnitedBreakfastName() + ".");
            btBreakfast.setText("Reselect");
        }

        if(todayMenu.hasLunch()){
            tvLunch.setText("Your lunch is: " + todayMenu.getUnitedLunchName() + ".");
            btLunch.setText("Reselect");
        }

        if(todayMenu.hasDinner()){
            tvDinner.setText("Your dinner is: " + todayMenu.getUnitedDinnerName() + ".");
            btDinner.setText("Reselect");
        }

        tvTotalProteins.setText("Total proteins: " + todayMenu.getTotalProteins() + " .");
        tvTotalFats.setText("Total fats: " + todayMenu.getTotalFats() + " .");
        tvTotalCalories.setText("Total calories: " + todayMenu.getTotalCalories() + " .");
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
            activeSong = Song.getSongByName(settingsParts[3].split(": ")[1]);

            me.putExtra("playMusic", playMusic);
            me.putExtra("useVideos", useVideos);
            me.putExtra("useManuallySave", useManuallySave);
            me.putExtra("activeSong", activeSong);
        }
    }

    public void initiateMediaPlayer(){
        mediaPlayer = MediaPlayer.create(mealsMenu.this, activeSong.getId());
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
        if(itemID == R.id.sendToMusicMaster){
            me.setClass(mealsMenu.this, musicMaster.class);
            me.putExtra("cameToMusicMasterFrom", getLocalClassName());
            startActivity(me);
        }

        if(itemID == R.id.sendToSettings){
            me.setClass(mealsMenu.this, settingsSetter.class);
            me.putExtra("cameToSettingsFrom", getLocalClassName());
            startActivity(me);
        }

        if(itemID == R.id.sendToUserScreen){
            me.setClass(mealsMenu.this, UserInfoScreen.class);
            me.putExtra("cameToUserScreenFrom", getLocalClassName());
            startActivity(me);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        int viewId = v.getId();

        if(viewId == btBreakfast.getId()) {
            me.setClass(this, breakfastSelection.class);
            startActivity(me);
        }

        if(viewId == btLunch.getId()) {
            me.setClass(this, lunchSelection.class);
            startActivity(me);
        }

        if(viewId == btDinner.getId()) {
            me.setClass(this, dinnerSelection.class);
            startActivity(me);
        }

        if(viewId == btFinish.getId()) {
            if(todayMenu.isThereAtLeastOneThing()){
                me.setClass(mealsMenu.this, finishMeals.class);
                startActivity(me);
            }
            else
                Toast.makeText(this, "Please pick at least one meal !", Toast.LENGTH_LONG).show();
        }
    }
}