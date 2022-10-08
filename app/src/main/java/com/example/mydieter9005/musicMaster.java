package com.example.mydieter9005;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class musicMaster extends AppCompatActivity {

    private MediaPlayer mediaPlayer;
    private VideoView videoView;

    ImageButton ibtPauseOrPlayMusic, ibtLowerMusicVolume, ibtHigherMusicVolume;
    LinearLayout musicMasterLinearLayout;
    Button btFinishMusicMaster;
    RadioGroup rgMusicChose;

    ArrayList<Song> songs = Song.getSongs();
    boolean isPlaying = true;
    final int maxVolume = 100;
    int currentVolume = 100;
    Song activeSong;

    FileInputStream is;
    InputStreamReader isr;
    BufferedReader br;
    String fileName = "settings";
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

        ibtLowerMusicVolume = (ImageButton) findViewById(R.id.ibtLowerMusicVolume);
        ibtHigherMusicVolume = (ImageButton) findViewById(R.id.ibtHigherMusicVolume);

        ibtPauseOrPlayMusic = (ImageButton) findViewById(R.id.ibtPauseOrPlayMusic);
        ibtPauseOrPlayMusic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
        });

        btFinishMusicMaster = (Button) findViewById(R.id.btFinishMusicMaster);

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

//    public void adjustVolumeChange(View v){
//        int viewId = v.getId();
//
//        if(viewId == ibtLowerMusicVolume.getId())
//            currentVolume -= 10;
//        if(viewId == ibtLowerMusicVolume.getId())
//            currentVolume += 10;
//
//        float volume = (float)(Math.log(maxVolume-currentVolume)/Math.log(maxVolume));
//        mediaPlayer.setVolume(volume, volume);
//    }

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
        activeSong.playSong();
        Toast.makeText(this, Song.getActiveSongIndex() + "", Toast.LENGTH_SHORT).show();
        RadioButton rbSelected = (RadioButton) rgMusicChose.getChildAt(Song.getActiveSongIndex());
        rgMusicChose.check(rbSelected.getId());
    }

    public void finish(View v){
        me.setClass(musicMaster.this, settingsSetter.class);
        me.putExtra("activeSong", activeSong);
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
            activeSong = Song.getSongByName(settingsParts[3].split(": ")[1]);

            me.putExtra("playMusic", playMusic);
            me.putExtra("useVideos", useVideos);
            me.putExtra("useManuallySave", useManuallySave);
            me.putExtra("activeSong", activeSong);
        }
    }

    public void initiateVideoPlayer(){
        Uri uri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.morning_background_video);
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
        if(me.getBooleanExtra("playMusic", true)){
            mediaPlayer.start();
        }
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

//    @Override
//    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
//        int itemID = item.getItemId();
//        if (itemID == R.id.resetToPreviousSettings) {
//            resetToPreviousMusic();
//        }
//        return super.onOptionsItemSelected(item);
//    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        videoView.resume();
        if(!me.getBooleanExtra("useVideos", true)){
            findViewById(R.id.breakfastSelectionLinearLayout).setBackground(getDrawable(R.drawable.morning_background));
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
}