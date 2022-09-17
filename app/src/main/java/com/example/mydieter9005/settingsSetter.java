package com.example.mydieter9005;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

public class settingsSetter extends AppCompatActivity {

    RadioGroup rgPlayMusic, rgUseVideos, rgUseManuallySave;
    Button btReturnToRecentActivity, btSaveSettingsChanges;
    boolean playMusic, useVideos, useManuallySave;

    FileOutputStream fos;
    OutputStreamWriter osw;
    BufferedWriter bw;
    String fileName = "settings";
    Intent me;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_setter);

        me = getIntent();

        rgPlayMusic = (RadioGroup) findViewById(R.id.rgPlayMusic);
        rgUseVideos = (RadioGroup) findViewById(R.id.rgUseVideos);
        rgUseManuallySave = (RadioGroup) findViewById(R.id.rgUseManuallySave);

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

    public void getRadioGroupsOptionsSelected(){
        int radioID;

        radioID = rgPlayMusic.getCheckedRadioButtonId();
        playMusic = false;
        if(radioID == R.id.rbPlayMusic)
            playMusic = true;

        radioID = rgUseVideos.getCheckedRadioButtonId();
        useVideos = false;
        if(radioID == R.id.rbUseVideos)
            useVideos = true;

        radioID = rgUseManuallySave.getCheckedRadioButtonId();
        useManuallySave = false;
        if(radioID == R.id.rbManuallySave)
            useManuallySave = true;
    }

    public void returnToRecentActivity(View v){
        finish();
    }
}