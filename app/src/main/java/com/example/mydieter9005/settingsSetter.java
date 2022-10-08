package com.example.mydieter9005;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class settingsSetter extends AppCompatActivity {

    RadioGroup rgPlayMusic, rgUseVideos, rgUseManuallySave;
    Button btReturnToRecentActivity, btSaveSettingsChanges;
    boolean playMusic, useVideos, useManuallySave;
    boolean wantToSave = false, chooseIfWantToSave = false, needSave = true;
    boolean playMusicAtStart, useVideosAtStart, useManuallySaveAtStart;

    FileOutputStream fos;
    OutputStreamWriter osw;
    BufferedWriter bw;
    String fileName = "settings";

    FileInputStream is;
    InputStreamReader isr;
    BufferedReader br;
    Intent me;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_setter);

        me = getIntent();

        btReturnToRecentActivity = (Button) findViewById(R.id.btReturnToRecentActivity);
        btSaveSettingsChanges = (Button) findViewById(R.id.btSaveSettingsChanges);

        rgPlayMusic = (RadioGroup) findViewById(R.id.rgPlayMusic);
        rgUseVideos = (RadioGroup) findViewById(R.id.rgUseVideos);
        rgUseManuallySave = (RadioGroup) findViewById(R.id.rgUseManuallySave);

        implementSettingsData();
    }

    public void saveSettings(View v){
        getRadioGroupsOptionsSelected();

        try {
            fos = openFileOutput(fileName, Context.MODE_PRIVATE);
            osw = new OutputStreamWriter(fos);
            bw = new BufferedWriter(osw);

            bw.write("Play music ?: " + playMusic + "\n");
            bw.write("Use Videos ?: " + useVideos + "\n");
            bw.write("Use manually Save ?: " + useManuallySave);

            bw.close();
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

            if(!Boolean.parseBoolean(settingsParts[0].split(": ")[1]))
                rgPlayMusic.check(R.id.rbMuteMusic);
            if(!Boolean.parseBoolean(settingsParts[1].split(": ")[1]))
                rgUseVideos.check(R.id.rbUseImages);
            if(!Boolean.parseBoolean(settingsParts[2].split(": ")[1]))
                rgUseManuallySave.check(R.id.rbAutoSave);

            getRadioGroupsOptionsSelected();
            playMusicAtStart = playMusic;
            useVideosAtStart = useVideos;
            useManuallySaveAtStart = useManuallySave;
        }
    }

    public void getRadioGroupsOptionsSelected(){
        int radioID;

        radioID = rgPlayMusic.getCheckedRadioButtonId();
        playMusic = radioID == R.id.rbPlayMusic;

        radioID = rgUseVideos.getCheckedRadioButtonId();
        useVideos = radioID == R.id.rbUseVideos;

        radioID = rgUseManuallySave.getCheckedRadioButtonId();
        useManuallySave = radioID == R.id.rbManuallySave;
    }

    public void goToMusicMaster(View v){
        me.setClass(settingsSetter.this, musicMaster.class);
        startActivity(me);
    }

    public void returnToRecentActivity(View v){
        getRadioGroupsOptionsSelected();
        if(!chooseIfWantToSave || needSave){
            if(((playMusicAtStart != playMusic) || (useVideosAtStart != useVideos) || (useManuallySaveAtStart != useManuallySave)) && !chooseIfWantToSave)
                checkIfWantToSave();
            else {
                needSave = false;
                chooseIfWantToSave = true; // Don't need to save because nothing changed.
                returnToRecentActivity(null);
            }
        }
        else{
            if(wantToSave){
                saveSettings(null);
            }
            String cameToSettingsFrom = me.getStringExtra("cameToSettingsFrom");
            if(cameToSettingsFrom.equals("MainActivity"))
                me.setClass(settingsSetter.this, MainActivity.class);
            if(cameToSettingsFrom.equals("mealsMenu"))
                me.setClass(settingsSetter.this, mealsMenu.class);
            if(cameToSettingsFrom.equals("breakfastSelection"))
                me.setClass(settingsSetter.this, breakfastSelection.class);
            if(cameToSettingsFrom.equals("lunchSelection"))
                me.setClass(settingsSetter.this, lunchSelection.class);
            if(cameToSettingsFrom.equals("dinnerSelection"))
                me.setClass(settingsSetter.this, dinnerSelection.class);
            if(cameToSettingsFrom.equals("ingredientsPickup"))
                me.setClass(settingsSetter.this, ingredientsPickup.class);
            if(cameToSettingsFrom.equals("finishMeals"))
                me.setClass(settingsSetter.this, finishMeals.class);
            if(cameToSettingsFrom.equals("customMeals"))
                me.setClass(settingsSetter.this, customMeals.class);
            if(cameToSettingsFrom.equals("customSelection"))
                me.setClass(settingsSetter.this, customSelection.class);
            if(cameToSettingsFrom.equals("mealModifier"))
                me.setClass(settingsSetter.this, mealModifier.class);

            startActivity(me);
        }
    }

    public void checkIfWantToSave(){
        AlertDialog ad;
        AlertDialog.Builder adb;
        adb = new AlertDialog.Builder(this);
        adb.setTitle("Your settings are: ");
        adb.setMessage("Play music ?: " + playMusic + "\n" + "Use videos ?: " + useVideos + "\n" + "Use manually save ?: " + useManuallySave);
        adb.setNegativeButton("Continue", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });

        adb.setNeutralButton("Don't save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                chooseIfWantToSave = true;
                wantToSave = false;
                returnToRecentActivity(null);
            }
        });

        adb.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                chooseIfWantToSave = true;
                wantToSave = true;
                returnToRecentActivity(null);
            }
        });

        ad = adb.create();
        ad.show();
    }

    public void resetToPreviousSettings(){
        rgPlayMusic.check(R.id.rbPlayMusic);
        if(!playMusicAtStart)
            rgPlayMusic.check(R.id.rbMuteMusic);

        rgUseVideos.check(R.id.rbUseVideos);
        if(!useVideosAtStart)
            rgUseVideos.check(R.id.rbUseImages);

        rgUseManuallySave.check(R.id.rbManuallySave);
        if(!useManuallySaveAtStart)
            rgUseManuallySave.check(R.id.rbAutoSave);
    }

    @Override
    public boolean onCreateOptionsMenu(@NonNull Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.settings_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemID = item.getItemId();
        if(itemID == R.id.resetToPreviousSettings){
            resetToPreviousSettings();
        }
        return super.onOptionsItemSelected(item);
    }
}