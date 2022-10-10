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
import android.widget.LinearLayout;
import android.widget.RadioGroup;
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

public class settingsSetter extends AppCompatActivity implements View.OnClickListener {

    private VideoView videoView;
    private MediaPlayer mediaPlayer;

    RadioGroup rgPlayMusic, rgUseVideos, rgUseManuallySave;
    Button btReturnToRecentActivity, btChangeMusic;
    boolean playMusic, useVideos, useManuallySave;
    boolean wantToSave = false, chooseIfWantToSave = false, needSave = true;
    boolean playMusicAtStart, useVideosAtStart, useManuallySaveAtStart;
    LinearLayout settingsSetterLinearLayout;
    Song activeSong = Song.getSongs().get(0);
    TextView tvCurrentSongName;

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
        if(me.hasExtra("activeSong"))
            activeSong = (Song) me.getSerializableExtra("activeSong");

        btReturnToRecentActivity = (Button) findViewById(R.id.btReturnToRecentActivity);
        btReturnToRecentActivity.setOnClickListener(this);
        btChangeMusic = (Button) findViewById(R.id.btChangeMusic);
        btChangeMusic.setOnClickListener(this);

        rgPlayMusic = (RadioGroup) findViewById(R.id.rgPlayMusic);
        rgUseVideos = (RadioGroup) findViewById(R.id.rgUseVideos);
        rgUseManuallySave = (RadioGroup) findViewById(R.id.rgUseManuallySave);

        settingsSetterLinearLayout = (LinearLayout) findViewById(R.id.settingsSetterLinearLayout);
        videoView = (VideoView) findViewById(R.id.settingsSetterVideoView);

        tvCurrentSongName = (TextView) findViewById(R.id.tvCurrentSongName);

        implementSettingsData();
        initiateVideoPlayer();
        initiateMediaPlayer();
    }

    public void saveSettings(){
        getRadioGroupsOptionsSelected();

        try {
            fos = openFileOutput(fileName, Context.MODE_PRIVATE);
            osw = new OutputStreamWriter(fos);
            bw = new BufferedWriter(osw);

            bw.write("Play music ?: " + playMusic + "\n");
            bw.write("Use Videos ?: " + useVideos + "\n");
            bw.write("Use manually Save ?: " + useManuallySave + "\n");
            bw.write("Active song name: " + activeSong.getName());

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
            tvCurrentSongName.setText("Current song: " + activeSong.getName().replaceAll("_", " "));
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

    public void goToMusicMaster(){
        me.setClass(settingsSetter.this, musicMaster.class);
        me.putExtra("cameToMusicMasterFrom", getLocalClassName());
        startActivity(me);
    }

    public void returnToRecentActivity(){
        getRadioGroupsOptionsSelected();
        if(!chooseIfWantToSave || needSave){
            if(((playMusicAtStart != playMusic) || (useVideosAtStart != useVideos) || (useManuallySaveAtStart != useManuallySave)) && !chooseIfWantToSave)
                checkIfWantToSave();
            else {
                needSave = false;
                chooseIfWantToSave = true; // Don't need to save because nothing changed.
                returnToRecentActivity();
            }
        }
        else{
            if(wantToSave)
                saveSettings();

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
            if(cameToSettingsFrom.equals("musicMaster"))
                me.setClass(settingsSetter.this, musicMaster.class);
            if(cameToSettingsFrom.equals("ingredientsSelection"))
                me.setClass(settingsSetter.this, ingredientsSelection.class);

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
                returnToRecentActivity();
            }
        });

        adb.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                chooseIfWantToSave = true;
                wantToSave = true;
                returnToRecentActivity();
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

    public void initiateVideoPlayer(){
        Uri uri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.settings_setter_video_background);
        videoView.setVideoURI(uri);

        if(me.getBooleanExtra("useVideos", true))
            videoView.start();

        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.setLooping(true);
            }
        });
    }

    public void initiateMediaPlayer(){
        mediaPlayer = MediaPlayer.create(settingsSetter.this, activeSong.getId());
        mediaPlayer.setLooping(true);
        if(me.getBooleanExtra("playMusic", true)){
            mediaPlayer.start();
        }
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        videoView.resume();
        if(!me.getBooleanExtra("useVideos", true)){
            settingsSetterLinearLayout.setBackground(getDrawable(R.drawable.settings_setter_background));
            videoView.stopPlayback();
        }
        else
            videoView.start();
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

    @Override
    public void onClick(View v) {
        int viewId = v.getId();

        if(viewId == btChangeMusic.getId())
            goToMusicMaster();

        if(viewId == btReturnToRecentActivity.getId())
            returnToRecentActivity();
    }
}