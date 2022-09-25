package com.example.mydieter9005;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
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

public class breakfastSelection extends AppCompatActivity {

    private MediaPlayer mediaPlayer;
    private VideoView videoView;

    Button btSendBreakfastToCustomize, btClearBreakfastSelection, btMultiBreakfastSelect;
    ArrayList<String> mealsList;
    ArrayAdapter<String> adapter;
    boolean multiSelect = false;
    String chosenBreakfastName = "";
    int chosenBreakfastCalories = 0, chosenBreakfastMinutes = 0, multiSelectCounter = 0;
    ListView listView;

    FileInputStream is;
    InputStreamReader isr;
    BufferedReader br;
    Intent me;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_breakfast_selection);

        me = getIntent();

        mealsList = new ArrayList<String>();
        mealsList.add("Nestle cereals with milk");
        mealsList.add("185");
        mealsList.add("1");

        mealsList.add("Chocolate flavored nestle cereals");
        mealsList.add("230");
        mealsList.add("1");

        mealsList.add("Yogurt");
        mealsList.add("100");
        mealsList.add("1");

        mealsList.add("Chocolate flavored yogurt");
        mealsList.add("200");
        mealsList.add("1");

        mealsList.add("Chocolate flavored ice cream");
        mealsList.add("300");
        mealsList.add("1");

        listView = (ListView) findViewById(R.id.listViewBreakfast);
        videoView = (VideoView) findViewById(R.id.breakfastVideoView);

        btSendBreakfastToCustomize = (Button) findViewById(R.id.btSendBreakfastToCustomize);
        btClearBreakfastSelection = (Button) findViewById(R.id.btClearBreakfastSelection);
        btMultiBreakfastSelect = (Button) findViewById(R.id.btMultiBreakfastSelect);

        setListViewFields();
        initiateVideoPlayer();
        initiateMediaPlayer();
        implementSettingsData();
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
                    me.setClass(breakfastSelection.this, mealsMenu.class);
                    me.putExtra("breakfast", selectedItem);
                    startActivity(me);
                }
                else{
                    String[] mealInfo = multiUsageFunctions.organizeMeal(selectedItem);
                    Toast.makeText(breakfastSelection.this, mealInfo[0] + " has added.", Toast.LENGTH_SHORT).show();
                    chosenBreakfastName += mealInfo[0].toLowerCase() + " and ";
                    chosenBreakfastCalories += multiUsageFunctions.getCaloriesOrMinutesOutOfString(mealInfo[1]);
                    chosenBreakfastMinutes += multiUsageFunctions.getCaloriesOrMinutesOutOfString(mealInfo[2]);
                    multiSelectCounter += 1;
                }
            }
        });
    }

    public void sendToCustomize(View v){
        me.setClass(breakfastSelection.this, customMeals.class);
        me.putExtra("cameFrom", "breakfast");
        startActivity(me);
    }

    public void multiOrSingleSelectUpdate(View v){
        if(!multiSelect){
            Toast.makeText(this, "Multi select has enabled.", Toast.LENGTH_SHORT).show();
            btMultiBreakfastSelect.setText("Disable multi select");
            btClearBreakfastSelection.setText("Finish choosing");
            multiSelectCounter = 0;
            multiSelect = true;
        }
        else{
            Toast.makeText(this, "Multi select has disabled.", Toast.LENGTH_SHORT).show();
            btMultiBreakfastSelect.setText("Enable multi select");
            btClearBreakfastSelection.setText("Clear selection");
            multiSelectCounter = 0;
            multiSelect = false;
        }
    }

    public void clearBreakfastSelection(View v){
        if(multiSelect){
            if(multiSelectCounter == 0){
                Toast.makeText(this, "You didn't choose anything yet.", Toast.LENGTH_SHORT).show();
            }
            else{
                chosenBreakfastName = chosenBreakfastName.substring(0, chosenBreakfastName.length() - 5);  // 5 = " and "
                String chosenBreakfast = chosenBreakfastName + ": " + chosenBreakfastCalories + " calories, " + chosenBreakfastMinutes + " minutes.";
                me.setClass(breakfastSelection.this, mealsMenu.class);
                me.putExtra("breakfast", chosenBreakfast);
                startActivity(me);
            }
        }
        else{
            if(me.hasExtra("breakfast")){
                me.removeExtra("breakfast");
                me.setClass(breakfastSelection.this, mealsMenu.class);
                startActivity(me);
            }
            else{
                Toast.makeText(this, "You didn't choose anything yet.", Toast.LENGTH_SHORT).show();
            }
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
        mediaPlayer = MediaPlayer.create(breakfastSelection.this, R.raw.my_song);
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
            me.setClass(breakfastSelection.this, settingsSetter.class);
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