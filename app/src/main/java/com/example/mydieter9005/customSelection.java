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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.VideoView;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class customSelection extends AppCompatActivity {

    private MediaPlayer mediaPlayer;
    private VideoView videoView;

    Button btMultiCustomSelect, btFinishCustomSelection;
    ArrayList<String> mealsList;
    ArrayAdapter<String> adapter;
    boolean multiSelect = false;
    String chosenCustomName = "", cameFrom;
    int chosenCustomCalories = 0, chosenCustomMinutes = 0, multiSelectCounter = 0;
    Song activeSong = Song.getSongs().get(0);
    ListView listView;

    FileInputStream is;
    InputStreamReader isr;
    BufferedReader br;
    String fileName = "savedCustomMeals";
    Intent me;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_selection);

        me = getIntent();
        if(me.hasExtra("activeSong"))
            activeSong = (Song) me.getSerializableExtra("activeSong");
        cameFrom = me.getStringExtra("cameFrom");

        showFileData();

        listView = (ListView) findViewById(R.id.listViewCustom);
        videoView = (VideoView) findViewById(R.id.customSelectionVideoView);

        btMultiCustomSelect = (Button) findViewById(R.id.btMultiCustomSelect);
        btFinishCustomSelection = (Button) findViewById(R.id.btFinishCustomSelection);

        setListViewFields();
        implementSettingsData();
        initiateVideoPlayer();
        initiateMediaPlayer();
    }

    public void setListViewFields(){
        String[] fields = new String[mealsList.size() / 3];
        for(int i = 0; i < mealsList.size(); i += 3){
            String field = mealsList.get(i) + ": " + mealsList.get(i + 1) + " calories, " + mealsList.get(i + 2) + " minutes.";
            fields[i / 3] = field;
        }
        setListViewAdapter(fields);
    }

    public void setListViewAdapter(String[] fields){
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, fields);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String selectedItem = (String) adapterView.getItemAtPosition(i);

                if(!multiSelect){
                    me.setClass(customSelection.this, mealsMenu.class);
                    me.putExtra(cameFrom, selectedItem);
                    startActivity(me);
                }
                else{
                    String[] mealInfo = multiUsageFunctions.organizeMeal(selectedItem);
                    Toast.makeText(customSelection.this, mealInfo[0] + " has added.", Toast.LENGTH_SHORT).show();
                    chosenCustomName += mealInfo[0].toLowerCase() + " and ";
                    chosenCustomCalories += multiUsageFunctions.getCaloriesOrMinutesOutOfString(mealInfo[1]);
                    chosenCustomMinutes += multiUsageFunctions.getCaloriesOrMinutesOutOfString(mealInfo[2]);
                    multiSelectCounter += 1;
                }
            }
        });
    }

    public void multiOrSingleSelectUpdate(View v){
        if(!multiSelect){
            Toast.makeText(this, "Multi select has enabled.", Toast.LENGTH_SHORT).show();
            btMultiCustomSelect.setText("Disable multi select");
            btFinishCustomSelection.setText("Finish selection");
            multiSelectCounter = 0;
            multiSelect = true;
        }
        else{
            Toast.makeText(this, "Multi select has disabled.", Toast.LENGTH_SHORT).show();
            btMultiCustomSelect.setText("Enable multi select");
            btFinishCustomSelection.setText("Return to customize");
            multiSelectCounter = 0;
            multiSelect = false;
        }
    }

    public void finishSelectionOrReturnToCustomize(View v){
        if(multiSelect){
            if(multiSelectCounter == 0){
                Toast.makeText(this, "You didn't choose anything yet.", Toast.LENGTH_SHORT).show();
            }
            else{
                chosenCustomName = chosenCustomName.substring(0, chosenCustomName.length() - 5);  // 5 = " and "
                String chosenCustom = chosenCustomName + ": " + chosenCustomCalories + " calories, " + chosenCustomMinutes + " minutes.";
                me.setClass(customSelection.this, mealsMenu.class);
                me.putExtra(cameFrom, chosenCustom);
                startActivity(me);
            }
        }
        else{
            me.setClass(customSelection.this, customMeals.class);
            startActivity(me);
        }
    }

    public void showFileData(){
        mealsList = new ArrayList<String>();
        String[] dataParts = getFileData(fileName).split("\n"), organizedMeal;

        for(String dataPart : dataParts){
            organizedMeal = multiUsageFunctions.organizeMeal(dataPart);

            mealsList.add(multiUsageFunctions.separateInfo(organizedMeal[0]));
            mealsList.add(multiUsageFunctions.separateInfo(organizedMeal[1]));
            mealsList.add(multiUsageFunctions.separateInfo(organizedMeal[2]));
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
            activeSong = Song.getSongByName(settingsParts[3].split(": ")[1]);

            me.putExtra("playMusic", playMusic);
            me.putExtra("useVideos", useVideos);
            me.putExtra("useManuallySave", useManuallySave);
            me.putExtra("activeSong", activeSong);
        }
    }

    public void initiateVideoPlayer(){
        Uri uri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.custom_selection_background_video);
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
        mediaPlayer = MediaPlayer.create(customSelection.this, activeSong.getId());
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
            me.setClass(customSelection.this, settingsSetter.class);
            me.putExtra("cameToSettingsFrom", getLocalClassName());
            startActivity(me);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        videoView.resume();
        if(!me.getBooleanExtra("useVideos", true)){
            findViewById(R.id.customSelectionLinearLayout).setBackground(getDrawable(R.drawable.custom_selection_background));
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