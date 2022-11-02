package com.example.mydieter9005;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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
import java.util.ArrayList;

public class musicMaster extends AppCompatActivity implements View.OnClickListener {

    private MediaPlayer mediaPlayer;
    private VideoView videoView;

    ImageButton ibtPauseOrPlayMusic, ibtLowerMusicVolume, ibtHigherMusicVolume;
    LinearLayout musicMasterLinearLayout;
    Button btFinishMusicMaster;
    RadioGroup rgMusicChose;

    ArrayList<Song> songs = Song.getSongs();
    boolean isPlaying = true;
    Song activeSong;

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
        setContentView(R.layout.activity_music_master);

        me = getIntent();
        if(me.hasExtra("activeSong"))
            activeSong = (Song) me.getSerializableExtra("activeSong");

        videoView = (VideoView) findViewById(R.id.musicMasterVideoView);
        musicMasterLinearLayout = (LinearLayout) findViewById(R.id.musicMasterLinearLayout);

        ibtPauseOrPlayMusic = (ImageButton) findViewById(R.id.ibtPauseOrPlayMusic);
        ibtPauseOrPlayMusic.setOnClickListener(this);
        btFinishMusicMaster = (Button) findViewById(R.id.btFinishMusicMaster);
        btFinishMusicMaster.setOnClickListener(this);

        rgMusicChose = (RadioGroup) findViewById(R.id.rgMusicChose);
        rgMusicChose.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                getRadioGroupOptionSelected();
            }
        });

        implementSettingsData();
        initiateRadioButtons();
        initiateMediaPlayer();
        initiateVideoPlayer();
    }

    public void getRadioGroupOptionSelected(){
        int radioID = rgMusicChose.getCheckedRadioButtonId();

        activeSong = Song.getSongByName((String)(((RadioButton) findViewById(radioID)).getText()));
        changeMediaPlayerSong();
    }

    public void initiateRadioButtons(){
        for(int i = 0; i < rgMusicChose.getChildCount(); i++){
            RadioButton rbCurrent = (RadioButton) rgMusicChose.getChildAt(i);
            rbCurrent.setText(songs.get(i).getName().replaceAll("_", " "));
        }

        // Not working because of some reason:
        RadioButton rbSelected = (RadioButton) rgMusicChose.getChildAt(Song.getActiveSongIndex());
        rgMusicChose.check(rbSelected.getId());
    }

    public void startOrPauseMusic(){
        if(!isPlaying) {
            ibtPauseOrPlayMusic.setImageResource(R.drawable.ic_pause_music_icon);
            mediaPlayer.start();
            isPlaying = true;
        }
        else{
            ibtPauseOrPlayMusic.setImageResource(R.drawable.ic_play_music_icon);
            mediaPlayer.pause();
            isPlaying = false;
        }
    }

    public void finishMusicMaster(){
        String cameToMusicMasterFrom = me.getStringExtra("cameToMusicMasterFrom");
        if(cameToMusicMasterFrom.equals("MainActivity"))
            me.setClass(musicMaster.this, MainActivity.class);
        if(cameToMusicMasterFrom.equals("mealsMenu"))
            me.setClass(musicMaster.this, mealsMenu.class);
        if(cameToMusicMasterFrom.equals("breakfastSelection"))
            me.setClass(musicMaster.this, breakfastSelection.class);
        if(cameToMusicMasterFrom.equals("lunchSelection"))
            me.setClass(musicMaster.this, lunchSelection.class);
        if(cameToMusicMasterFrom.equals("dinnerSelection"))
            me.setClass(musicMaster.this, dinnerSelection.class);
        if(cameToMusicMasterFrom.equals("ingredientsPickup"))
            me.setClass(musicMaster.this, ingredientsPickup.class);
        if(cameToMusicMasterFrom.equals("finishMeals"))
            me.setClass(musicMaster.this, finishMeals.class);
        if(cameToMusicMasterFrom.equals("customMeals"))
            me.setClass(musicMaster.this, customMeals.class);
        if(cameToMusicMasterFrom.equals("customSelection"))
            me.setClass(musicMaster.this, customSelection.class);
        if(cameToMusicMasterFrom.equals("mealModifier"))
            me.setClass(musicMaster.this, mealModifier.class);
        if(cameToMusicMasterFrom.equals("settingsSetter"))
            me.setClass(musicMaster.this, settingsSetter.class);
        if(cameToMusicMasterFrom.equals("ingredientsSelection"))
            me.setClass(musicMaster.this, ingredientsSelection.class);
        if(cameToMusicMasterFrom.equals("WorldSavedCustomMeals"))
            me.setClass(musicMaster.this, WorldSavedCustomMeals.class);
        if(cameToMusicMasterFrom.equals("Register"))
            me.setClass(musicMaster.this, Register.class);
        if(cameToMusicMasterFrom.equals("Login"))
            me.setClass(musicMaster.this, Login.class);
        if(cameToMusicMasterFrom.equals("UserInfoScreen"))
            me.setClass(musicMaster.this, UserInfoScreen.class);
        if(cameToMusicMasterFrom.equals("ProfilePictureSelection"))
            me.setClass(musicMaster.this, ProfilePictureSelection.class);

        me.putExtra("activeSong", activeSong);
        saveSong();
        startActivity(me);
    }

    public void saveSong(){  // Because saved inside the "settings" file, the current settings are needed.
        Boolean playMusic, useVideos, useManuallySave, useDigitalClock;
        String[] settingsParts = getFileData("settings").split("\n");

        playMusic = Boolean.parseBoolean(settingsParts[0].split(": ")[1]);
        useVideos = Boolean.parseBoolean(settingsParts[1].split(": ")[1]);
        useManuallySave = Boolean.parseBoolean(settingsParts[2].split(": ")[1]);
        useDigitalClock = Boolean.parseBoolean(settingsParts[4].split(": ")[1]);

        try {
            fos = openFileOutput(fileName, Context.MODE_PRIVATE);
            osw = new OutputStreamWriter(fos);
            bw = new BufferedWriter(osw);

            bw.write("Play music ?: " + playMusic + "\n");
            bw.write("Use Videos ?: " + useVideos + "\n");
            bw.write("Use manually Save ?: " + useManuallySave + "\n");
            bw.write("Active song name: " + activeSong.getName() + "\n");
            bw.write("Use digital clock ?: " + useDigitalClock);

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

    public void initiateVideoPlayer(){
        Uri uri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.music_master_video_background);
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
        mediaPlayer = MediaPlayer.create(musicMaster.this, activeSong.getId());
        mediaPlayer.setLooping(true);
        mediaPlayer.start();
    }

    public void changeMediaPlayerSong(){
        mediaPlayer.reset();
        initiateMediaPlayer();
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
        if (itemID == R.id.resetToPreviousSettings) {
            resetToPreviousMusic();
        }
        return super.onOptionsItemSelected(item);
    }

    private void resetToPreviousMusic() {
        if(Song.getActiveSongIndex() != 0)
            songs.get(0).playSong();
        activeSong = Song.getActiveSong();
        rgMusicChose.check(R.id.rbMusicTrack1);
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        videoView.resume();
        if(!me.getBooleanExtra("useVideos", true)){
            musicMasterLinearLayout.setBackground(getDrawable(R.drawable.music_master_background));
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

        if(viewId == ibtPauseOrPlayMusic.getId())
            startOrPauseMusic();

        if(viewId == btFinishMusicMaster.getId())
            finishMusicMaster();
    }
}